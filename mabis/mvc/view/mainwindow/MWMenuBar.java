/**
 * 
 */
package mvc.view.mainwindow;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu.Separator;
import javax.swing.KeyStroke;

import logger.MabisLogger;
import mvc.view.AboutMabis;
import mvc.view.UserSelectionPanel;
import mvc.view.newUser.NewUserDialog;
import mvc.view.settings.SettingsExplorer;

/**
 * Class nicely and logically group the functionality related to menu bar that
 * {@link MainWindow} should consist
 * 
 * @author kornicameister
 */
public class MWMenuBar extends JMenuBar {
	private static final long serialVersionUID = 3381375656282403270L;
	private JMenu file;
	private JMenu collection;
	private JMenu data;
	private JMenu help;
	private MainWindowMenuBarListener listener;
	/**
	 * this is a reference to main window allowing {@link MWMenuBar} to pass
	 * actions againt class to mainWindow
	 */
	private MainWindow mw;

	public MWMenuBar(MainWindow parent) throws HeadlessException {
		super();// file menu

		this.mw = parent;
		this.setDoubleBuffered(true);
		this.setOpaque(true);

		this.listener = new MainWindowMenuBarListener();

		initItems();
		initActions();
	}

	/**
	 * Method goes through each JMenu and initializes it by
	 * <ul>
	 * <li>creating new reference</li>
	 * </ul>
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
	 * Method goes through each JMenuItem and initializes it by
	 * <ul>
	 * <li>adding description</li>
	 * <li>adding key binding</li>
	 * <li>adding listeners</li>
	 * </ul>
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
		JMenuItem about = help.add("About");
		about.addActionListener(this.listener);

		JMenuItem settings = help.add("Settings");
		settings.addActionListener(this.listener);
		settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
				ActionEvent.CTRL_MASK)); // ALT+N
	}

	private void initCollectionMenu() {
		JMenuItem newBook = collection.add("New book");
		newBook.addActionListener(this.listener);
		newBook.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
				ActionEvent.CTRL_MASK)); // CTRL+B
		JMenuItem newMovie = collection.add("New movie");
		newMovie.addActionListener(this.listener);
		newMovie.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				ActionEvent.CTRL_MASK)); // CTRL+M
		JMenuItem newAudio = collection.add("New audio album");
		newAudio.addActionListener(this.listener);
		newAudio.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK)); // CTRL+M
	}

	/**
	 * Metoda operuje jedynie na menu <b>File</b>. Dodając do niego kolejne
	 * pozycje oraz odpowiednio je inicjalizując
	 */
	private void initFileMenu() {
		JMenuItem newUser = file.add("New user");
		newUser.addActionListener(this.listener);
		newUser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.ALT_MASK)); // ALT+N

		JMenuItem userList = file.add("User panel");
		userList.addActionListener(this.listener);
		userList.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				ActionEvent.ALT_MASK)); // ALT+M

		file.add(new Separator());

		JMenuItem exit = file.add("Exit");
		exit.addActionListener(this.listener);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.ALT_MASK)); // ALT+M
	}

	/**
	 * Listener created especially for MWMenuBar communicates with main window
	 * by reference
	 * 
	 * @author kornicameister
	 */
	class MainWindowMenuBarListener implements ActionListener {
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
				
			} else if (action.equals("New book")) {

			} else if (action.equals("New movie")) {

			} else if (action.equals("Settings")) {
				SettingsExplorer se = new SettingsExplorer();
				se.setVisible(true);
			} else if (action.equals("About")) {
				AboutMabis am = new AboutMabis();
				am.setVisible(true);
			}
		}
	}

}
