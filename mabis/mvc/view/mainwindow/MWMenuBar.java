/**
 * 
 */
package mvc.view.mainwindow;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu.Separator;
import javax.swing.KeyStroke;

import logger.MabisLogger;
import mvc.model.entity.User;
import mvc.view.AboutMabis;
import mvc.view.items.creators.AudioAlbumCreator;
import mvc.view.items.creators.BookCreator;
import mvc.view.items.creators.MovieCreator;
import mvc.view.settings.SettingsExplorer;
import mvc.view.user.NewUserDialog;
import mvc.view.user.UserSelectionPanel;
import settings.io.LastRunDescription;
import settings.io.SettingsLoader;

/**
 * Klasa grupuje funkcjonalnosc paska menu, ktore kazde glowne okno powinno
 * zawierac
 * 
 * @author kornicameister
 */
public class MWMenuBar extends JMenuBar
		implements
			PropertyChangeListener,
			ActionListener {
	private static final long serialVersionUID = 3381375656282403270L;
	private JMenu file;
	private JMenu collection;
	private JMenu data;
	private JMenu help;
	private MainWindow mw;
	private User connectedUser;
	private final static LastRunDescription LSR = SettingsLoader.loadLastRun();

	public MWMenuBar(MainWindow parent) throws HeadlessException {
		super();

		this.mw = parent;
		this.setDoubleBuffered(true);
		this.setOpaque(true);

		initItems();
		initActions();
	}

	/**
	 * Metoda inicjalizuje obiekty, ktore sa widoczne dla dla uzytkownika
	 */
	private void initItems() {
		file = new JMenu("File");
		collection = new JMenu("Collection");
		data = new JMenu("Data");
		help = new JMenu("Help");

		this.add(file);
		this.add(collection);
		this.add(data);
		this.add(help);
	}

	/**
	 * Metoda-opakowanie, jej zadaniem jest po prostu wywolanie kolejnych metod
	 * tej klasy, ktore zainicjalizuje {@link JMenuItem} dla kolejnych
	 * {@link JMenu}
	 */
	private void initActions() {
		this.initFileMenu();
		this.initCollectionMenu();
		this.initDataMenu();
		this.initHelpMenu();
	}

	private void initDataMenu() {

	}

	private void initHelpMenu() {
		JMenuItem settings = help.add("Settings");
		settings.addActionListener(this);
		settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
				ActionEvent.CTRL_MASK)); // ALT+N

		JMenuItem runDesc = help.add("Last run");
		runDesc.addActionListener(this);

		JMenuItem about = help.add("About");
		about.addActionListener(this);
	}

	private void initCollectionMenu() {
		JMenuItem newBook = collection.add("New book");
		newBook.addActionListener(this);
		newBook.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
				ActionEvent.CTRL_MASK)); // CTRL+B
		JMenuItem newMovie = collection.add("New movie");
		newMovie.addActionListener(this);
		newMovie.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				ActionEvent.CTRL_MASK)); // CTRL+M
		JMenuItem newAudio = collection.add("New audio album");
		newAudio.addActionListener(this);
		newAudio.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK)); // CTRL+M
	}

	/**
	 * Metoda operuje jedynie na menu <b>File</b>. Dodając do niego kolejne
	 * pozycje oraz odpowiednio je inicjalizując
	 */
	private void initFileMenu() {
		JMenuItem newUser = file.add("New user");
		newUser.addActionListener(this);
		newUser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.ALT_MASK)); // ALT+N

		JMenuItem userList = file.add("User panel");
		userList.addActionListener(this);
		userList.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				ActionEvent.ALT_MASK)); // ALT+M

		file.add(new Separator());

		JMenuItem exit = file.add("Exit");
		exit.addActionListener(this);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.ALT_MASK)); // ALT+M
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = ((JMenuItem) e.getSource()).getActionCommand();
		MabisLogger.getLogger().log(Level.INFO, action);
		if (action.equals("New user")) {
			NewUserDialog nu = new NewUserDialog(mw, true);
			nu.setVisible(true);
		} else if (action.equals("User panel")) {
			UserSelectionPanel nu = new UserSelectionPanel(mw);
			nu.setVisible(true);
		} else if (action.equals("Exit")) {
			System.exit(0);
		} else if (action.equals("New audio album")) {
			AudioAlbumCreator aac = new AudioAlbumCreator(connectedUser,
					"New album");
			aac.addPropertyChangeListener(this);
			aac.setVisible(true);
		} else if (action.equals("New book")) {
			BookCreator aac = new BookCreator(connectedUser, "New book");
			aac.addPropertyChangeListener(this);
			aac.setVisible(true);
		} else if (action.equals("New movie")) {
			MovieCreator aac = new MovieCreator(connectedUser, "New movie");
			aac.addPropertyChangeListener(this);
			aac.setVisible(true);
		} else if (action.equals("Settings")) {
			SettingsExplorer se = new SettingsExplorer();
			se.setVisible(true);
		} else if (action.equals("About")) {
			AboutMabis am = new AboutMabis();
			am.setVisible(true);
		} else if (action.equals("Last run")) {
			JOptionPane.showMessageDialog(null, LSR.toString(), "Last run",
					JOptionPane.INFORMATION_MESSAGE, null);
		}
	}

	/**
	 * Prosta metoda, wywolany zawsze z glownego okna, w momencie kiedy zostanie
	 * ustawiony nowy uzytkownik (tj. kiedy uzytkownik zostanie podlaczony do
	 * programu)
	 * 
	 * @param connectedUser
	 *            polaczony uzytkownik
	 */
	public void setConnectedUser(User connectedUser) {
		this.connectedUser = connectedUser;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("itemAffected")) {
			this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(),
					evt.getNewValue());
		}
	}

}
