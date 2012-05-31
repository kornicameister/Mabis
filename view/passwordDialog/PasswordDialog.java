package view.passwordDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class PasswordDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 8470460108291440058L;
	private JPasswordField password;
	private JButton ok, cancel;
	private JLabel prompt;
	JPanel passPanel, labelInput, buttons;

	private String result;

	public PasswordDialog(JFrame parent) {
		super(parent);

		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Password:");
		this.setAlwaysOnTop(true);
		this.setModal(true);

		this.setSize(250, 250);
		this.setLocationRelativeTo(parent);

	}

	@Override
	protected void dialogInit() {
		super.dialogInit();

		this.passPanel = new JPanel();
		this.labelInput = new JPanel();
		this.buttons = new JPanel();
		this.password = new JPasswordField();
		this.ok = new JButton("Ok");
		this.cancel = new JButton("Cancel");
		this.prompt = new JLabel("Please enter your password:");

		passPanel.setLayout(new BorderLayout());
		labelInput.setLayout(new GridLayout(2, 1, 5, 5));
		buttons.setLayout(new BorderLayout());

		password.setEchoChar('*');

		ok.addActionListener(this);
		cancel.addActionListener(this);

		labelInput.add(prompt);
		labelInput.add(password);
		buttons.add(ok, BorderLayout.WEST);
		buttons.add(cancel, BorderLayout.EAST);
		passPanel.setPreferredSize(new Dimension(200, 100));
		passPanel.add(labelInput, BorderLayout.NORTH);
		passPanel.add(buttons, BorderLayout.SOUTH);
		this.add(passPanel);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			result = new String(password.getPassword());
		}
		this.dispose();
	}

	public String getPassword() {
		return result;
	}

	public void showDialog() {
		this.setVisible(true);
	}
}
