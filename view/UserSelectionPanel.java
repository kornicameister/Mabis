package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import controller.InvalidBaseClass;
import controller.SQLStamentType;
import controller.entity.UserSQLFactory;

import logger.MabisLogger;
import model.entity.User;
import view.imagePanel.ImagePanel;
import view.mainwindow.MainWindow;

public class UserSelectionPanel extends JDialog {
	private static final long serialVersionUID = -3642888588569732458L;
	private MainWindow mw = null;
	private JInternalFrame userList = null;
	private JButton connectButton = null;
	private JButton cancelButton = null;
	private JPanel userListPanel;
	private UserSQLFactory userFactory = null;
	private HashMap<Integer, ImagePanel> thumbails = null;

	public UserSelectionPanel(Frame owner) {
		super(owner);
		this.mw = (MainWindow) owner;
		this.userFactory = new UserSQLFactory();
		this.thumbails = new HashMap<Integer, ImagePanel>();
		this.initComponents();
		this.layoutComponents();
		this.obtainUsers();
		this.initMeta();
	}

	private void initMeta() {
		this.setSize(new Dimension(500, 500));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Connect as...");
	}

	private void obtainUsers() {
		try {
			this.userFactory.setTable(new User());
			this.userFactory.setStatementType(SQLStamentType.SELECT);
			this.userFactory.executeSQL();
			HashMap<Integer, User> users = this.userFactory.getUsers();
			for (User u : users.values()) {
				thumbails
						.put(u.getPrimaryKey(), new ImagePanel(u.getPicture()));
			}
		} catch (InvalidBaseClass e) {
			e.printStackTrace();
		}
	}

	public void initComponents() {
		this.userList = new JInternalFrame("User", false, false, false, true);
		this.connectButton = new JButton("Connect");
		this.cancelButton = new JButton("Cancel");

		this.userListPanel = new JPanel(new FlowLayout(), true);
		this.userListPanel.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));
		this.userList.add(this.userListPanel);

	}

	public void layoutComponents() {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addComponent(this.userList)
				.addGroup(
						layout.createParallelGroup()
								.addComponent(this.connectButton)
								.addComponent(this.cancelButton)));
		layout.setVerticalGroup(layout
				.createParallelGroup()
				.addComponent(this.userList)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(this.connectButton)
								.addComponent(this.cancelButton)));

		this.pack();
		this.revalidate();
		this.repaint();
	}

	class UserSelectionPanelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			if (source.equals(connectButton)) {
				mw.getBottomPanel().getStatusBar().setMessage("Connected as ");
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

	}

}
