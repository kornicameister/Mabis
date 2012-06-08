/**
 * package mabis.mvc.view in MABIS
 * by kornicameister
 */
package mvc.view;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import settings.GlobalPaths;

/**
 * Klasa pozwalaja na podglad informacji o aplikacji.
 * Niezaprzeczalnym atutem jest możliwość zmiany tekstu wyswietlanego
 * przez okienko w zewnetrznym pliku html
 * 
 * @author kornicameister
 * @see GlobalPaths#ABOUT_MABIS_HTML
 */
public class AboutMabis extends JFrame implements MabisFrameInterface {
	private static final long serialVersionUID = 6992601172376070322L;
	private JEditorPane description;

	public AboutMabis() {
		super("About");
		setSize(500, 500);
		this.initComponents();
		this.layoutComponents();
	}

	@Override
	public void initComponents() {
		try {
			URI tmp = new File(GlobalPaths.ABOUT_MABIS_HTML.toString()).toURI();
			URL aboutFile = tmp.toURL();
			this.description = new JEditorPane(aboutFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void layoutComponents() {
		this.add(new JScrollPane(this.description));
	}
}
