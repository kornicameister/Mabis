package settings.io;

/**
 * Wyjatek rzucany zawsze wtedy jesli pojawi sie blad przy
 * parsowaniu pliku z ustawieniami.
 * @author tomasz
 *
 */
public class SettingsParseException extends Exception {
	private static final long serialVersionUID = 7793612894551543259L;

	public SettingsParseException(Settings s, String message) {
		super(s.getClass().getSimpleName() + "::" + message);
	}
}
