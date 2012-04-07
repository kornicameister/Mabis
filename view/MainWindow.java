/**
 * package view in MABIS
 * by kornicameister
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MenuBar;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import view.MainWindow.MainWindowComponents.Buttons;
import view.MainWindow.MainWindowComponents.Menus;
import view.MainWindow.MainWindowComponents.Panels;
import view.MainWindow.MainWindowComponents.Statusbars;
import view.MainWindow.MainWindowComponents.Toolbars;
import view.utilities.StatusBar;

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
	/**
	 * The value of this constant is {@value} and it was autogenerated
	 */
	private static final long serialVersionUID = 2740437090361841747L;

	/**
	 * Represent choosen category which is used to group collection view againts
	 * specified criteria
	 */
	private String groupBy = "null";

	/**
	 * interceptor of all action events coming from MainWindow
	 */
	private MainWindowListener mainWindowListener;

	private MainWindowComponents mwComponents;
	protected Buttons bRef;
	protected Panels pRef;
	protected Menus mRef;
	protected Statusbars sRef;
	protected Toolbars tRef;

	/**
	 * Constructor of the main windows, calls for all private method to
	 * initialize the end user view
	 * 
	 * @param title
	 *            title of the window
	 * @param d
	 *            dimension of the window
	 * @see Dimension
	 */
	public MainWindow(String title, Dimension d) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(false);
		setLocationRelativeTo(null); // centering on the screen

		// setting refs to nested classes
		mwComponents = new MainWindowComponents();
		bRef = mwComponents.buttons;
		pRef = mwComponents.panels;
		mRef = mwComponents.menus;
		tRef = mwComponents.toolbars;
		sRef = mwComponents.statusBars;

		setSize(d);

		initMenuBar();
		initUserContent();
		establishActionConnections();
	}
	
	/**
	 * method goes throughout all fields of {@link MainWindow} and add
	 * actionListeners to every each one of them that requires it
	 */
	private void establishActionConnections() {
		this.mainWindowListener = new MainWindowListener(this);
		
		//buttons listeners
		this.bRef.aboutMe.addActionListener(this.mainWindowListener);
		this.bRef.editButton.addActionListener(this.mainWindowListener);
		this.bRef.exitButton.addActionListener(this.mainWindowListener);
		this.bRef.newItem.addActionListener(this.mainWindowListener);
		this.bRef.publishButton.addActionListener(this.mainWindowListener);
		this.bRef.toogleConnection.addActionListener(this.mainWindowListener);
	}

	/**
	 * Method is taking care of initializing window content, that is:
	 * <ul>
	 * <li>collection view</li>
	 * <li>friends list</li>
	 * <li>friends interaction block</li>
	 * <li>buttons</li>
	 * </ul>
	 * and placing it on layouts
	 */
	private void initUserContent() {
		// adding content jpanel
		this.pRef.contentPane = new JPanel(true);
		this.pRef.contentPane.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));

		this.setContentPane(this.pRef.contentPane);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		// content
		initToolBar();
		initCollectionView();
		initUserList();
		initBottomPanel();

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createParallelGroup(Alignment.CENTER)
				.addComponent(this.tRef.toolBar, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(this.pRef.collectionView,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												Alignment.TRAILING)
												.addComponent(
														this.pRef.userList,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														200)))
				.addComponent(this.pRef.bottomPanel, GroupLayout.DEFAULT_SIZE,
						getContentPane().getWidth(), Short.MAX_VALUE));

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addComponent(this.tRef.toolBar, GroupLayout.PREFERRED_SIZE,
						40, GroupLayout.PREFERRED_SIZE)
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(this.pRef.collectionView)
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(
														this.pRef.userList)))
				.addComponent(this.pRef.bottomPanel,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						50));

		this.revalidate();
		this.repaint();
		this.pack();
	}

	/**
	 * Method is taking care of initializing bottom panel that contains
	 * 
	 * <ul>
	 * <li> {@link MainWindow#statusBar}</li>
	 * <li> {@link MainWindow#databaseStatusBar}</li>
	 * <li> {@link MainWindow#publishButton}</li>
	 * <ul>
	 */
	private void initBottomPanel() {
		this.pRef.bottomPanel = new JPanel(true);
		this.pRef.bottomPanel.setBorder(BorderFactory.createEmptyBorder());

		this.sRef.statusBar = new StatusBar();
		this.sRef.statusBar.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));

		this.pRef.actionsPanel = new JPanel(new FlowLayout());
		this.pRef.actionsPanel.setBorder(BorderFactory.createEmptyBorder());

		this.bRef.toogleConnection = new JButton("Connect");
		this.bRef.newItem = new JButton("New");
		this.bRef.editButton = new JButton("Edit");
		this.bRef.exitButton = new JButton("Exit");
		this.bRef.aboutMe = new JButton("User");

		this.pRef.actionsPanel.add(this.bRef.aboutMe);
		this.pRef.actionsPanel.add(new JLabel("|"));
		this.pRef.actionsPanel.add(this.bRef.newItem);
		this.pRef.actionsPanel.add(this.bRef.editButton);
		this.pRef.actionsPanel.add(new JLabel("|"));
		this.pRef.actionsPanel.add(this.bRef.toogleConnection);
		this.pRef.actionsPanel.add(this.bRef.exitButton);

		this.bRef.publishButton = new JButton("Sync up");

		// organizing into the layout
		GroupLayout layout = new GroupLayout(this.pRef.bottomPanel);
		this.pRef.bottomPanel.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup()
								.addComponent(this.pRef.actionsPanel, 100,
										this.getWidth() - 200, this.getWidth())
								.addComponent(this.sRef.statusBar, 100,
										this.getWidth() - 200, this.getWidth()))
				.addComponent(this.bRef.publishButton,
						GroupLayout.DEFAULT_SIZE, 140,
						GroupLayout.PREFERRED_SIZE));

		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup()
						.addGroup(
								layout.createSequentialGroup()
										.addComponent(this.pRef.actionsPanel)
										.addComponent(this.sRef.statusBar))
						.addComponent(this.bRef.publishButton, GroupLayout.DEFAULT_SIZE,
								62, GroupLayout.PREFERRED_SIZE)));

		layout.linkSize(SwingConstants.HORIZONTAL, this.sRef.statusBar,
				this.pRef.actionsPanel);
	}

	/**
	 * Method initializes userList panel along with all subcomponents actions
	 * and buttons
	 */
	private void initUserList() {
		JPanel userListPanel = new JPanel(new BorderLayout(), true);

		this.pRef.userList = new JScrollPane(userListPanel);
		this.pRef.userList.setWheelScrollingEnabled(true);
		this.pRef.userList
				.setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
						"Users"));

		// TODO add action to view menu
		JPopupMenu userMenu = new JPopupMenu("Friend popup");
		userMenu.add(new JMenuItem("Chat"));
		userMenu.add(new JSeparator(JSeparator.HORIZONTAL));
		userMenu.add(new JMenuItem("List collection"));
		userMenu.add(new JSeparator(JSeparator.HORIZONTAL));
		userMenu.add(new JMenuItem("Make friend"));
		userMenu.add(new JMenuItem("Block"));
		userMenu.setSelected(this.pRef.collectionView);

		// creating table view for user list
		JTable userTable = new JTable(20, 3);

		// setting up to hierarchy
		userListPanel.add(userTable);
		userListPanel.setComponentPopupMenu(userMenu);
	}

	/**
	 * Method initialized collection view along with all subcomponents
	 */
	private void initCollectionView() {
		this.pRef.collectionView = new JPanel(new BorderLayout(), true);
		this.pRef.collectionView.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Collection"));

		// TODO add listener
		JPopupMenu collectionMenu = new JPopupMenu("Collection popup menu");
		collectionMenu.add(new JMenuItem("Edit"));
		collectionMenu.add(new JMenuItem("Remove"));
		collectionMenu.add(new JMenuItem("Publish/Unpublish"));
		collectionMenu.setSelected(this.pRef.collectionView);
	}

	/**
	 * Method initialize toolbar of the application by adding shortcut buttons
	 * triggering top actions from each menu and place it on panel
	 */
	private void initToolBar() {
		this.tRef.toolBar = new JToolBar("Mabis toolbar", JToolBar.HORIZONTAL);
		this.tRef.toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.tRef.toolBar.setOpaque(false);
		this.tRef.toolBar.setAutoscrolls(false);
		this.tRef.toolBar.setFloatable(false);
		this.tRef.toolBar.setRollover(false);

		// database
		String z[] = { "Online", "Local" };
		JComboBox<String> dbContent = new JComboBox<String>(z);
		JLabel collectionLabel = new JLabel("Collection : ");
		collectionLabel.setLabelFor(dbContent);
		this.tRef.toolBar.add(collectionLabel);
		this.tRef.toolBar.add("Collection", dbContent);

		// zoom ;-)
		String zz[] = { "100%", "90%", "80%", "70%", "60%", "50%", "40%",
				"30%", "20%", "10%" };
		JComboBox<String> zoomContent = new JComboBox<String>(zz);
		JLabel zoomLabel = new JLabel("Zoom : ");
		zoomLabel.setLabelFor(zoomContent);
		this.tRef.toolBar.add("Zoom", zoomLabel);
		this.tRef.toolBar.add("Zoom", zoomContent);

		// view mode ;-)
		String zzz[] = { "All", "Books", "Movies", "Music" };
		JComboBox<String> viewModeContent = new JComboBox<String>(zzz);
		JLabel viewModeLabel = new JLabel("View : ");
		viewModeLabel.setLabelFor(viewModeContent);
		this.tRef.toolBar.add(viewModeLabel);
		this.tRef.toolBar.add("View as", viewModeContent);

		// group by
		// hint -> here
		HashMap<String, String[]> groups = new HashMap<String, String[]>();
		String bookGroup[] = { "Author", "ISBN" };
		String movieGroup[] = { "Director" };
		String audioGroup[] = { "Band" };
		String mutualGroup[] = { "No group", "Title", "Year", "Genre" };
		groups.put("book", bookGroup);
		groups.put("movie", movieGroup);
		groups.put("audio", audioGroup);
		groups.put("null", mutualGroup);

		JComboBox<String> groupBy = new JComboBox<String>(
				groups.get(this.groupBy));
		JLabel groupByLabel = new JLabel("Group by : ");
		groupByLabel.setLabelFor(groupBy);
		this.tRef.toolBar.add(groupByLabel);
		this.tRef.toolBar.add("Group by", groupBy);

		JButton revalidateButton = new JButton("Evaluate");
		this.tRef.toolBar.add(revalidateButton);
	}

	/**
	 * Method initialize menuBar and its content along with all required
	 * listeners that are to listen key shortcuts or clicks triggering some
	 * action
	 * 
	 * @see MenuBar
	 */
	private void initMenuBar() {
		// customizing menu bar
		this.mRef.menuBar = new JMenuBar();

		// file menu
		JMenu file = new JMenu("File");
		file.add(new JMenuItem("Connect"));

		// collection menu
		JMenu collection = new JMenu("Connection");
		collection.add(new JMenuItem("Publish"));

		// friends menu
		JMenu friends = new JMenu("Friends");
		friends.add(new JMenuItem("List"));

		// data menu
		JMenu data = new JMenu("Data");
		data.add("Action");

		// Help
		JMenu help = new JMenu("Help");
		help.add(new JMenuItem("About"));

		this.mRef.menuBar.add(file);
		this.mRef.menuBar.add(collection);
		this.mRef.menuBar.add(friends);
		this.mRef.menuBar.add(data);
		this.mRef.menuBar.add(help);

		// this.setMenuBar(menuBar);
		this.setJMenuBar(this.mRef.menuBar);
	}

	class MainWindowComponents {
		public final Panels panels = new Panels();
		public final Buttons buttons = new Buttons();
		public final Toolbars toolbars = new Toolbars();
		public final Menus menus = new Menus();
		public final Statusbars statusBars = new Statusbars();

		protected class Buttons {
			/**
			 * JButton to trigger action responsible for publishing local
			 * collection and placing it on the remote server
			 */

			protected JButton publishButton;
			/**
			 * button to toogle connection by following pattern
			 * <ul>
			 * <li>when disconnected, connection attempt will be performed +
			 * user list will be displayed</li>
			 * <li>when connection, connection will be lost</li>
			 * </ul>
			 * What is important here, that connection is understood as <b>
			 * 
			 * <pre>
			 * being logged in to program,not database
			 * </pre>
			 * 
			 * </b>
			 */
			protected JButton toogleConnection;

			/**
			 * JButton to exit from the application. </br> <b>Notice</b> that
			 * exit process will trigger database disconnection event
			 */
			protected JButton exitButton;

			/**
			 * JButton to add new item to the database</br> When triggered user
			 * is asked what kind of item he would like to add only when
			 * <b>Collection view</b> is set to <b><i>ALL</i></b>
			 */
			protected JButton newItem;

			/**
			 * JButton to edit selected, from <b>Collection view</b>, item
			 */
			protected JButton editButton;

			/**
			 * JButton to show JFrame containing currently loggeed user
			 * informations and statistics
			 */
			protected JButton aboutMe;
		}

		protected class Menus {
			/**
			 * Menu bar of the main windows, contains all the actions even those
			 * not included in main window content area
			 */
			public JMenuBar menuBar;
		}

		protected class Toolbars {
			/**
			 * ToolBar aggregates buttons triggering most used actions.
			 * Contains:
			 * <ul>
			 * <li>zoom combo box</li>
			 * <li>group by combo box</li>
			 * <li>search text field</li>
			 * <li>search button</li>
			 * </ul>
			 * to perform operations againt collection view
			 */
			public JToolBar toolBar;
		}

		protected class Panels {
			/**
			 * JPanel for entire JFrame, all elements are going to be placed
			 * upon him
			 */
			private JPanel contentPane;

			/**
			 * JPanel to hold collection view, that will be computeted and
			 * presented in user specified form, meaning as:
			 * <ul>
			 * <li>covers composition</li>
			 * <li>list of entities</li>
			 * </ul>
			 */
			private JPanel collectionView;

			/**
			 * JPanel containing:
			 * <ul>
			 * <li>dynamic status bar, displaying <b>runtime</b> information</li>
			 * <li>constant status bar, displaying <b>database usage
			 * statistic</b></li>
			 * </ul>
			 * 
			 * @see MainWindow#databaseStatusBar
			 * @see MainWindow#statusBar
			 */
			private JPanel bottomPanel;

			/**
			 * JScrollPanel containing list of user located at the remote server
			 * Users that are friends are marked highlighted, those considered
			 * foes are blocked
			 */
			private JScrollPane userList;

			/**
			 * JPanel to aggregate action buttons
			 * 
			 * @see JPanel
			 */
			private JPanel actionsPanel;
		}

		protected class Statusbars {
			/**
			 * StatusBar to print runtime information
			 * 
			 * @see StatusBar
			 */
			private StatusBar statusBar;
		}
	}
}
