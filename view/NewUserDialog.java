/**
 * 
 */
package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import logger.MabisLogger;
import model.entity.User;
import view.imagePanel.ImageFileFilter;
import view.imagePanel.ImageFilePreview;
import view.imagePanel.ImagePanel;
import view.mainwindow.MainWindow;
import controller.InvalidBaseClass;
import controller.SQLStamentType;
import controller.entity.UserSQLFactory;

/**
 * @author kornicameister
 * 
 */
public class NewUserDialog extends JDialog {
	private static final long serialVersionUID = -1413322815019667627L;
	private JLabel mailLabel = null;
	private JTextField mailField = null;
	private JSeparator horizontalLine = null;
	private JLabel loginLabel = null;
	private JTextField loginField = null;
	private JLabel fnameLabel = null;
	private JTextField fnameField = null;
	private JLabel lnameLabel = null;
	private JTextField lnameField = null;
	private ImagePanel imagePanel = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JButton newAvatarButton = null;
	private NewUserDialogListener listener = null;
	private JFileChooser imageChooser = null;
	private JLabel passLabel = null;
	private JPasswordField passField = null;
	private MainWindow mw = null;

	/**
	 * @param owner
	 * @param modalspecifies
	 *            whether dialog blocks user input to other top-level windows
	 *            when shown. If true, the modality type property is set to
	 *            DEFAULT_MODALITY_TYPE, otherwise the dialog is modeless.
	 */
	public NewUserDialog(MainWindow owner, boolean modal) {
		super(owner, modal);
		this.mw = owner;
		this.listener = new NewUserDialogListener(this);
		this.initComponents();
		this.initMetaData();
		this.layoutComponents();
		this.initChooser();
	}

	private void initChooser() {
		this.imageChooser = new JFileChooser();
		// this.imageChooser.addChoosableFileFilter(new ImageFileFilter());
		// this.imageChooser.setAcceptAllFileFilterUsed(false);
		this.imageChooser.setFileFilter(new ImageFileFilter());
		this.imageChooser.setAccessory(new ImageFilePreview(imageChooser));
	}

	private void layoutComponents() {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createParallelGroup()
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup()
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		this.loginLabel)
																.addComponent(
																		this.loginField))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		this.mailLabel)
																.addComponent(
																		this.mailField))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		this.passLabel)
																.addComponent(
																		this.passField))
												.addComponent(
														this.horizontalLine)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		this.fnameLabel,
																		50, 73,
																		83)
																.addComponent(
																		this.fnameField))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		this.lnameLabel,
																		50, 73,
																		83)
																.addComponent(
																		this.lnameField)))
								.addGroup(
										layout.createParallelGroup()
												.addComponent(this.imagePanel)
												.addComponent(
														this.newAvatarButton,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE,
														Integer.MAX_VALUE)))
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(this.okButton,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE,
										Integer.MAX_VALUE)
								.addComponent(this.cancelButton,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE,
										Integer.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup()
								.addGroup(
										layout.createSequentialGroup()
												.addGroup(
														layout.createParallelGroup()
																.addComponent(
																		this.loginLabel)
																.addComponent(
																		this.loginField,
																		20, 20,
																		20))
												.addGroup(
														layout.createParallelGroup()
																.addComponent(
																		this.mailLabel)
																.addComponent(
																		this.mailField,
																		20, 20,
																		20))
												.addGroup(
														layout.createParallelGroup()
																.addComponent(
																		this.passLabel)
																.addComponent(
																		this.passField,
																		20, 20,
																		20))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		this.horizontalLine,
																		5, 6, 7))
												.addGroup(
														layout.createParallelGroup()
																.addComponent(
																		this.fnameLabel)
																.addComponent(
																		this.fnameField,
																		20, 20,
																		20))
												.addGroup(
														layout.createParallelGroup()
																.addComponent(
																		this.lnameLabel)
																.addComponent(
																		this.lnameField,
																		20, 20,
																		20)))
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(this.imagePanel)
												.addComponent(
														this.newAvatarButton)))
				.addGroup(
						layout.createParallelGroup()
								.addComponent(this.okButton,
										GroupLayout.DEFAULT_SIZE, 0,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(this.cancelButton,
										GroupLayout.DEFAULT_SIZE, 0,
										GroupLayout.PREFERRED_SIZE)));
		this.revalidate();
		this.repaint();
		this.pack();
	}

	private void initComponents() {
		this.loginLabel = new JLabel("Login: ");
		this.loginField = new JTextField();

		this.mailLabel = new JLabel("EMail: ");
		this.mailField = new JTextField();

		this.passLabel = new JLabel("Pass: ");
		this.passField = new JPasswordField();

		this.horizontalLine = new JSeparator();

		this.fnameLabel = new JLabel("Name: ");
		this.fnameField = new JTextField();

		this.lnameLabel = new JLabel("Last name: ");
		this.lnameField = new JTextField();

		this.imagePanel = new ImagePanel("src/resources/defaultAvatar.png");
		this.imagePanel.setSize(new Dimension(100, 90));
		this.imagePanel.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));

		this.okButton = new JButton("Ok");
		this.okButton.addActionListener(this.listener);
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(this.listener);

		this.newAvatarButton = new JButton("Search...");
		this.newAvatarButton.addActionListener(this.listener);
	}

	private void initMetaData() {
		this.setTitle("Creating new user...");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setMinimumSize(new Dimension(420, 300));
		this.setLocationRelativeTo(this.getOwner());
	}

	class NewUserDialogListener implements ActionListener {
		private NewUserDialog backReference;

		public NewUserDialogListener(NewUserDialog ref) {
			this.backReference = ref;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			if (source == okButton) {
				User user = new User(fnameField.getText(), lnameField.getText());
				user.setLogin(loginField.getText());
				user.setEmail(mailField.getText());
				user.setPicture(imagePanel.getImg(), imagePanel.getImgPath());
				user.setPassword(new String(passField.getPassword()));

				UserSQLFactory factory = new UserSQLFactory();
				factory.setStatementType(SQLStamentType.INSERT);
				try {
					factory.setTable(user);
					if (factory.executeSQL()) {
						mw.getBottomPanel()
								.getStatusBar()
								.setMessage("New user was successfully created");
					} else {
						// TODO add Error JFrame and saving to log
						mw.getBottomPanel()
								.getStatusBar()
								.setMessage(
										"Failed to create new user, see log file for details");
					}
				} catch (InvalidBaseClass e1) {
					e1.printStackTrace();
				} finally {
					if (isDisplayable()) {
						setVisible(false);
						dispose();
					}
				}
			} else if (source == cancelButton) {
				if (isDisplayable()) {
					setVisible(false);
					dispose();
				}
			} else if (source == newAvatarButton) {
				int returnValue = imageChooser.showOpenDialog(backReference);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					// TODO this part must be fixed, problem lies in loading the
					// image without checking it's dimension
					imagePanel.swapImage(imageChooser.getSelectedFile()
							.getAbsolutePath());
					if (imagePanel.getImg().getIconWidth() > 90
							&& imagePanel.getImg().getIconHeight() > 120) {
						JOptionPane.showMessageDialog(backReference,
								"This image is too big");
					} else {
						imagePanel.repaint();
					}
					imageChooser.setSelectedFile(null);
				}
			}
			MabisLogger.getLogger().log(Level.INFO, source.getActionCommand(),
					source);
		}

	}

}
