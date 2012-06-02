package settings;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import view.mainwindow.MainWindow;

public class SettingsLoader extends Settings {

	@Override
	public void execute() {
		return;
	}
	
	public static TreeMap<String, String> loadPaths() throws SettingsException{
		TreeMap<String, String> tmp = new TreeMap<>();
		try {
			Element root = ((Document) new SAXBuilder().build(new File(SettingsLoader.pathToXML))).getRootElement();
			
			List<?> paths = root.getChildren("paths");
			
			for(short i = 0 ; i < paths.size() ; i++){
				Element node = (Element) paths.get(i);
				tmp.put(node.getAttributeValue("path"), node.getChildText("path"));
			}
		} catch (JDOMException | IOException e) {
			throw new SettingsException(e.getMessage());
		}
		return tmp;
	}
	
	public static void loadMainWindow(MainWindow mw) throws SettingsException{
		try {
			Element root = ((Document) new SAXBuilder().build(new File(SettingsLoader.pathToXML))).getRootElement();
			
			List<?> paths = root.getChildren("main_window");
			
			for(short i = 0 ; i < paths.size() ; i++){
				Element node = (Element) paths.get(i);
				mw.setTitle(node.getChildText("title"));
				mw.setSize(
						Integer.valueOf(node.getChildText("width")),
						Integer.valueOf(node.getChildText("height")));
				mw.setLocation(
						Integer.valueOf(node.getChildText("xPos")),
						Integer.valueOf(node.getChildText("yPos")));
			}
		} catch (JDOMException | IOException e) {
			throw new SettingsException(e.getMessage());
		}
	}
	
}
