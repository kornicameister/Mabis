package settings.io;

import java.io.IOException;

import javax.swing.JFrame;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import settings.GlobalPaths;

public class SettingsSaver extends Settings {

	public SettingsSaver() {
		super();
	}

	@Override
	public void execute() {
		try {
			Document doc = new Document(new Element("mabis"));
			
			doc.getRootElement().addContent(this.saveWindowPosition());
			doc.getRootElement().addContent(this.saveGlobalPaths());
			
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			outputter.output(doc, this.initOutput());
		} catch (IOException | SettingsException e) {
			e.printStackTrace();
		}
	}

	private Element saveWindowPosition() {
		Element frames = new Element("frames");
		for(JFrame f : SettingsSaver.frames){
			Element window = new Element(f.getClass().getSimpleName().replaceAll(" ","_"));
			window.addContent(new Element("title").setText(f.getTitle()));
			window.addContent(new Element("width").setText(String.valueOf(f.getWidth())));
			window.addContent(new Element("height").setText(String.valueOf(f.getHeight())));
			window.addContent(new Element("xPos").setText(String.valueOf(f.getX())));
			window.addContent(new Element("yPos").setText(String.valueOf(f.getY())));
			frames.addContent(window);
		}
		return frames;
	}

	private Element saveGlobalPaths() {
		Element e = new Element("paths");
		for(GlobalPaths p : GlobalPaths.values()){
			Element pp = new Element("path");
			pp.setAttribute(new Attribute("type", p.name()));
			pp.setText(p.toString());
			e.addContent(pp);
		}
		return e;
	}
}
