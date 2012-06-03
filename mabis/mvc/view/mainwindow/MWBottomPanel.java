/**
 *
 */
package mvc.view.mainwindow;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import logger.MabisLogger;
import mvc.view.AboutMabis;
import mvc.view.utilities.StatusBar;

/**
 * @author kornicameister contains: <ul> <li> {@link MainWindow#statusBar}</li>
 * <li> {@link MainWindow#databaseStatusBar}</li> <li> {@link MainWindow#publishButton}</li>
 * <ul>
 */
public class MWBottomPanel extends JPanel {

    private static final long serialVersionUID = 7673272237316575906L;
    private StatusBar statusBar;
    private JPanel actionsPanel;
    private JButton toogleConnection;
    private JButton newItem;
    private JButton editButton;
    private JButton aboutMe;
    private JButton exitButton;
    private JButton publishButton;
    private MainWindowBottomPanelListener listener;
    private final MainWindow mwParent;

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
        this.listener = new MainWindowBottomPanelListener();
        this.initComponents();
    }

    private void initComponents() {
        this.statusBar = new StatusBar();
        this.statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        this.actionsPanel = new JPanel(new FlowLayout());
        this.actionsPanel.setBorder(BorderFactory.createEmptyBorder());

        this.toogleConnection = new JButton("Connect");
        this.newItem = new JButton("New");
        this.editButton = new JButton("Edit");
        this.exitButton = new JButton("Exit");
        this.aboutMe = new JButton("User");
        this.publishButton = new JButton("Sync up");

        this.actionsPanel.add(this.aboutMe);
        this.actionsPanel.add(new JLabel("|"));
        this.actionsPanel.add(this.newItem);
        this.actionsPanel.add(this.editButton);
        this.actionsPanel.add(new JLabel("|"));
        this.actionsPanel.add(this.toogleConnection);
        this.actionsPanel.add(this.exitButton);

        // adding listeners
        this.aboutMe.addActionListener(this.listener);
        this.editButton.addActionListener(this.listener);
        this.exitButton.addActionListener(this.listener);
        this.newItem.addActionListener(this.listener);
        this.publishButton.addActionListener(this.listener);
        this.toogleConnection.addActionListener(this.listener);

        // organizing into the layout
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
                layout.createParallelGroup().addComponent(this.actionsPanel,
                GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE,
                Short.MAX_VALUE).addComponent(this.statusBar,
                GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE,
                Short.MAX_VALUE)).addComponent(this.publishButton, GroupLayout.DEFAULT_SIZE,
                140, GroupLayout.PREFERRED_SIZE));

        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
                layout.createParallelGroup().addGroup(
                layout.createSequentialGroup().addComponent(this.actionsPanel).addComponent(this.statusBar)).addComponent(this.publishButton,
                GroupLayout.DEFAULT_SIZE, 62,
                GroupLayout.PREFERRED_SIZE)));
    }

    class MainWindowBottomPanelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            Object s = arg0.getSource();
            if (s.equals(aboutMe)) {
                AboutMabis w = new AboutMabis();
                w.setVisible(true);
            } else if (s.equals(editButton)) {
            } else if (s.equals(exitButton)) {
                mwParent.setVisible(false);
                mwParent.dispose();
            } else if (s.equals(newItem)) {
            } else if (s.equals(publishButton)) {
            } else if (s.equals(toogleConnection)) {
            }
            MabisLogger.getLogger().log(Level.INFO,
                    ((JButton) s).getActionCommand());
        }
    }

    public StatusBar getStatusBar() {
        return this.statusBar;
    }
}
