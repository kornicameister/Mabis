package view.items.minipanels;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import logger.MabisLogger;
import model.entity.Author;
import model.enums.AuthorType;
import settings.GlobalPaths;

public class AuthorMiniPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 3416144336071217011L;
	protected ArrayList<Author> authors = new ArrayList<>();

	protected final JButton newAuthorButton = new JButton("N");
	protected final JButton selectAuthorButton = new JButton("S");

	protected JTable table;
	protected DefaultTableModel tableModel;
	private JScrollPane scrollForTable;
	protected TreeMap<Integer, Author> rowToAuthor = new TreeMap<>();
	protected AuthorType type;
	private int currentlySelectedRow = -1;

	public AuthorMiniPanel(TreeSet<Author> authors, AuthorType type) {
		this.authors.addAll(authors);
		this.type = type;
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

		this.initTable();
		this.scrollForTable = new JScrollPane(this.table);
	}

	protected void initTable() {
		String columnNames[] = { "ID", "First Name", "Last Name" };
		this.tableModel = new DefaultTableModel(columnNames, 0);
		this.table = new JTable(tableModel){
			private static final long serialVersionUID = 6303631988571439208L;

			@Override
			public Class<?> getColumnClass(int column) {
				if(column == 0){
					return ImageIcon.class;
				}
				return Object.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		class TableMouseListener extends MouseAdapter implements MouseListener{
			@Override
			public void mouseClicked(MouseEvent e) {
				currentlySelectedRow = table.rowAtPoint(e.getPoint());
			}
		}
		
		class TableKeyListener extends KeyAdapter implements KeyListener{
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyChar() == KeyEvent.VK_DELETE){
					tableModel.removeRow(currentlySelectedRow);
					rowToAuthor.remove(new Integer(currentlySelectedRow));
					table.revalidate();
					currentlySelectedRow = -1;
				}
			}
		}
		this.table.addKeyListener(new TableKeyListener());
		this.table.addMouseListener(new TableMouseListener());
	}

	public void addRow(Author a) {
		Object data[] = { this.rowToAuthor.size() + 1, a.getFirstName(), a.getLastName() };
		if(a.getPrimaryKey() < 0){
			ImageIcon tmp = new ImageIcon(GlobalPaths.CROSS_SIGN.toString());
			data[0] = new ImageIcon(tmp.getImage().getScaledInstance(10, 10, Image.SCALE_FAST));
		}else{
			ImageIcon tmp = new ImageIcon(GlobalPaths.OK_SIGN.toString());
			data[0] = new ImageIcon(tmp.getImage().getScaledInstance(10, 10, Image.SCALE_FAST));
		}
		this.tableModel.addRow(data);
		this.rowToAuthor.put(this.rowToAuthor.size(),a);
	}

	public void setAuthors(TreeSet<Author> authors) {
		this.authors.clear();
		this.clearTable();
		for (Author a : authors) {
			this.authors.add(a);
			this.addRow(a);
		}
	}

	private void clearTable() {
		for (int i = tableModel.getRowCount(); i > 0; i--) {
			this.tableModel.removeRow(i);
		}
		this.table.revalidate();
	}

	public ArrayList<Author> getDatabaseAuthors() {
		return authors;
	}
	
	public Collection<Author> getAuthors(){
		return this.rowToAuthor.values();
	}

	private void layoutComponents() {
		GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);

		gl.setHorizontalGroup(gl
				.createSequentialGroup()
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.newAuthorButton, 50, 50, 50)
								.addComponent(this.selectAuthorButton, 50, 50,
										50)).addGap(5)
				.addComponent(this.scrollForTable));
		gl.setVerticalGroup(gl
				.createParallelGroup()
				.addGroup(
						gl.createSequentialGroup()
								.addComponent(this.newAuthorButton)
								.addComponent(this.selectAuthorButton))
				.addComponent(this.scrollForTable, 90, 90, 90));

		this.table.getColumnModel().getColumn(0).setMaxWidth(40);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		Comparator<Author> comparator = new Comparator<Author>() {
			@Override
			public int compare(Author o1, Author o2) {
				int result = o1.getLastName().compareTo(o2.getLastName());
				if (result == 0) {
					result = o1.getFirstName().compareTo(o2.getFirstName());
				}
				return result;
			}
		};
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
				Author tmp = new Author(firstName, lastName, this.type);
				int index = Collections.binarySearch(this.authors, tmp, comparator);
				if (index > 0) {
					tmp = this.authors.get(index);
				}
				this.addRow(tmp);
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
				this.addRow(tmp);
			}
		}
		MabisLogger.getLogger().log(Level.INFO,
				"Action called by clicking at {0}", source.getName());
	}

	public void clear() {
		this.clearTable();
		this.authors.clear();
		this.rowToAuthor.clear();
	}

}
