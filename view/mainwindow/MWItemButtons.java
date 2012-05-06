/**
 * 
 */
package view.mainwindow;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import view.items.BookCreator;

import logger.MabisLogger;

/**
 * Klase {@link MWItemButtons} dziedziczy z JPanel i jest agregatem gdzie
 * zlokalizowane są przyciski do interakcji z kolekcją
 * 
 * @author kornicameister
 * 
 */
public class MWItemButtons extends JPanel {
	private static final long serialVersionUID = -4998076655395481258L;
	private JButton newBookButton;
	private JButton newMovieButton;
	private JButton newAudioAlbumButton;
	private JButton scanForMoviesButton;
	private MWItemButtonsListener listener;

	public MWItemButtons() {
		super(true);
		this.setLayout(new GridLayout(0, 1, 10, 10));
		this.listener = new MWItemButtonsListener();
		this.initComponents();
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Item fun ;-)"));
		this.newBookButton = new JButton("New book");
		this.newBookButton.addActionListener(this.listener);
		this.newMovieButton = new JButton("New movie");
		this.newMovieButton.addActionListener(this.listener);
		this.scanForMoviesButton = new JButton("Search movies");
		this.scanForMoviesButton.addActionListener(this.listener);
		this.newAudioAlbumButton = new JButton("New audio");
		this.newAudioAlbumButton.addActionListener(this.listener);

		// layout
		this.add(this.newAudioAlbumButton);
		this.add(this.newBookButton);
		this.add(this.newMovieButton);
		this.add(this.scanForMoviesButton);
	}

	/**
	 * Listener dla panelu {@link MWItemButtons}
	 * 
	 * @author kornicameister
	 * @see 0.1
	 */
	class MWItemButtonsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent b) {
			JButton source = (JButton) b.getSource();
			if (source.equals(newBookButton)) {
				BookCreator bc = new BookCreator("New book");
				bc.setVisible(true);
			} else if (source.equals(newAudioAlbumButton)) {

			} else if (source.equals(newMovieButton)) {

			} else if (source.equals(scanForMoviesButton)) {

			}
			MabisLogger.getLogger().log(Level.INFO, "Action called ::  {0}",
					source.getActionCommand());
		}

	}
}
