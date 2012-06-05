package mvc.view.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

import mvc.view.WindowClosedListener;
import settings.io.SettingsException;
import settings.io.SettingsLoader;

public class SettingsExplorer extends JFrame {
	private static final long serialVersionUID = -1392950610679954491L;
	private JTabbedPane tabManager;
	private JPanel contentPane;
	private JButton closeButton;

	public SettingsExplorer() {
		super();
		this.addWindowListener(new WindowClosedListener());
		try {
			SettingsLoader.loadFrame(this);
		} catch (SettingsException e) {
			this.setLocationRelativeTo(null);
			this.setTitle("Settings explorer");
		}
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.initComponents();
		this.layoutComponents();
	}

	private void layoutComponents() {
		this.contentPane = new JPanel(true);
		this.contentPane.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));

		this.setContentPane(this.contentPane);

		GroupLayout gl = new GroupLayout(getContentPane());
		getContentPane().setLayout(gl);

		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup()
				.addComponent(this.tabManager, 500, 600, 700)
				.addComponent(this.closeButton, 500, 600, 700));
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(this.tabManager)
				.addComponent(this.closeButton, 50, 50, 50));

		this.revalidate();
		this.pack();
	}

	private void initComponents() {
		this.tabManager = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);

		this.tabManager.addTab("Paths", new PathExplorer());

		this.closeButton = new JButton("Exit");
		this.closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
	}

}
