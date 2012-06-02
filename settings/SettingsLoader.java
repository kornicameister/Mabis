package settings;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SettingsLoader extends Settings {
	private SAXParser saxParser;

	public SettingsLoader() throws SettingsException {
		super();
		try {
			this.saxParser = SAXParserFactory.newInstance().newSAXParser();
		} catch (ParserConfigurationException | SAXException e) {
			throw new SettingsException(this, e.getMessage());
		}
	}

	private DefaultHandler initHandler() {
		return new DefaultHandler(){
			@Override
			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
			}
			
			@Override
			public void endElement(String uri, String localName, String qName)
					throws SAXException {
			}
			
			@Override
			public void characters(char[] ch, int start, int length)
					throws SAXException {
			}
		};
	}

	@Override
	public void execute() throws SettingsParseException {
		try {
			this.saxParser.parse(new File(Settings.pathToXML), this.initHandler());
		} catch (SAXException | IOException e) {
			throw new SettingsParseException(this, e.getMessage());
		}
	}

}
