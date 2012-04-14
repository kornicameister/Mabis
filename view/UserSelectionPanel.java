package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import logger.MabisLogger;
import model.entity.User;
import view.mainwindow.MainWindow;
import controller.InvalidBaseClass;
import controller.SQLStamentType;
import controller.entity.UserSQLFactory;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import view.imagePanel.ChoosableImagePanel;

public class UserSelectionPanel extends JDialog implements PropertyChangeListener {

    private static final long serialVersionUID = -3642888588569732458L;
    private static final Dimension avatarSize = new Dimension(100, 100);
    private MainWindow mw = null;
    private JButton connectButton = null;
    private JButton cancelButton = null;
    private JPanel userListPanel;
    private UserSQLFactory userFactory = null;
    private TreeMap<User, ChoosableImagePanel> thumbails = null;
    private HashMap<Integer, User> users = null;
    private final UserSelectionPanelListener listener;
    private JPanel rootListPanel = null;
    private JScrollPane userScrollPanel = null;
    private short selectedUserIndex = -1;

    public UserSelectionPanel(Frame owner) {
        super(owner);
        this.mw = (MainWindow) owner;
        this.userFactory = new UserSQLFactory();
        this.thumbails = new TreeMap<User, ChoosableImagePanel>();
        this.listener = new UserSelectionPanelListener();

        this.initComponents();
        this.layoutComponents();

        this.obtainUsers();
        this.initThumbailList();
        this.initMeta();
    }

    private void initThumbailList() {
        for (User u : this.users.values()) {
            // we have user u, now lets get it's panel
            ChoosableImagePanel i = this.thumbails.get(u);
            i.setPreferredSize(avatarSize);
            i.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                    u.getLogin()));
            this.userListPanel.add(i);
        }
    }

    private void initMeta() {
        this.setSize(new Dimension(500, 280));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Connect as...");
        this.setLocationRelativeTo(null);
    }

    private void obtainUsers() {
        try {
            this.userFactory.setTable(new User());
            this.userFactory.setStatementType(SQLStamentType.SELECT);
            this.userFactory.executeSQL();
            users = this.userFactory.getUsers();
            for (User u : this.users.values()) {
                ChoosableImagePanel p = new ChoosableImagePanel(u.getPicture(), u.getPictureFile().getPath());
                p.addPropertyChangeListener(this);
                thumbails.put(u, p);
            }
        } catch (InvalidBaseClass e) {
            e.printStackTrace();
        }
    }

    public final void initComponents() {
        this.rootListPanel = new JPanel(true);

        this.userScrollPanel = new JScrollPane(this.rootListPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.userScrollPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                "Users"));

        this.userListPanel = new JPanel(true);
        this.userListPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.rootListPanel.add(this.userListPanel);

        this.connectButton = new JButton("Connect");
        this.connectButton.addActionListener(this.listener);
        this.cancelButton = new JButton("Cancel");
        this.cancelButton.addActionListener(this.listener);

    }

    public final void layoutComponents() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup().addComponent(this.userScrollPanel).addGroup(layout.createSequentialGroup().
                addComponent(this.connectButton,
                GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE,
                Short.MAX_VALUE).
                addComponent(this.cancelButton,
                GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE,
                Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createSequentialGroup().addComponent(this.userScrollPanel).addGroup(layout.createParallelGroup().addComponent(this.connectButton).addComponent(this.cancelButton)));

        this.pack();
        this.repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if ((Boolean) e.getNewValue() == true) {
            ChoosableImagePanel p = (ChoosableImagePanel) e.getSource();
            this.selectedUserIndex = -1;
            boolean panelLocated = false;
            for (ChoosableImagePanel pp : this.thumbails.values()) {
                pp.demark();
                if (!panelLocated) {
                    if (pp.equals(p)) {
                        panelLocated = true;
                        pp.mark();
                        this.selectedUserIndex += 1;
                    } else {
                        this.selectedUserIndex += 1;
                    }
                }
            }
        }
    }

    class UserSelectionPanelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.equals(connectButton)) {
                connectWithUser();
                if (isDisplayable()) {
                    setVisible(false);
                    dispose();
                }
            } else if (source.equals(cancelButton)) {
                if (isDisplayable()) {
                    setVisible(false);
                    dispose();
                }
            }
            MabisLogger.getLogger().log(Level.INFO, source.getActionCommand());
        }

        private void connectWithUser() {
            try {
                User u = (User) users.values().toArray()[selectedUserIndex];

                UserSQLFactory f = new UserSQLFactory();
                f.setStatementType(SQLStamentType.SELECT);
                f.setTable(new User());
                f.addWhereClause("idUser", u.getPrimaryKey().toString());
                f.executeSQL();
                if (!f.getUsers().isEmpty()) {
                    mw.getBottomPanel().getStatusBar().setMessage("Connected as: " + u.getLogin() + "/" + u.getEmail());
                    mw.setConnectedUser(u);
                } else {
                    mw.getBottomPanel().getStatusBar().setMessage("Failed to connect using following credentials: " + u.getLogin() + "/" + u.getEmail());
                }
            } catch (InvalidBaseClass ex) {
                MabisLogger.getLogger().log(Level.SEVERE, null, ex);
            }

        }
    }
}
