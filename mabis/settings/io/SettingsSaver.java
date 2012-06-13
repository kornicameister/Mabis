package settings.io;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.JFrame;

import logger.MabisLogger;
import mvc.view.WindowClosedListener;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import settings.GlobalPaths;

/**
 * Klasa, ktorej głownym zadaniem jest zapis ustawiem aplikacji do pliku XML.
 * Skalowalna, ponieważ rozszerzenie jej możliwości polega jedynie na dopisaniu
 * linijek kodu dodającego do elementu głownego drzewa XML podrzewo opisujace
 * konkretna grupe ustawien. Wewnetrznie korzysta z JDom
 * 
 * @author tomasz
 * @see Element
 */
public class SettingsSaver extends Settings {

	public SettingsSaver() {
		super();
	}

	@Override
	public void execute() {
		Document doc = null;
		if (!(new File(Settings.pathToXML).exists())) {
			doc = new Document(new Element("mabis"));
			doc.getRootElement().addContent(this.saveRunCountBlock());
			doc.getRootElement().addContent(this.saveWindowPosition());
			doc.getRootElement().addContent(this.saveGlobalPaths());
		} else {
			try {
				doc = new SAXBuilder().build(new File(Settings.pathToXML));
				this.updateRunCountBlock(doc);
				this.updateWindowPositions(doc);
				this.updateGlobalPaths(doc);
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		}
		try {
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			outputter.output(doc, this.initOutput());
		} catch (IOException | SettingsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metoda pobiera z dokumentu XML listę ścieżek i iterując po nich dokonuje 
	 * auktualnienia pozycji ścieżek. 
	 * 
	 * @param doc referencja do dokumetu ustawien XML
	 */
	private void updateGlobalPaths(Document doc) {
		List<?> paths = doc.getRootElement().getChildren("paths");
		paths = ((Element) paths.get(0)).getChildren();
		for(GlobalPaths gp : GlobalPaths.values()){
			if(!gp.isFixed()){
				for (Object e : paths) {
					Element ee = (Element) e;
					if(gp.name().equals(ee.getAttribute("type").getValue())){
						ee.setText(gp.toString());
						break;
					}
				}
			}
		}
	}

	/**
	 * Metoda pobiera z dokumentu XML aktualną listę okienek, których pewne meta-dane
	 * zostały w nim umieszczone. Iterując po liście okienek, które zostały
	 * wytypowane do zapisania swoich ustawień podczas danego uruchomienia aplikacji,
	 * metoda uaktualnia, a jeśli to konieczne zapisuje dane nowe okienka.
	 * 
	 * @param doc referencja do dokumetu ustawien XML
	 */
	private void updateWindowPositions(Document doc) {
		List<?> frames = doc.getRootElement().getChildren("frames");
		for(JFrame frame : SettingsSaver.frames){
			for (short i = 0; i < frames.size(); i++) {
				try{
					Element node  = (Element) ((Element) frames.get(i)).getChildren(frame.getClass().getName()).get(0);
					node.getChild("title").setText(frame.getTitle());
					node.getChild("width").setText(String.valueOf(frame.getWidth()));
					node.getChild("height").setText(String.valueOf(frame.getHeight()));
					node.getChild("xPos").setText(String.valueOf(frame.getX()));
					node.getChild("yPos").setText(String.valueOf(frame.getY()));
				}catch(IndexOutOfBoundsException e){
					MabisLogger.getLogger().log(Level.WARNING,"{0} not found in settings save, saving",frame.getClass().getName());
					Element window = new Element(frame.getClass().getName());
					window.addContent(new Element("title").setText(frame.getTitle()));
					window.addContent(new Element("width").setText(String.valueOf(frame.getWidth())));
					window.addContent(new Element("height").setText(String.valueOf(frame.getHeight())));
					window.addContent(new Element("xPos").setText(String.valueOf(frame.getX())));
					window.addContent(new Element("yPos").setText(String.valueOf(frame.getY())));
					doc.getRootElement().getChild("frames").addContent(window);
				}
			}
		}
	}

	/**
	 * Metoda uaktulnia blok informacyjny uruchomien aplikacji.
	 * 
	 * @param doc referencja do dokumetu ustawien XML
	 */
	private void updateRunCountBlock(Document doc) {
		List<?> paths = doc.getRootElement().getChildren("runs");
		for(Object e : paths){
			Element elem = (Element) e;
			elem.getChild("count").setText(SettingsSaver.RUN_COUNT.toString());
			elem.getChild("lastRun").setText(DateFormat.getDateInstance().format(new Date()));
			elem.getChild("errorFree").setText(Boolean.valueOf(true).toString());
		}
	}

	/**
	 * Metoda odwołuje się do danych o uruchomieniach programu. Zapisując do XML
	 * informacje o ostatnim uruchomieniu aplkikacji.
	 * 
	 * @return element z poddrzewem informacji o ostatnim uruchomieniu
	 */
	private Element saveRunCountBlock() {
		Element r = new Element("runs");
		r.addContent(new Element("count").setText(SettingsSaver.RUN_COUNT.toString()));
		r.addContent(new Element("lastRun").setText(DateFormat.getDateInstance().format(new Date())));
		r.addContent(new Element("errorFree").setText(SettingsSaver.RUN_ERROR_FREE.toString()));
		return r;
	}

	/**
	 * Metoda generuje drzewo XML, tj. Element, opisujące następujące atrybuty
	 * kolejnych okien
	 * <ul>
	 * <li>tytuł</li>
	 * <li>wysokość <em>x</em> szerokość</li>
	 * <li>xPos <em>x</em> yPos</li>
	 * </ul>
	 * Okienka, przeznaczone do umieszczenia w pliku konfiguracyjnym dodawania
	 * są w trakcie działania programu do statycznego {@link TreeSet}
	 * 
	 * @return podrzewo XML opisujące kolejne oienka
	 * @see WindowClosedListener
	 */
	private Element saveWindowPosition() {
		Element frames = new Element("frames");
		for (JFrame f : SettingsSaver.frames) {
			Element window = new Element(f.getClass().getName());
			window.addContent(new Element("title").setText(f.getTitle()));
			window.addContent(new Element("width").setText(String.valueOf(f.getWidth())));
			window.addContent(new Element("height").setText(String.valueOf(f.getHeight())));
			window.addContent(new Element("xPos").setText(String.valueOf(f.getX())));
			window.addContent(new Element("yPos").setText(String.valueOf(f.getY())));
			frames.addContent(window);
		}
		return frames;
	}

	/**
	 * Zapisuje globalne ścieżki, których używa program, a które znajdują się w
	 * {@link GlobalPaths}. Ścieżka zapisywana jest jako :
	 * <p>
	 * <strong>typ</strong> : <i>ścieżka</i>
	 * </p>
	 * 
	 * @return element XML z podrzewem opisującym kolejne ścieżki.
	 * @see GlobalPaths
	 */
	private Element saveGlobalPaths() {
		Element e = new Element("paths");
		for (GlobalPaths p : GlobalPaths.values()) {
			if (!p.isFixed()) {
				Element pp = new Element("path");
				pp.setAttribute(new Attribute("type", p.name()));
				pp.setText(p.toString());
				e.addContent(pp);
			}
		}
		return e;
	}
}
