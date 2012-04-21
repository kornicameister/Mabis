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
	private JPasswordField password = new JPasswordField();
	private JButton ok = new JButton("OK");
	private JButton cancel = new JButton("Cancel");
	private JLabel prompt = new JLabel("Please enter your password:");
	
	JPanel passPanel = new JPanel();
	JPanel labelInput = new JPanel();
	JPanel buttons = new JPanel();
	
	private String result;

	public PasswordDialog(JFrame parent) {
		super(parent);
	
		this.setResizable(false);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		passPanel.setLayout(new BorderLayout());
		labelInput.setLayout(new GridLayout(2, 1));
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
		this.setSize(200, 200);
		this.setLocationRelativeTo(null);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			result = new String(password.getPassword());
		} else {
			this.dispose();
		}
	}

	public String getPassword() {
		return result;
	}

	public void showDialog() {
		this.setVisible(true);
	}
}
