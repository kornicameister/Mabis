package settings.io;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.swing.JFrame;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Klasa ładuje ustawienia, niemniej jej działania nie jest zorientowane na
 * załadowanie wszystkich ustawień do konkretnego pliku. Działa zupełnie
 * inaczej. Jeśli pojawia się odwołanie do jakiejść wartości z ustawień klasa
 * odczytuje konkretne poddrzewo XML i odnajduje żądaną wartość. Operacje
 * wykonywane są z użyciem metod statycznych. </br>Klasa jest abstrakcyjna z
 * powodu natury swojego dzialania. Uzywana jedynie wtedy, kiedy jest potrzebna,
 * zapewnie dostep do ustawien zlokalizowanych w pliku uzywajac metod
 * statycznych
 * 
 * @author tomasz
 * 
 */
public abstract class SettingsLoader extends Settings {
	/**
	 * Metoda szuka scieżki zapisane w elemencie XML opisanym przez attribute
	 * <b>type</b> o wartości takiej jak parametr wywołania <i>pathName</i>
	 * 
	 * @param pathName
	 *            nazwa ściezki
	 * @return scieżkę dla podanej nazwy ścieżki
	 * @throws SettingsException
	 */
	public static String load(String pathName) throws SettingsException {
		if (pathName.equals("TMP")) {
			return System.getProperty("java.io.tmpdir") + "/";
		}
		try {
			Element root = ((Document) new SAXBuilder().build(new File(
					SettingsLoader.pathToXML))).getRootElement();

			List<?> paths = root.getChildren("paths");
			paths = ((Element) paths.get(0)).getChildren();

			for (Object e : paths) {
				Element ee = (Element) e;
				if (ee.getAttribute("type").getValue().equals(pathName)) {
					return ee.getText();
				}
			}
		} catch (JDOMException | IOException e) {
			throw new SettingsException(e.getMessage());
		}
		return "";
	}

	/**
	 * Załaduje informacje o JFrame podanym w argumencie. Metoda ładuje grubę
	 * atrybutów opisującą dane okienko. Okienko odnajdywane jest bazując na
	 * wartości zwróconej przez wywołanie metody <b>f.getClass().getName()</i>
	 * 
	 * @param f
	 *            okienko do którego zostaną zapisane ustawienia
	 * @throws SettingsException
	 */
	public static void load(JFrame f) throws SettingsException {
		try {
			Element root = ((Document) new SAXBuilder().build(new File(
					SettingsLoader.pathToXML))).getRootElement();

			List<?> paths = root.getChildren("frames");

			for (short i = 0; i < paths.size(); i++) {
				Element node = (Element) paths.get(i);

				List<?> frames = node.getChildren(f.getClass().getName());
				if (frames.size() == 0) {
					throw new SettingsException(
							"No such frame in settings file");
				}
				for (short j = 0; j < frames.size(); j++) {
					node = (Element) frames.get(j);
					f.setTitle(node.getChildText("title"));
					f.setSize(Integer.valueOf(node.getChildText("width")),
							Integer.valueOf(node.getChildText("height")));
					f.setLocation(Integer.valueOf(node.getChildText("xPos")),
							Integer.valueOf(node.getChildText("yPos")));
				}

			}
		} catch (JDOMException | IOException e) {
			throw new SettingsException(e.getMessage());
		}
	}

	/**
	 * Ładuje opis ostatniego uruchomienia aplikacji i zapisuje do obiektu klasy
	 * {@link LastRunDescription}
	 * 
	 * @return obiekt klasy {@link LastRunDescription} opisujący ostatnie
	 *         uruchomienie programu
	 * @throws SettingsException
	 */
	public static LastRunDescription loadLastRun() {
		Element root;
		try {
			root = ((Document) new SAXBuilder().build(new File(
					SettingsLoader.pathToXML))).getRootElement();
			List<?> runs = root.getChildren("runs");
			if (runs.size() == 0) {
				return new LastRunDescription(0, true, new Date(0));
			}

			Element node = (Element) runs.get(0);
			int count = Integer.decode(node.getChildText("count"));
			SettingsLoader.RUN_COUNT = count;
			boolean status = Boolean.valueOf(node.getChildText("errorFree"));
			Date date = Date.valueOf(node.getChildText("lastRun"));

			return new LastRunDescription(count, status, date);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		return new LastRunDescription(0, true, new Date(0));
	}

}
