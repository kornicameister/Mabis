/**
 * 
 */
package view;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

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
	private JPanel imagePanel;

	/**
	 * @param owner
	 * @param modalspecifies
	 *            whether dialog blocks user input to other top-level windows
	 *            when shown. If true, the modality type property is set to
	 *            DEFAULT_MODALITY_TYPE, otherwise the dialog is modeless.
	 */
	public NewUserDialog(Frame owner, boolean modal) {
		super(owner, modal);
		this.initComponents();
		this.initMetaData();
		this.layoutComponents();
		this.initDefaultAvatar();
	}

	private void initDefaultAvatar() {
		
	}

	private void layoutComponents() {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup()
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(this.loginLabel)
												.addComponent(this.loginField))
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(this.mailLabel)
												.addComponent(this.mailField))
								.addComponent(this.horizontalLine)
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(this.fnameLabel)
												.addComponent(this.fnameField))
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(this.lnameLabel)
												.addComponent(this.lnameField)))
				.addComponent(this.imagePanel));
		layout.setVerticalGroup(layout
				.createParallelGroup()
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup()
												.addComponent(this.loginLabel)
												.addComponent(this.loginField))
								.addGroup(
										layout.createParallelGroup()
												.addComponent(this.mailLabel)
												.addComponent(this.mailField))
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(
														this.horizontalLine))
								.addGroup(
										layout.createParallelGroup()
												.addComponent(this.fnameLabel)
												.addComponent(this.fnameField))
								.addGroup(
										layout.createParallelGroup()
												.addComponent(this.lnameLabel)
												.addComponent(this.lnameField)))
				.addComponent(this.imagePanel));
		this.revalidate();
		this.repaint();
		this.pack();
	}

	private void initComponents() {
		this.loginLabel = new JLabel("Login: ");
		this.loginField = new JTextField();

		this.mailLabel = new JLabel("Login: ");
		this.mailField = new JTextField();

		this.horizontalLine = new JSeparator();

		this.fnameLabel = new JLabel("Name: ");
		this.fnameField = new JTextField();

		this.lnameLabel = new JLabel("Last name: ");
		this.lnameField = new JTextField();

		this.imagePanel = new JPanel(true);
		this.imagePanel.setSize(new Dimension(180, 90));
		this.imagePanel.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
	}

	private void initMetaData() {
		this.setTitle("Creating new user...");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setMinimumSize(new Dimension(350, 200));
		this.setLocationRelativeTo(this.getOwner());
	}

}
