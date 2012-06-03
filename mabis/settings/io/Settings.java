package settings.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JFrame;

import org.xml.sax.InputSource;

import settings.SettingDataType;


public abstract class Settings {
	protected static String pathToXML = "settings/mabis.xml";
	protected static TreeMap<SettingDataType, Object> data = new TreeMap<>();
	protected static TreeSet<JFrame> frames = new TreeSet<>(new Comparator<JFrame>() {
		@Override
		public int compare(JFrame f1,JFrame f2) {
			int res = f1.getTitle().compareTo(f2.getTitle());
			if(res == 0){
				Double w1 = (double) f1.getWidth();
				Double w2 = (double) f2.getWidth();
				Double h1 = (double) f1.getHeight();
				Double h2 = (double) f2.getHeight();
				res = w1.compareTo(w2);
				if(res == 0){
					res = h1.compareTo(h2);
				}
			}
			return res;
		}
	});
	protected File xmlFile;

	public Settings() {
		this.xmlFile = new File(Settings.pathToXML);
	}
	
	public static void addData(SettingDataType sdt, Object o){
		data.put(sdt,o);
	}
	
	public static void addFrame(JFrame f) {
		frames.add(f);
		System.out.println(frames.contains(f));
	}
	
	protected InputSource initInput() throws SettingsException{
		try{
			InputSource s = new InputSource(
					new InputStreamReader(
							new FileInputStream(this.xmlFile),
							"UTF-8"));
			s.setEncoding("UTF-8");
			return s;
		} catch (FileNotFoundException | UnsupportedEncodingException e){
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
	
	public abstract void execute() throws SettingsParseException, SettingsException;
	
	public static TreeMap<SettingDataType, Object> getData() {
		return data;
	}
	
	public static String getPathToXML() {
		return pathToXML;
	}

	public static void setPathToXML(String pathToXML) {
		Settings.pathToXML = pathToXML;
	}
}
