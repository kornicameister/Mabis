/**
 * package mabis.mvc.view in MABIS
 * by kornicameister
 */
package mvc.view;

import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import settings.GlobalPaths;

/**
 * Klasa pozwalaja na podglad informacji o aplikacji. Niezaprzeczalnym atutem
 * jest możliwość zmiany tekstu wyswietlanego przez okienko w zewnetrznym pliku
 * html
 * 
 * @author kornicameister
 * @see GlobalPaths#ABOUT_MABIS_HTML
 */
public class AboutMabis extends JFrame implements MabisFrameInterface {
	private static final long serialVersionUID = 6992601172376070322L;
	private JTabbedPane tabManager;

	public AboutMabis() {
		super("About");
		setSize(620, 500);
//		this.kit = new HTMLEditorKit();
//		this.aboutCSS();
		this.initComponents();
		this.layoutComponents();
	}

	@Override
	public void initComponents() {
		this.tabManager = new JTabbedPane();
		this.tabManager.addTab("About", this.aboutTab());
		this.tabManager.addTab("Author", this.authorTab());
		this.tabManager.addTab("License", this.licenseTab());
	}

	private JScrollPane licenseTab() {
		JTextArea license = new JTextArea(this.loadText(GlobalPaths.LICENSE
				.toString()));
		return new JScrollPane(license);
	}

	private JScrollPane authorTab() {
		URI file;
		try {
			file = new File(GlobalPaths.ABOUT_AUTHOR_HTML.toString()).toURI();
			JEditorPane description = new JEditorPane(file.toURL());
			description.setEditable(false);
			return new JScrollPane(description);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private JScrollPane aboutTab() {
		URI file;
		try {
			file = new File(GlobalPaths.ABOUT_MABIS_HTML.toString()).toURI();
			JEditorPane description = new JEditorPane(file.toURL());
			description.setEditable(false);
			return new JScrollPane(description);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String loadText(String path) {
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(path));
			byte[] contents = new byte[1024];
			StringBuilder sb = new StringBuilder();
			while ((bis.read(contents)) != -1) {
				sb.append(new String(contents));
			}
			bis.close();
			path = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	@Override
	public void layoutComponents() {
		this.setLayout(new BorderLayout());
		this.add(this.tabManager, BorderLayout.CENTER);
	}

//	private StyleSheet aboutCSS() {
//		StyleSheet styleSheet = this.kit.getStyleSheet();
//		styleSheet
//				.addRule("body {color:grey; font-family:times; margin: 4px; }");
//		styleSheet.addRule("h1 {color: blue;}");
//		styleSheet.addRule("h2 {color: #ff0000;}");
//		styleSheet.addRule("p.content {text-align:center}");
//		styleSheet.addRule("b {font-size: 15px; color: blue;}");
//		return styleSheet;
//	}
}
