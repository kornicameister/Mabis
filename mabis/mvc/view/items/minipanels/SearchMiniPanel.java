package mvc.view.items.minipanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import mvc.view.MabisFrameInterface;

public class SearchMiniPanel extends JPanel implements MabisFrameInterface {
	private static final long serialVersionUID = 4346017525316071088L;
	private JTextField searchQuery;
	private JButton searchButton, cancelButton;
	private JProgressBar searchProgressBar;
	private JComboBox<String> criteria;
	private String[] comboBoxContent;

	public SearchMiniPanel() {
		this.comboBoxContent = new String[1];
		this.initComponents();
		this.layoutComponents();
	}

	@Override
	public void initComponents() {
		this.searchButton = new JButton("Search");
		this.cancelButton = new JButton("Cancel");
		this.searchQuery = new JTextField();
		this.searchProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		this.searchProgressBar.setMinimum(0);
		this.searchProgressBar.setMaximum(100);
		this.searchProgressBar.setStringPainted(true);
		this.criteria = new JComboBox<>(this.comboBoxContent);

		this.searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				firePropertyChange("query", criteria.getSelectedItem(),
						searchQuery.getText());
			}
		});

		this.cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				firePropertyChange("cancelSearch", false, true);
			}
		});
	}

	@Override
	public void layoutComponents() {
		GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);

		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);

		gl.setHorizontalGroup(gl
				.createParallelGroup()
				.addGroup(
						gl.createSequentialGroup()
								.addComponent(this.searchQuery, 70, 90, 130)
								.addComponent(this.criteria)
								.addComponent(this.searchButton)
								.addComponent(this.cancelButton))
				.addComponent(this.searchProgressBar));

		gl.setVerticalGroup(gl
				.createSequentialGroup()
				.addGroup(
						gl.createParallelGroup().addComponent(this.searchQuery)
								.addComponent(this.criteria)
								.addComponent(this.searchButton, 30, 30, 30)
								.addComponent(this.cancelButton, 30, 30, 30))
				.addComponent(this.searchProgressBar, 30, 30, 30));
	}

	public String[] getComboBoxContent() {
		return comboBoxContent;
	}

	public void setComboBoxContent(String[] comboBoxContent) {
		this.comboBoxContent = comboBoxContent;
		this.criteria.removeAllItems();
		for (String s : comboBoxContent) {
			this.criteria.addItem(s);
		}
		this.criteria.setSelectedIndex(0);
	}

	public JProgressBar getProgressBar() {
		return this.searchProgressBar;
	}

}
