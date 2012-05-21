package view.items;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.entity.Genre;

public class GenreMiniPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -9003039695215128758L;
	private final ArrayList<Genre> genres = new ArrayList<>();
	private final JComboBox<String> genreBox = new JComboBox<>();
	private JButton newGenreButton = new JButton("N");
	private JButton selectGenreButton = new JButton("S");

	public GenreMiniPanel(String string, TreeSet<Genre> treeSet) {
		this.genres.addAll(treeSet);
		this.setBorder(BorderFactory.createTitledBorder(string));
		this.initComponents();
		this.layoutComponents();
	}

	private void layoutComponents() {
		JPanel tmp = new JPanel();
		GroupLayout gl = new GroupLayout(tmp);
		tmp.setLayout(gl);

		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addComponent(this.genreBox, 140, 140, 140)
				.addComponent(this.newGenreButton, 50, 50, 50)
				.addComponent(this.selectGenreButton, 50, 50, 50));
		gl.setVerticalGroup(gl.createParallelGroup()
				.addComponent(this.genreBox, 25, 25, 25)
				.addComponent(this.newGenreButton)
				.addComponent(this.selectGenreButton));
		this.add(tmp);
	}

	private void initComponents() {
		this.genreBox.setName("genreBox");
		this.newGenreButton.setToolTipText("Create new genre");
		this.newGenreButton.setName("Create new genre");
		this.newGenreButton.addActionListener(this);
		this.selectGenreButton.setToolTipText("Select genre");
		this.selectGenreButton.setName("Select genre");
		this.selectGenreButton.addActionListener(this);
	}
	public JComboBox<String> getGenreBox() {
		return genreBox;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source.equals(newGenreButton)) {
			String returned = JOptionPane.showInputDialog(null,
					"Input:", source.getName(),
					JOptionPane.PLAIN_MESSAGE);
			if (returned != null) {
				Genre tmp = new Genre(returned);
				genreBox.addItem(returned);
				this.firePropertyChange("genreCreated", null, tmp);
			}
		} else if (source.equals(selectGenreButton)) {
			if (genres.size() == 0) {
				return;
			}
			Object arr[] = genres.toArray();
			Object returned = JOptionPane.showInputDialog(source,
					"Select one from following box", source.getName(),
					JOptionPane.QUESTION_MESSAGE, null, arr, arr[0]);
			if (returned != null) {
				Genre tmp = (Genre) returned;
				genreBox.addItem(tmp.getGenre());
				this.firePropertyChange("genreSelected", null, tmp);
			}
		}
	}

}
