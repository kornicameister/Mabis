package settings;

public class SettingsException extends Exception {
	private static final long serialVersionUID = 2407236843128657165L;

	public SettingsException(final Settings s,String message) {
		super(s.getClass().getSimpleName() + "::" + message);
	}

}
