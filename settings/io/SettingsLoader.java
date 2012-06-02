package settings.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
	
	@SuppressWarnings("unchecked")
	public static String loadPath(String pathName) throws SettingsException{
		try {
			Element root = ((Document) new SAXBuilder().build(new File(SettingsLoader.pathToXML))).getRootElement();
			
			List<Element> paths = root.getChildren("paths");
			paths = ((Element)paths.get(0)).getChildren();

			for(Element e : paths){
				if(e.getAttribute("type").getValue().equals(pathName)){
					return e.getText();
				}
			}
		} catch (JDOMException | IOException e) {
			throw new SettingsException(e.getMessage());
		}
		return "";
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
