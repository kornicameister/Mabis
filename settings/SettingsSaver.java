package settings;

import java.io.IOException;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import view.mainwindow.MainWindow;

public class SettingsSaver extends Settings {

	public SettingsSaver() {
		super();
	}

	@Override
	public void execute() {
		try {
			Document doc = new Document(new Element("mabis"));
			
			doc.getRootElement().addContent(this.saveWindowPosition());
			doc.getRootElement().addContent(this.saveGlobalPaths());
			
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			outputter.output(doc, this.initOutput());
		} catch (IOException | SettingsException e) {
			e.printStackTrace();
		}
	}

	private Element saveWindowPosition() {
		MainWindow mw = (MainWindow) SettingsSaver.data.get(SettingDataType.MAIN_WINDOW);
			Element window = new Element("main_window");
			window.addContent(new Element("title").setText(mw.getTitle()));
			window.addContent(new Element("width").setText(String.valueOf(mw.getWidth())));
			window.addContent(new Element("height").setText(String.valueOf(mw.getHeight())));
			window.addContent(new Element("xPos").setText(String.valueOf(mw.getX())));
			window.addContent(new Element("yPos").setText(String.valueOf(mw.getY())));
		return window;
	}

	private Element saveGlobalPaths() {
		Element e = new Element("paths");
		for(GlobalPaths p : GlobalPaths.values()){
			Element pp = new Element("path");
			pp.setAttribute(new Attribute("type", p.name()));
			pp.setText(p.toString());
			e.addContent(pp);
		}
		return e;
	}
}
