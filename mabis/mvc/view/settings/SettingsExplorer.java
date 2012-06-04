package mvc.view.settings;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import mvc.view.WindowClosedListener;
import settings.GlobalPaths;
import settings.io.SettingsException;
import settings.io.SettingsLoader;

public class SettingsExplorer extends JFrame {
	private static final long serialVersionUID = -1392950610679954491L;
	private JTabbedPane tabManager;
	private JButton viewFileSystemButton;

	public SettingsExplorer() {
		super("Settings");
		this.initComponents();
		this.layoutComponents();
		this.addWindowListener(new WindowClosedListener());
		try {
			SettingsLoader.loadFrame(this);
		} catch (SettingsException e) {
			e.printStackTrace();
		}
		this.viewFileSystemButton = new JButton("Search...");
	}

	private void layoutComponents() {
	}

	private void initComponents() {
		this.tabManager = new JTabbedPane(JTabbedPane.BOTTOM,
				JTabbedPane.SCROLL_TAB_LAYOUT);

		for(GlobalPaths p :  GlobalPaths.values()){
			this.tabManager.addTab("Paths", new SettingPathChunk(p));
		}
	}

	private class SettingPathChunk extends JPanel {
		private static final long serialVersionUID = 1504580042150900835L;
		private JTextField pathField;
		private JButton pathAcceptButton;
		private JButton openFileChooserButton;
		private GlobalPaths type;
		private JLabel pathTypeLabel;
		private ActionListener listener;

		public SettingPathChunk(GlobalPaths type) {
			this.type = type;
			this.initComponents();
			this.layoutComponents();
		}

		private void layoutComponents() {
			this.setLayout(new GridLayout(1, 3));
			
			this.add(this.pathTypeLabel);
			this.add(this.pathField);
			this.add(this.pathAcceptButton);
			this.add(openFileChooserButton);
		}

		private void initComponents() {
			this.pathTypeLabel = new JLabel(this.type.name());
			this.pathField = new JTextField();
			this.pathAcceptButton = new JButton("Set");
			
			this.listener = new SettingsPathExplorerListener();
			this.pathAcceptButton.addActionListener(this.listener);
			openFileChooserButton.addActionListener(this.listener);
		}

		private class SettingsPathExplorerListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton source = (JButton) e.getSource();
				if (source.equals(pathAcceptButton)) {

				} else if(source.equals(viewFileSystemButton)){

				}
			}
		}
	}

}
