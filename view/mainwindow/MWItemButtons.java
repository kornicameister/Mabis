/**
 * 
 */
package view.mainwindow;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * Klase {@link MWItemButtons} dziedziczy z JPanel i jest agregatem gdzie
 * zlokalizowana jest lista użytkowników w formie tabeli. Użytkownicy pobierani
 * są z bazy danych zlokalizowanych online !
 * 
 * @author kornicameister
 * 
 */
public class MWItemButtons extends JPanel {
	private static final long serialVersionUID = -4998076655395481258L;
	private JButton newBookButton;
	private JButton newMovieButton;
	private JButton newAudioAlbumButton;

	public MWItemButtons() {
		super(true);
		this.setLayout(new FlowLayout());
		this.initComponents();
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Item fun ;-)"));
		this.newBookButton = new JButton("New book");
		this.newMovieButton = new JButton("New movie");
		this.newAudioAlbumButton = new JButton("New audio");
		
		
		//layout
		this.add(this.newAudioAlbumButton);
		this.add(this.newBookButton);
		this.add(this.newMovieButton);
	}
}
