package mvc.view.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logger.MabisLogger;
import settings.GlobalPaths;

public class PathExplorer extends JPanel {
	private static final long serialVersionUID = 7450996024285798367L;
	private JTextField pathField;
	private JButton pathAcceptButton;
	private JButton openFileChooserButton;
	private ActionListener listener;
	private JComboBox<Object> typeOfPath;

	public PathExplorer() {
		this.initComponents();
		this.layoutComponents();
	}

	private void layoutComponents() {
		this.add(typeOfPath);
		this.add(this.pathField);
		this.add(this.pathAcceptButton);
		this.add(openFileChooserButton);

		GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);

		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);

		gl.setHorizontalGroup(gl
				.createSequentialGroup()
				.addComponent(this.typeOfPath, 100, 150, 160)
				.addComponent(this.pathField, 200, GroupLayout.DEFAULT_SIZE,
						Integer.MAX_VALUE)
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.openFileChooserButton)
								.addComponent(this.pathAcceptButton)));
		gl.setVerticalGroup(gl
				.createParallelGroup()
				.addComponent(this.typeOfPath, 55, 55, 55)
				.addComponent(this.pathField, 55, 55, 55)
				.addGroup(
						gl.createSequentialGroup()
								.addComponent(this.openFileChooserButton)
								.addComponent(this.pathAcceptButton)));
	}

	private void initComponents() {
		this.typeOfPath = new JComboBox<>();
		for (GlobalPaths p : GlobalPaths.values()) {
			this.typeOfPath.addItem(p.name());
		}
		this.typeOfPath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String si = (String) typeOfPath.getSelectedItem();
				pathField.setText(GlobalPaths.valueOf(si).toString());
			}
		});

		this.pathField = new JTextField(
				GlobalPaths.AUTHOR_CACHE_PATH.toString());

		this.pathAcceptButton = new JButton("Apply");
		this.openFileChooserButton = new JButton("Open");

		this.listener = new SettingsPathExplorerListener();
		this.pathAcceptButton.addActionListener(this.listener);
		this.openFileChooserButton.addActionListener(this.listener);
	}

	private class SettingsPathExplorerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			if (source.equals(pathAcceptButton)) {
				String pathContent = pathField.getText();
				if (pathContent.length() > 0) {
					GlobalPaths.valueOf((String) typeOfPath.getSelectedItem())
							.setPath(pathContent);
					Object params[] = {typeOfPath.getSelectedItem(),
							pathContent};
					MabisLogger.getLogger().log(Level.INFO,
							"{0} changed to {1}", params);
				}
			} else if (source.equals(openFileChooserButton)) {
				JFileChooser jfc = new JFileChooser("./");
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = jfc.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					pathField.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		}
	}
}