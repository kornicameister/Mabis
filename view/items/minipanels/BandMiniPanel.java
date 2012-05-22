package view.items.minipanels;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import logger.MabisLogger;
import model.entity.Band;

public class BandMiniPanel extends AuthorMiniPanel {
	private static final long serialVersionUID = -7769648241373563025L;
	private final ArrayList<Band> bands = new ArrayList<>();

	public BandMiniPanel(String string, TreeSet<Band> bands) {
		super(string, null);
		this.bands.addAll(bands);
	}

	@Override
	protected void initComponents() {
		this.selectAuthorButton.setToolTipText("Select band");
		this.selectAuthorButton.setName("Select one of the following band");
		this.selectAuthorButton.addActionListener(this);
		this.newAuthorButton.setToolTipText("Create new band");
		this.newAuthorButton.setName("Create new band");
		this.newAuthorButton.addActionListener(this);
		this.authorsBox = new JTextField();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source.equals(newAuthorButton)) {
			String returned = JOptionPane.showInputDialog(null, "Input:",
					source.getName(), JOptionPane.PLAIN_MESSAGE);
			if (returned != null) {
				Band tmp = new Band(returned);
				this.firePropertyChange("bandCreated", null, tmp);
				((JTextField)authorsBox).setText(returned);
			}
		} else if (source.equals(selectAuthorButton)) {
			if (bands.size() == 0) {
				return;
			}
			Object arr[] = bands.toArray();
			Object returned = JOptionPane.showInputDialog(source,
					"Select one from following box", source.getName(),
					JOptionPane.QUESTION_MESSAGE, null, arr, arr[0]);
			if (returned != null) {
				Band tmp = (Band) returned;
				this.firePropertyChange("bandSelected", null, tmp);
			}
		}
		MabisLogger.getLogger().log(Level.INFO,
				"Action called by clicking at {0}", source.getName());
	}

	public JComboBox<String> getBandsBox() {
		return this.getAuthorsBox();
	}

	public void clear() {
		((JTextField)authorsBox).setText("");
	}
}
