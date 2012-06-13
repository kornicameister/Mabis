package mvc.view.items.minipanels;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mvc.model.entity.Genre;
import mvc.model.enums.GenreType;
import settings.GlobalPaths;

/**
 * Klasa symuluje chmurę tagów. Tagi mogą być do niej dodawana pojedynczo lub
 * jako cała {@link ArrayList}. Wewnętrznie korzysta z {@link GenreMiniPanel},
 * który pozwala na wybiór i dodawania pojedynczych gatunków.
 * 
 * @author kornicameister
 * @see GenreMiniPanel
 * 
 */
public class TagCloudMiniPanel extends JPanel {
	private static final long serialVersionUID = -8170767178911147951L;
	private JTable tagsTable;
	private TreeMap<Integer, Genre> rowToGenre = new TreeMap<>();
	private DefaultTableModel tagsModel;
	private GenreMiniPanel gmp;
	private JScrollPane scrollForTable;
	private int currentlySelectedRow = -1;

	/**
	 * Metoda pozwala na utworzenie chmury tagów z już wstępnie zdefiniowaną ich
	 * listą
	 * 
	 * @param genres
	 *            lista tagow
	 * @param type
	 *            typ tagu
	 * @see GenreType
	 * @see Genre
	 */
	public TagCloudMiniPanel(TreeSet<Genre> genres, GenreType type) {
		gmp = new GenreMiniPanel(genres, type);
		this.addPropertyListener();
		this.initializeTagTable();
		this.layoutComponents();
	}

