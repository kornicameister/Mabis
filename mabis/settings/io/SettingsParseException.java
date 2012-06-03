package settings.io;


public class SettingsParseException extends Exception{
	private static final long serialVersionUID = 7793612894551543259L;

	public SettingsParseException(Settings s, String message) {
		super(s.getClass().getSimpleName() + "::" + message);
	}
}
