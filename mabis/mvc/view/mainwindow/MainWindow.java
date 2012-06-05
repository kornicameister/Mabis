/**
 * package mabis.mvc.view in MABIS by kornicameister
 */
package mvc.view.mainwindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import mvc.controller.SQLStamentType;
import mvc.controller.database.MySQLAccess;
import mvc.controller.entity.UserSQLFactory;
import mvc.controller.exceptions.SQLEntityExistsException;
import mvc.model.entity.User;
import mvc.view.UserSelectionPanel;
import mvc.view.WindowClosedListener;
import mvc.view.newUser.NewUserDialog;

/**
 * This is the main window class that presented to the user in the beginning and
 * works as singleton aggregating most of actions and passing them to external
 * listeners
 * 
 * @author kornicameister
 * @version 0.1
 * @see java.swing.JFrame
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = -8447166696627624367L;
	private MySQLAccess mysql = null;
	private User connectedUser = null;
	private MWToolBar toolBar;
	private MWBottomPanel bottomPanel;
	private MWCollectionView collectionView;
	private JPanel contentPane = null;
	private MWMenuBar menuBar = null;

	/**
	 * Constructor of the main windows, calls for all private method to
	 * initialize the end user mabis.mvc.view
	 * 
	 * @param title
	 *            title of the window
	 * @param d
	 *            dimension of the window
	 * @see Dimension
	 */
	public MainWindow(String title) {
		super(title);

		this.menuBar = new MWMenuBar(this);
		this.setJMenuBar(this.menuBar);
		this.bottomPanel = new MWBottomPanel(this);
		this.collectionView = new MWCollectionView(this, new BorderLayout(),
				true);
		this.toolBar = new MWToolBar("Mabis toolbar", JToolBar.HORIZONTAL);

		layoutComponents();

		this.toolBar.setEnabled(false);
		this.collectionView.setEnabled(false);
		this.bottomPanel.setEnabled(false);

		this.toolBar.addPropertyChangeListener(this.collectionView);
		this.addPropertyChangeListener("connectedUser", this.collectionView);
		this.bottomPanel.addPropertyChangeListener(this.collectionView);
		this.menuBar.addPropertyChangeListener(this.collectionView);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.initConnection();
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				checkForUsers();
			}
		});
		this.addWindowListener(new WindowClosedListener());
	}

	private void checkForUsers() {
		// check for any user, if none print NewUserDialog
		UserSQLFactory f = new UserSQLFactory(SQLStamentType.SELECT, new User());
		try {
			f.executeSQL(true);
		} catch (SQLException | SQLEntityExistsException e) {
			e.printStackTrace();
		}
		if (!f.getUsers().isEmpty()) {
			UserSelectionPanel usp = new UserSelectionPanel(f.getUsers(), this);
			usp.setVisible(true);
			usp.setAlwaysOnTop(true);
			usp = null;
		} else {
			int retVal = JOptionPane.showConfirmDialog(this,
					"No users found\nWould like to create new user ?");
			if (retVal == JOptionPane.OK_OPTION) {
				NewUserDialog newUser = new NewUserDialog(this, true);
				newUser.setVisible(true);
			}
		}
		System.gc();
	}

	private void initConnection() {
		this.mysql = new MySQLAccess();
		try {
			if (this.mysql.isConnected()) {
				this.bottomPanel.getStatusBar().setMessage(
						"Connected to Mabis DB");
			} else {
				this.bottomPanel.getStatusBar().setMessage(
						"Connection not established");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method is taking care of initializing window content, that is:
	 * <ul>
	 * <li>collection mabis.mvc.view</li>
	 * <li>friends list</li>
	 * <li>friends interaction block</li>
	 * <li>buttons</li>
	 * </ul>
	 * and placing it on layouts
	 */
	private void layoutComponents() {
		// adding content jpanel
		this.contentPane = new JPanel(true);
		this.contentPane.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));

		this.setContentPane(this.contentPane);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		this.bottomPanel.setBorder(BorderFactory.createEtchedBorder());

		layout.setHorizontalGroup(layout
				.createParallelGroup(Alignment.CENTER)
				.addComponent(this.toolBar, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(
						layout.createSequentialGroup().addComponent(
								this.collectionView,
								GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addComponent(this.bottomPanel, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addComponent(this.toolBar, GroupLayout.PREFERRED_SIZE, 40,
						GroupLayout.PREFERRED_SIZE)
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(this.collectionView))
				.addComponent(this.bottomPanel, GroupLayout.PREFERRED_SIZE,
						GroupLayout.DEFAULT_SIZE, 50));

		this.repaint();
		this.pack();
	}

	public MWBottomPanel getBottomPanel() {
		return this.bottomPanel;
	}

	public void setConnectedUser(User newUser) {
		if (this.connectedUser != null
				&& this.connectedUser.getLogin().equals(newUser.getLogin())) {
			return;
		}
		User oldUser = this.getConnectedUser();
		this.connectedUser = newUser;
		this.firePropertyChange("connectedUser", oldUser, this.connectedUser);
		this.bottomPanel.setConnectedUser(this.connectedUser);
		this.menuBar.setConnectedUser(this.connectedUser);
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					collectionView.loadCollection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		this.toolBar.setEnabled(true);
		this.collectionView.setEnabled(true);
		this.bottomPanel.setEnabled(true);
	}

	public User getConnectedUser() {
		return this.connectedUser;
	}
}