	/**
	 * Metoda inicjalizuje tabele tego minipanelu
	 */
	private void initializeTagTable() {
		String columnNames[] = {"ID", "Name"};
		this.tagsModel = new DefaultTableModel(columnNames, 0);
		this.tagsTable = new JTable(this.tagsModel) {
			private static final long serialVersionUID = 6303631988571439208L;

			@Override
			public Class<?> getColumnClass(int column) {
				if (column == 0) {
					return ImageIcon.class;
				}
				return Object.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};
		this.tagsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		class TableMouseListener extends MouseAdapter implements MouseListener {
			@Override
			public void mouseClicked(MouseEvent e) {
				currentlySelectedRow = tagsTable.rowAtPoint(e.getPoint());
			}
		}

		class TableKeyListener extends KeyAdapter implements KeyListener {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_D
						|| e.getKeyChar() == KeyEvent.VK_DELETE) {
					tagsModel.removeRow(currentlySelectedRow);
					rowToGenre.remove(new Integer(currentlySelectedRow));
					tagsTable.revalidate();
					currentlySelectedRow = -1;
				}
			}
		}
		this.tagsTable.addKeyListener(new TableKeyListener());
		this.tagsTable.addMouseListener(new TableMouseListener());
	}

	/**
	 * Klasa ta korzysta ze starego modelu obslugi mini panelu, dlatego tez
	 * nasluchuje sygnalow pochodzacych z zagniezdzonej klasy
	 * {@link TagCloudMiniPanel}. Chodzi tutaj o {@link GenreMiniPanel}
	 */
	private void addPropertyListener() {
		this.gmp.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent e) {
				String property = e.getPropertyName();
				if (property.equals("genreSelected")
						|| property.equals("genreCreated")) {
					addRow((Genre) e.getNewValue());
				}
			}
		});
	}

	private void layoutComponents() {
		this.scrollForTable = new JScrollPane(this.tagsTable);

		GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);

		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addComponent(this.gmp, 60, 60, 60).addGap(5)
				.addComponent(this.scrollForTable));

		gl.setVerticalGroup(gl.createParallelGroup().addComponent(this.gmp)
				.addComponent(this.scrollForTable, 80, 100, Short.MAX_VALUE));

		this.tagsTable.getColumnModel().getColumn(0).setMaxWidth(40);
	}

	/**
	 * Metoda pozwala na dodanie nowego elementu do chumry tagow
	 * 
	 * @param g
	 *            nowy gatunek
	 */
	public void addRow(Genre g) {
		Object data[] = {null, g.getGenre()};
		if (g.getPrimaryKey() < 0) {
			ImageIcon tmp = new ImageIcon(GlobalPaths.CROSS_SIGN.toString());
			data[0] = new ImageIcon(tmp.getImage().getScaledInstance(10, 10,
					Image.SCALE_FAST));
		} else {
			ImageIcon tmp = new ImageIcon(GlobalPaths.OK_SIGN.toString());
			data[0] = new ImageIcon(tmp.getImage().getScaledInstance(10, 10,
					Image.SCALE_FAST));
		}
		this.tagsModel.addRow(data);
		this.rowToGenre.put(this.rowToGenre.size(), g);
	}

	public void setTags(Collection<Genre> genres) {
		gmp.tags.clear();
		for (Genre g : genres) {
			gmp.tags.add(g);
			this.addRow(g);
		}
		this.tagsTable.revalidate();
	}

	public void clear() {
		gmp.tags.clear();
		this.clearTable();
		this.rowToGenre.clear();
	}

	private void clearTable() {
		for (int i = tagsModel.getRowCount(); i > 0; i--) {
			this.tagsModel.removeRow(i);
		}
		this.tagsTable.revalidate();
	}

	/**
	 * Lista gatunkow, ktore pochodzaca z bazy danych i nie zostaly zdefiniowany
	 * albo poprzez pobranie nowego elementu kolekcji poprzez API lub dodane
	 * przez uzytkownika
	 * 
	 * @return array liste gatunkow z bazy danych
	 */
	public ArrayList<Genre> getDatabaseTags() {
		return gmp.tags;
	}

	/**
	 * Zwraca liczbe gatunkow, ktore zostaly zatwierdzone jako gatunki opisujace
	 * ten element kolekcji
	 * 
	 * @return kolekcje gatunkow dal danego elementu kolekcji
	 */
	public Collection<Genre> getTags() {
		return rowToGenre.values();
	}

	/**
	 * Wewnętrzna klasa, definiuje funkcjonalność dodawania nowego gatunku bądź
	 * wybieranie gatunków z tych już istniejących. Nie pozwala na dodanie
	 * gatunku takiego, który co do nazwy jest identyczny z gatunkiem już
	 * istniejącym.
	 * 
	 * @author kornicameister
	 * 
	 */
	private class GenreMiniPanel extends JPanel implements ActionListener {
		private static final long serialVersionUID = -9003039695215128758L;
		private JButton newGenreButton = new JButton("N");
		private JButton selectGenreButton = new JButton("S");
		private GenreType type;

		final ArrayList<Genre> tags = new ArrayList<>();

		public GenreMiniPanel(TreeSet<Genre> treeSet, GenreType type) {
			this.tags.addAll(treeSet);
			this.initComponents();
			this.layoutComponents();
			this.type = type;
		}

		private void layoutComponents() {
			JPanel tmp = new JPanel();
			GroupLayout gl = new GroupLayout(tmp);
			tmp.setLayout(gl);

			gl.setHorizontalGroup(gl.createParallelGroup()
					.addComponent(this.newGenreButton, 50, 50, 50)
					.addComponent(this.selectGenreButton, 50, 50, 50));
			gl.setVerticalGroup(gl.createSequentialGroup()
					.addComponent(this.newGenreButton)
					.addComponent(this.selectGenreButton));
			this.add(tmp);
		}

		private void initComponents() {
			this.newGenreButton.setToolTipText("Create new genre");
			this.newGenreButton.setName("Create new genre");
			this.newGenreButton.addActionListener(this);
			this.selectGenreButton.setToolTipText("Select genre");
			this.selectGenreButton.setName("Select genre");
			this.selectGenreButton.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();

			class GenreComparator implements Comparator<Genre> {
				@Override
				public int compare(Genre o1, Genre o2) {
					return o1.getGenre().compareTo(o2.getGenre());
				}
			}
			Collections.sort(this.tags, new GenreComparator());

			if (source.equals(newGenreButton)) {
				String returned = JOptionPane.showInputDialog(null, "Input:",
						source.getName(), JOptionPane.PLAIN_MESSAGE);
				if (returned != null) {
					Genre tmp = new Genre(returned, this.type);
					int found = Collections.binarySearch(this.tags, tmp,
							new GenreComparator());
					if (found < 0) {
						this.firePropertyChange("genreCreated", null, tmp);
					}
					this.firePropertyChange("tag", null, tmp);
				}
			} else if (source.equals(selectGenreButton)) {
				if (tags.size() == 0) {
					return;
				}
				Object arr[] = tags.toArray();
				Object returned = JOptionPane.showInputDialog(source,
						"Select one from following box", source.getName(),
						JOptionPane.QUESTION_MESSAGE, null, arr, arr[0]);
				if (returned != null) {
					Genre tmp = (Genre) returned;
					this.firePropertyChange("genreSelected", null, tmp);
					this.firePropertyChange("tag", null, tmp);
				}
			}
		}
	}
}
