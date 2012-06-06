package mvc.view.items.minipanels;

import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mvc.model.entity.Book;
import mvc.model.enums.BookIndustryIdentifierType;
import mvc.model.utilities.BookIndustryIdentifier;
import settings.GlobalPaths;

/**
 * Klasa, ktora pozwala na dodawanie kolejnych numerow producenta. Chodzi tutaj
 * glownie o ksiazki, ale nie wykluczone jest uzycie tego komponentu do innych
 * celow
 * 
 * @author tomasz
 * @see BookIndustryIdentifier
 * @see Book
 */
public class IndustryIdentifiersMiniPanel extends JPanel {
	private static final long serialVersionUID = -3958286587031427043L;
	private JTable iiTable;
	private DefaultTableModel iiModel;
	private TreeMap<Integer, BookIndustryIdentifier> rowToBii = new TreeMap<>();
	private int currentlySelectedRow = -1;
	private JScrollPane scrollForTable;
	private JComboBox<BookIndustryIdentifierType> biiCombo;

	/**
	 * Tworzy nowy {@link IndustryIdentifiersMiniPanel}
	 */
	public IndustryIdentifiersMiniPanel() {
		this.initializeTable();
		this.initComponents();
		this.layoutComponents();
	}

	private void initComponents() {
		this.scrollForTable = new JScrollPane(this.iiTable);

		class ScrollListener extends MouseAdapter implements MouseListener {

			@Override
			public void mouseClicked(MouseEvent e) {
				addRow(new BookIndustryIdentifier(
						BookIndustryIdentifierType.OTHER, ""));
			}

		}

		this.scrollForTable.addMouseListener(new ScrollListener());
	}

	private void layoutComponents() {
		GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);

		gl.setHorizontalGroup(gl.createSequentialGroup().addGroup(
				gl.createParallelGroup().addComponent(this.scrollForTable)));
		gl.setVerticalGroup(gl.createParallelGroup().addGroup(
				gl.createSequentialGroup().addComponent(this.scrollForTable,
						90, 90, 90)));

		this.iiTable.getColumnModel().getColumn(0).setMaxWidth(40);
	}

	/**
	 * Inicjalizacja tabeli dla tego panelu
	 */
	private void initializeTable() {
		String columnNames[] = {"LP", "Number", "Type"};
		this.iiModel = new DefaultTableModel(columnNames, 0);
		this.iiTable = new JTable(this.iiModel) {
			private static final long serialVersionUID = 7141622026027712315L;
			@Override
			public Class<?> getColumnClass(int column) {
				if (column == 0) {
					return ImageIcon.class;
				} else if (column == 1) {
					return String.class;
				} else if (column == 2) {
					return JComboBox.class;
				}
				return Object.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column > 0) {
					return true;
				}
				return false;
			}
		};

		class TableMouseListener extends MouseAdapter implements MouseListener {
			@Override
			public void mouseClicked(MouseEvent e) {
				currentlySelectedRow = iiTable.rowAtPoint(e.getPoint());
			}
		}

		/**
		 * Listener dla tabeli, dzialajacy w nastepujacy sposob:
		 * <ul>
		 * <li>klawisz delete -> usuwa wiersz</li>
		 * <li>klawisz <b>Enter</b> -> zatwierdza nowy wiersz</li>
		 * </ul>
		 * 
		 * @author tomasz
		 * 
		 */
		class TableKeyListener extends KeyAdapter implements KeyListener {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_D
						|| e.getKeyChar() == KeyEvent.VK_DELETE) {
					iiModel.removeRow(currentlySelectedRow);
					rowToBii.remove(new Integer(currentlySelectedRow));
					iiTable.revalidate();
					currentlySelectedRow = -1;
				} else if (e.getKeyChar() == '\n') {
					String v1 = (String) iiModel.getValueAt(
							iiModel.getRowCount() - 1, 1);
					BookIndustryIdentifierType v2 = (BookIndustryIdentifierType) iiModel
							.getValueAt(iiModel.getRowCount() - 1, 2);
					rowToBii.put(iiModel.getRowCount() - 1,
							new BookIndustryIdentifier(v2, v1));
				}
			}
		}

		this.iiTable.addKeyListener(new TableKeyListener());
		this.iiTable.addMouseListener(new TableMouseListener());

		this.biiCombo = new JComboBox<BookIndustryIdentifierType>();
		for (BookIndustryIdentifierType bii : BookIndustryIdentifierType
				.values()) {
			this.biiCombo.addItem(bii);
		}

		this.iiTable.getColumnModel().getColumn(2)
				.setCellEditor(new DefaultCellEditor(this.biiCombo));
	}

	/**
	 * Dodaje nowy wiersz do tabeli
	 * 
	 * @param bii
	 */
	public void addRow(BookIndustryIdentifier bii) {
		Object data[] = {null, bii.getValue(), bii.getType()};
		ImageIcon tmp = new ImageIcon(GlobalPaths.ISBN_SIGN.toString());
		data[0] = new ImageIcon(tmp.getImage().getScaledInstance(10, 10,
				Image.SCALE_FAST));
		this.iiModel.addRow(data);
		this.rowToBii.put(this.rowToBii.size(), bii);
	}

	/**
	 * Ustawia listę {@link BookIndustryIdentifier} dla tego mini-panelu
	 * 
	 * @param biis
	 *            lista numerow producenta
	 */
	public void setIIS(TreeSet<BookIndustryIdentifier> biis) {
		this.rowToBii.clear();

		this.clearTable();
		for (BookIndustryIdentifier bii : biis) {
			this.addRow(bii);
		}
	}

	/**
	 * Wyczyszczenie tabeli
	 */
	public void clearTable() {
		for (int i = this.iiModel.getRowCount(); i != 0; i--) {
			this.iiModel.removeRow(i);
		}
		this.iiTable.revalidate();
	}

	public Collection<BookIndustryIdentifier> getII() {
		return this.rowToBii.values();
	}
}
