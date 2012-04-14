/**
 * package view in MABIS by kornicameister
 */
package view.mainwindow;

import database.MySQLAccess;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.GroupLayout.Alignment;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import model.entity.User;
import view.NewUserDialog;
import view.UserSelectionPanel;

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
    protected MWMenuBar menuBar = null;
    protected MWToolBar toolBar = null;
    private MWBottomPanel bottomPanel = null;
    private MWUserList userListPanel = null;
    private JPanel contentPane = null;
    private JPanel collectionView = null;
    private MySQLAccess mysql = null;
    private User connectedUser = null;

    /**
     * Constructor of the main windows, calls for all private method to
     * initialize the end user view
     *
     * @param title title of the window
     * @param d dimension of the window
     * @see Dimension
     */
    public MainWindow(String title, Dimension d) {
        super(title);

        this.bottomPanel = new MWBottomPanel();
        this.setJMenuBar(new MWMenuBar(this));
        this.toolBar = new MWToolBar("Mabis toolbar", JToolBar.HORIZONTAL);
        this.collectionView = new MWCollectionView(new BorderLayout(), true);
        this.userListPanel = new MWUserList(new BorderLayout(), true);
        layoutComponents();

        setSize(d);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(false);
        setLocationRelativeTo(null); // centering on the screen

        this.initConnection();
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                checkForUsers();
            }
        });
    }

    private void checkForUsers() {
        // check for any user, if none print NewUserDialog
        if (this.mysql.doWeHaveUser(null)) {
            UserSelectionPanel usp = new UserSelectionPanel(this);
            usp.setVisible(true);
            usp.setAlwaysOnTop(true);
        } else {
            int retVal = JOptionPane.showConfirmDialog(this,
                    "No users found\nWould like to create new user ?");
            if (retVal == JOptionPane.OK_OPTION) {
                NewUserDialog newUser = new NewUserDialog(this, true);
                newUser.setVisible(true);
                this.checkForUsers();
            }
        }
    }

    private void initConnection() {
        this.mysql = new MySQLAccess();
        if (!this.mysql.connect()) {
            JOptionPane.showMessageDialog(this, "Failed to connect to MABIS");
        } else {
            this.bottomPanel.getStatusBar().setMessage(
                    "Mabis connection established");
        }
    }

    /**
     * Method is taking care of initializing window content, that is: <ul>
     * <li>collection view</li> <li>friends list</li> <li>friends interaction
     * block</li> <li>buttons</li> </ul> and placing it on layouts
     */
    private void layoutComponents() {
        // adding content jpanel
        this.contentPane = new JPanel(true);
        this.contentPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        this.setContentPane(this.contentPane);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        this.bottomPanel.setBorder(BorderFactory.createEtchedBorder());

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(this.toolBar, GroupLayout.DEFAULT_SIZE,
                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(
                layout.createSequentialGroup().addComponent(this.collectionView,
                GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE).addGroup(
                layout.createParallelGroup(
                Alignment.TRAILING).addComponent(
                this.userListPanel,
                GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE,
                200))).addComponent(this.bottomPanel, GroupLayout.DEFAULT_SIZE,
                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(this.toolBar, GroupLayout.PREFERRED_SIZE, 40,
                GroupLayout.PREFERRED_SIZE).addGroup(
                layout.createParallelGroup(Alignment.BASELINE).addComponent(this.collectionView).addGroup(
                layout.createSequentialGroup().addComponent(
                this.userListPanel))).addComponent(this.bottomPanel, GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE, 50));

        this.repaint();
        this.pack();
    }

    public MWBottomPanel getBottomPanel() {
        return this.bottomPanel;
    }

    public void setConnectedUser(User u) {
        this.connectedUser = u;
    }
}
