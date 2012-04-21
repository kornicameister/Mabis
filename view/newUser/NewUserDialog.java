/**
 *
 */
package view.newUser;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
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
import model.entity.Picture;
import model.entity.User;
import view.imagePanel.ImageFileFilter;
import view.imagePanel.ImageFilePreview;
import view.imagePanel.ImagePanel;
import view.mainwindow.MainWindow;

/**
 * @author kornicameister
 * 
 */
public class NewUserDialog extends JDialog {

	private static final long serialVersionUID = -1413322815019667627L;
	private static final Dimension avatarDim = new Dimension(512, 512);
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
		this.initMetaData();
		this.initComponents();
		this.layoutComponents();
		this.initChooser();
	}

	private void initChooser() {
		this.imageChooser = new JFileChooser();
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
																		this.lnameField))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		this.okButton,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE,
																		Integer.MAX_VALUE)
																.addComponent(
																		this.cancelButton,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE,
																		Integer.MAX_VALUE)
																.addComponent(
																		this.newAvatarButton)))
								.addGroup(
										layout.createParallelGroup()
												.addComponent(
														this.imagePanel,
														GroupLayout.DEFAULT_SIZE,
														135, 135))));
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
																		20))
												.addGroup(
														layout.createParallelGroup()
																.addComponent(
																		this.okButton,
																		GroupLayout.DEFAULT_SIZE,
																		0,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(
																		this.cancelButton,
																		GroupLayout.DEFAULT_SIZE,
																		0,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(
																		this.newAvatarButton)))
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(
														this.imagePanel,
														GroupLayout.DEFAULT_SIZE,
														135, 135))));
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

		this.imagePanel = new ImagePanel(new File(
				"src/resources/defaultAvatar.png"));
		this.imagePanel.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));

		this.okButton = new JButton("Create new user");
		this.okButton.addActionListener(this.listener);
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(this.listener);

		this.newAvatarButton = new JButton("New avatar...");
		this.newAvatarButton.addActionListener(this.listener);
	}

	private void initMetaData() {
		this.setTitle("Creating new user...");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(this.getOwner());
		this.setMinimumSize(new Dimension(380, 220));
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
				try {
					user.setPicture(new Picture(imagePanel.getImageFile()));
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
				user.setPassword(new String(passField.getPassword()));

				try {
					NewUserRegisterer.saveToLocalDatabase(user);
					NewUserRegisterer.saveToOnlineDatabase(user);
				} catch (SQLException e1) {
					e1.printStackTrace();
				} finally {
					if (isDisplayable()) {
						setVisible(false);
						dispose();
					}
				}

				mw.getBottomPanel()
						.getStatusBar()
						.setMessage(
								"New user " + user.getLogin() + " was created");

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
					ImageIcon tmp;
					try {
						File newAvatar = new File(imageChooser
								.getSelectedFile().getAbsolutePath());
						tmp = new ImageIcon(ImageIO.read(newAvatar));
						if (tmp.getIconHeight() > avatarDim.height
								|| tmp.getIconWidth() > avatarDim.width) {
							JOptionPane.showMessageDialog(backReference,
									"Avatar you're trying to use is too big",
									"Avatar size error",
									JOptionPane.ERROR_MESSAGE);
							MabisLogger.getLogger().log(
									Level.WARNING
									,
									"Too big avatar attempt, avatar size = ["
											+ tmp.getIconWidth() + ";"
											+ tmp.getIconHeight() + "]");
						} else {
							imagePanel.setImg(newAvatar);
							imagePanel.repaint();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					imageChooser.setSelectedFile(null);
				}
			}
			MabisLogger.getLogger().log(Level.INFO, source.getActionCommand(),
					source);
		}
	}
}
