package settings.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.JFrame;

import logger.MabisLogger;
import mvc.view.WindowClosedListener;

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
	/**
	 * {@link TreeSet} ramek, inicjalizowany z custom comparatorem ktory sortuje
	 * ramki po nazwie klasy, rozmiarze oraz pozycji. Dodatkowym skutkiem,
	 * pozadanym w tym wypadku, jest zapewnienie ze {@link Settings#frames} nie
	 * bedzie zawieralo duplikatow.
	 */
	protected static TreeSet<JFrame> frames = new TreeSet<>(
			new Comparator<JFrame>() {
				@Override
				public int compare(JFrame f1, JFrame f2) {
					int res = f1.getClass().getName()
							.compareTo(f2.getClass().getName());
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
	protected static Integer RUN_COUNT = new Integer(0);
	protected static Boolean RUN_ERROR_FREE = new Boolean(false);

	/**
	 * Statyczna metoda dostepowa wywolywane z {@link WindowClosedListener}.
	 * Pozwala na dodawania do {@link Settings} kolejnych referencji okienek.
	 * Wykonanie takiej operacji jest rownoznaczne z faktem, ze meta-dane o
	 * danej ramce zostana umieszczone w pliku ustawien
	 * 
	 * @param f
	 *            referencja do ramki
	 * @see WindowClosedListener
	 */
	public static void addFrame(JFrame f) {
		frames.add(f);
		MabisLogger.getLogger().log(Level.INFO,
				"{0} frame saved to settings file", f.getClass().getName());
	}

	/**
	 * Metoda odwoluje sie do pliku XML, ktory zawiera ustawienie aplikacji i
	 * inicjalizuje {@link OutputStream} wymagany, aby moc pisac do pliku
	 * okreslonego przez {@link Settings#pathToXML}
	 * 
	 * @return referencje do strumienia wyjsciowego
	 * @throws SettingsException
	 */
	protected OutputStream initOutput() throws SettingsException {
		try {
			return new FileOutputStream(new File(Settings.pathToXML));
		} catch (FileNotFoundException e) {
			throw new SettingsException(this, e.getMessage());
		}
	}

	public static void setRunStatus(boolean status) {
		Settings.RUN_ERROR_FREE = status;
	}

	public static void incrementRunCount() {
		Settings.RUN_COUNT += 1;
	}

	/**
	 * Abstrakcyjna metoda redefiniowana jedynie w {@link SettingsSaver}. Sluzy
	 * wykonaniu zadania danej klasy - zaladowaniu/zapisaniu ustawien
	 * 
	 * @throws SettingsParseException
	 * @throws SettingsException
	 */
	public abstract void execute() throws SettingsParseException,
			SettingsException;

	public static String getPathToXML() {
		return pathToXML;
	}

	public static void setPathToXML(String pathToXML) {
		Settings.pathToXML = pathToXML;
	}
}
