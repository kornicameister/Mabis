package settings.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.JFrame;

import logger.MabisLogger;

import org.xml.sax.InputSource;

/**
 * Klasa abstrakcyjna, stanowi wyjście dla klas obsługujących zapis i odczyt
 * ustawień. Zainicjalizowane są tutaj podstawowe struktury danych, trzymające
 * konkretne obiekty i używane przez konkretne fukcje. Wspomniane struktury
 * danych są zawsze statyczne aby zapewnić, że dane będą zbierane przez cały
 * czas działania aplikacji i przetworzone dopiero po jej zamknięciu.
 * 
 * @author tomasz
 * 
 */
public abstract class Settings {
	protected static String pathToXML = "settings/mabis.xml";
	protected static TreeSet<JFrame> frames = new TreeSet<>(
			new Comparator<JFrame>() {
				@Override
				public int compare(JFrame f1, JFrame f2) {
					int res = f1.getClass().getName().compareTo(f2.getClass().getName());
					if (res == 0) {
						Double w1 = (double) f1.getWidth();
						Double w2 = (double) f2.getWidth();
						Double h1 = (double) f1.getHeight();
						Double h2 = (double) f2.getHeight();
						res = w1.compareTo(w2);
						if (res == 0) {
							res = h1.compareTo(h2);
						}
					}
					return res;
				}
			});
	protected File xmlFile;
	protected static Integer RUN_COUNT = new Integer(0);
	protected static Boolean RUN_ERROR_FREE = new Boolean(false);

	public Settings() {
		this.xmlFile = new File(Settings.pathToXML);
	}
	public static void addFrame(JFrame f) {
		frames.add(f);
		MabisLogger.getLogger().log(Level.INFO, "{0} frame saved to settings file", f.getClass().getName());
	}

	protected InputSource initInput() throws SettingsException {
		try {
			InputSource s = new InputSource(new InputStreamReader(
					new FileInputStream(this.xmlFile), "UTF-8"));
			s.setEncoding("UTF-8");
			return s;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			throw new SettingsException(this, e.getMessage());
		}
	}

	protected OutputStream initOutput() throws SettingsException {
		try {
			return new FileOutputStream(this.xmlFile);
		} catch (FileNotFoundException e) {
			throw new SettingsException(this, e.getMessage());
		}
	}

	public static void setRunStatus(boolean status){
		Settings.RUN_ERROR_FREE = status;
	}
	
	public static void incrementRunCount() {
		Settings.RUN_COUNT += 1;
	}
	
	public abstract void execute() throws SettingsParseException,
			SettingsException;

	public static String getPathToXML() {
		return pathToXML;
	}

	public static void setPathToXML(String pathToXML) {
		Settings.pathToXML = pathToXML;
	}
}
