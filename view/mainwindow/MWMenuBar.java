/**
 * 
 */
package view.mainwindow;

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
import view.UserSelectionPanel;
import view.newUser.NewUserDialog;

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
	private JMenu friends;
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
		collection = new JMenu("Connection");
		friends = new JMenu("Friends");
		data = new JMenu("Data");
		help = new JMenu("Help");

		this.add(file);
		this.add(collection);
		this.add(friends);
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
	// TODO add missing descriptions and key bindings
	private void initActions() {
		file.add("Connect").addActionListener(this.listener);
		file.add(new Separator());

		JMenuItem newUser = file.add("New user");
		newUser.addActionListener(this.listener);
		newUser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.ALT_MASK)); // ALT+N

		JMenuItem userList = file.add("User panel");
		userList.addActionListener(this.listener);
		userList.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				ActionEvent.ALT_MASK)); // ALT+M

		collection.add("Publish").addActionListener(this.listener);
		friends.add("List").addActionListener(this.listener);
		data.add("Action").addActionListener(this.listener);
		help.add("About MABIS").addActionListener(this.listener);
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
			if (action.equals("Connect")) {
			} else if (action.equals("Publish")) {
			} else if (action.equals("List")) {
			} else if (action.equals("About MABIS")) {
			} else if (action.equals("Action")) {
			} else if (action.equals("New user")) {
				NewUserDialog nu = new NewUserDialog(mw, true);
				nu.setVisible(true);
			} else if (action.equals("User panel")) {
				UserSelectionPanel nu = new UserSelectionPanel(mw);
				nu.setVisible(true);
			}
			MabisLogger.getLogger().log(Level.INFO, action);
		}
	}

}
