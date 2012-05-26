package view.items.minipanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logger.MabisLogger;
import model.entity.Author;

public class AuthorMiniPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 3416144336071217011L;
	private ArrayList<Author> authors = new ArrayList<>();
	protected final JButton newAuthorButton = new JButton("N");
	protected final JButton selectAuthorButton = new JButton("S");
	protected JComponent authorsBox = new JComboBox<String>();

	public AuthorMiniPanel(String string, TreeSet<Author> treeSet) {
		if (treeSet != null) {
			this.authors.addAll(treeSet);
		}
		this.setBorder(BorderFactory.createTitledBorder(string));
		this.initComponents();
		this.layoutComponents();
	}

	protected void initComponents() {
		this.selectAuthorButton.setToolTipText("Select author");
		this.selectAuthorButton.setName("Select author");
		this.newAuthorButton.setToolTipText("Create new author");
		this.newAuthorButton.setName("Create new author");

		this.newAuthorButton.addActionListener(this);
		this.selectAuthorButton.addActionListener(this);
	}

	@SuppressWarnings("unchecked")
	public void setAuthors(TreeSet<Author> authors){
		this.authors.clear();
		this.authors.addAll(authors);
		for(Author a : this.authors){
			((JComboBox<String>)this.authorsBox).addItem(a.toString());
		}
	}
	
	public ArrayList<Author> getAuthors() {
		return authors;
	}
	
	@SuppressWarnings("unchecked")
	public JComboBox<String> getAuthorsBox() {
		return (JComboBox<String>) authorsBox;
	}

	private void layoutComponents() {
		JPanel tmp = new JPanel();
		GroupLayout gl = new GroupLayout(tmp);
		tmp.setLayout(gl);

		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addComponent(this.authorsBox, 140, 140, 140)
				.addComponent(this.newAuthorButton, 50, 50, 50)
				.addComponent(this.selectAuthorButton, 50, 50, 50));
		gl.setVerticalGroup(gl.createParallelGroup()
				.addComponent(this.authorsBox, 25, 25, 25)
				.addComponent(this.newAuthorButton)
				.addComponent(this.selectAuthorButton));
		this.add(tmp);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source.equals(newAuthorButton)) {
			String returned = JOptionPane.showInputDialog(null, "Input:",
					source.getName(), JOptionPane.PLAIN_MESSAGE);
			if (returned != null) {
				String parts[] = returned.split(" ");
				String firstName = parts[0];
				String lastName = new String();
				for (int i = 1; i < parts.length; i++) {
					lastName += parts[i];
					if (i < parts.length - 1) {
						lastName += " ";
					}
				}
				Author tmp = new Author(firstName, lastName);
				this.firePropertyChange("authorCreated", null, tmp);
				((JComboBox<String>) authorsBox).addItem(returned);
			}
		} else if (source.equals(selectAuthorButton)) {
			if (authors.size() == 0) {
				return;
			}
			Object arr[] = authors.toArray();
			Object returned = JOptionPane.showInputDialog(source,
					"Select one from following box", source.getName(),
					JOptionPane.QUESTION_MESSAGE, null, arr, arr[0]);
			if (returned != null) {
				Author tmp = (Author) returned;
				((JComboBox<String>) authorsBox).addItem(tmp.getFirstName()
						+ " " + tmp.getLastName());
				this.firePropertyChange("authorSelected", null, tmp);
			}
		}
		MabisLogger.getLogger().log(Level.INFO,
				"Action called by clicking at {0}", source.getName());
	}

	public void clear() {
		this.authors.clear();
		JComboBox<String> tmp = (JComboBox<String>) this.authorsBox;
		tmp.removeAllItems();
	}

}
