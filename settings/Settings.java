package settings;

import java.util.TreeMap;

public abstract class Settings {
	protected static String pathToXML = "./settings/mabis.xml";
	protected TreeMap<String, Object> loadedValues;

	public Settings() {
		this.loadedValues = new TreeMap<>();
	}
	
	public abstract void execute() throws SettingsParseException;
	
	public static String getPathToXML() {
		return pathToXML;
	}

	public static void setPathToXML(String pathToXML) {
		Settings.pathToXML = pathToXML;
	}
}
