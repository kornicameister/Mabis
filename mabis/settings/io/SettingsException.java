package settings.io;

/**
 * Wyjatek ogolny.</br> Rzucany zawsze wtedy kiedy pojawi sie blad, inny niz
 * {@link SettingsParseException}, podczas przetwarzania pliku z ustawieniami.
 * 
 * @author tomasz
 * 
 */
public class SettingsException extends Exception {
	private static final long serialVersionUID = 2407236843128657165L;

	public SettingsException(final Settings s, String message) {
		super(s.getClass().getSimpleName() + "::" + message);
	}

	public SettingsException(String message) {
		super(message);
	}

}
