/**
 *
 */
package mvc.view.mainwindow;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import logger.MabisLogger;
import mvc.model.entity.User;
import mvc.view.MabisFrameInterface;
import mvc.view.items.creators.AudioAlbumCreator;
import mvc.view.items.creators.BookCreator;
import mvc.view.items.creators.MovieCreator;
import mvc.view.utilities.StatusBar;

/**
 * Skladowy panel {@link MainWindow}. Zostaly na nim umieszczone przyciski
 * pozwalajace na dodawanie nowych elementow kolekcji oraz przycisk pozwalajacy
 * na zakonczenie dzialania z aplikacja
 * 
 * @author tomasz
 * 
 */
public class MWBottomPanel extends JPanel
		implements
			PropertyChangeListener,
			ActionListener,
			MabisFrameInterface {

	private static final long serialVersionUID = 7673272237316575906L;
	private StatusBar statusBar;
	private JPanel actionsPanel;

	private JButton newBookButton;
	private JButton newMovieButton;
	private JButton newAudioAlbumButton;

	private JButton exitButton;
	private final MainWindow mwParent;

	private User connectedUser;

	/**
	 * Creates MWBottomPanel (MainWindowBottomPanel) with MainWindow as it's
	 * parent By parent reference MWBottomPanel has ability to control
	 * MainWindow and therefore application at the whole
	 * 
	 * @param parent
	 */
	public MWBottomPanel(MainWindow parent) {
		super();

		this.mwParent = parent;
		this.setDoubleBuffered(true);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.initComponents();
		this.layoutComponents();
	}

	@Override
	public void initComponents() {
		this.statusBar = new StatusBar();
		this.statusBar.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));

		this.actionsPanel = new JPanel(new FlowLayout());
		this.actionsPanel.setBorder(BorderFactory.createEmptyBorder());

		this.exitButton = new JButton("Exit");
		this.newAudioAlbumButton = new JButton("New audio");
		this.newBookButton = new JButton("New book");
		this.newMovieButton = new JButton("New movie");

		this.actionsPanel.add(this.newAudioAlbumButton);
		this.actionsPanel.add(this.newBookButton);
		this.actionsPanel.add(this.newMovieButton);
		this.actionsPanel.add(new JLabel("|"));
		this.actionsPanel.add(this.exitButton);

		// adding listeners
		this.exitButton.addActionListener(this);
		this.newAudioAlbumButton.addActionListener(this);
		this.newBookButton.addActionListener(this);
		this.newMovieButton.addActionListener(this);
	}

	@Override
	public void layoutComponents() {
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup()
						.addComponent(this.actionsPanel,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
						.addComponent(this.statusBar, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)));

		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup().addGroup(
						layout.createSequentialGroup()
								.addComponent(this.actionsPanel)
								.addComponent(this.statusBar))));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object s = arg0.getSource();
		if (s.equals(exitButton)) {
			mwParent.setVisible(false);
			mwParent.dispose();
			System.exit(0);
		} else if (s.equals(newAudioAlbumButton)) {
			AudioAlbumCreator aac = new AudioAlbumCreator(connectedUser,
					"New album");
			aac.addPropertyChangeListener(this);
			aac.setVisible(true);
		} else if (s.equals(newBookButton)) {
			BookCreator aac = new BookCreator(connectedUser, "New book");
			aac.addPropertyChangeListener(this);
			aac.setVisible(true);
		} else if (s.equals(newMovieButton)) {
			MovieCreator aac = new MovieCreator(connectedUser, "New movie");
			aac.addPropertyChangeListener(this);
			aac.setVisible(true);
		}
		MabisLogger.getLogger().log(Level.INFO,
				((JButton) s).getActionCommand());
	}

	public StatusBar getStatusBar() {
		return this.statusBar;
	}

	/**
	 * Metoda ustawia użytkownika, który właśnie połączył się z bazą danych, a
	 * który jest delegowany do kreatorów obiektów kolekcji.
	 * 
	 * @param us
	 */
	public void setConnectedUser(User us) {
		this.connectedUser = us;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("itemAffected")) {
			this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(),
					evt.getNewValue());
		}
	}
}
