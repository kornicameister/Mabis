/**
 * package mabis.mvc.view in MABIS
 * by kornicameister
 */
package mvc.view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
		setSize(650, 500);
		this.initComponents();
		this.layoutComponents();
	}

	@Override
	public void initComponents() {
		this.tabManager = new JTabbedPane();
		this.tabManager.addTab("About", this.aboutTab());
		this.tabManager.addTab("Author", this.authorTab());
		this.tabManager.addTab("License", this.licenseTab());
		this.setResizable(false);
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
		class AboutMabisPanel extends JPanel implements MabisFrameInterface {
			private static final long serialVersionUID = 2179636719860933267L;
			private JLabel mabisIcon;
			private JTextArea mabisShortDesc;
			private JTextArea mabisApiDesc;
			private JTextArea mabisMVCDesc;
			private JScrollPane scrollPanes[] = new JScrollPane[3];
			private JLabel apiIcons[] = new JLabel[3];

			public AboutMabisPanel() {
				this.initComponents();
				this.layoutComponents();
			}

			@Override
			public void initComponents() {
				this.mabisIcon = new JLabel(new ImageIcon(
						GlobalPaths.MABIS_ICON.toString()));
				this.mabisShortDesc = new JTextArea(
						loadText(GlobalPaths.MABIS_WHY.toString()));
				this.mabisShortDesc.setLineWrap(true);
				this.mabisShortDesc.setWrapStyleWord(true);
				this.mabisShortDesc.setEditable(false);
				this.scrollPanes[0] = new JScrollPane(this.mabisShortDesc);
				this.scrollPanes[0].setBorder(BorderFactory.createTitledBorder("Czemu ?"));

				this.mabisApiDesc = new JTextArea(
						loadText(GlobalPaths.MABIS_API.toString()));
				this.mabisApiDesc.setLineWrap(true);
				this.mabisApiDesc.setWrapStyleWord(true);
				this.mabisApiDesc.setEditable(false);
				this.scrollPanes[1] = new JScrollPane(this.mabisApiDesc);
				this.scrollPanes[1].setBorder(BorderFactory.createTitledBorder("API !"));

				this.mabisMVCDesc = new JTextArea(
						loadText(GlobalPaths.MABIS_MVC.toString()));
				this.mabisMVCDesc.setLineWrap(true);
				this.mabisMVCDesc.setWrapStyleWord(true);
				this.mabisMVCDesc.setEditable(false);
				this.scrollPanes[2] = new JScrollPane(this.mabisMVCDesc);
				this.scrollPanes[2].setBorder(BorderFactory.createTitledBorder("MVC !"));
				
				
				this.apiIcons[0] = new JLabel(new ImageIcon(new ImageIcon(
						GlobalPaths.LASTFM_LOGO.toString()).getImage().getScaledInstance(128, 40, Image.SCALE_FAST)));
				this.apiIcons[1] = new JLabel(new ImageIcon(new ImageIcon(
						GlobalPaths.IMDB_LOGO.toString()).getImage().getScaledInstance(128, 40, Image.SCALE_FAST)));
				this.apiIcons[2] = new JLabel(new ImageIcon(new ImageIcon(
						GlobalPaths.GOOGLE_BOOKS_LOGO.toString()).getImage().getScaledInstance(128, 40, Image.SCALE_FAST)));
			}

			@Override
			public void layoutComponents() {
				GroupLayout gl = new GroupLayout(this);
				this.setLayout(gl);

				gl.setAutoCreateContainerGaps(true);
				gl.setAutoCreateGaps(true);

				gl.setHorizontalGroup(
						gl.createParallelGroup()
						.addGroup(gl.createSequentialGroup()
								.addComponent(this.mabisIcon, 128, 128, 128)
								.addComponent(this.scrollPanes[0])
						)
						.addGroup(gl.createSequentialGroup()
								.addGroup(gl.createParallelGroup()
										.addComponent(this.apiIcons[0],128,128,128)
										.addComponent(this.apiIcons[1],128,128,128)
										.addComponent(this.apiIcons[2],128,128,128)
										)
								.addComponent(this.scrollPanes[1])
						)
						.addGroup(gl.createSequentialGroup()
								.addComponent(this.scrollPanes[2])
						)
				);
				
				gl.setVerticalGroup(
						gl.createSequentialGroup()
						.addGroup(gl.createParallelGroup()
								.addComponent(this.mabisIcon, 128, 128, 128)
								.addComponent(this.scrollPanes[0])
						)
						.addGroup(gl.createParallelGroup()
								.addGroup(gl.createSequentialGroup()
										.addGap(20)
										.addComponent(this.apiIcons[0],40,40,40)
										.addComponent(this.apiIcons[1],40,40,40)
										.addComponent(this.apiIcons[2],40,40,40)
										)
								.addComponent(this.scrollPanes[1])
						)
						.addGroup(gl.createParallelGroup()
								.addComponent(this.scrollPanes[2])
						)
				);
			}
		}
		return new JScrollPane(new AboutMabisPanel());
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
}
