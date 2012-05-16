package view.items;

import java.awt.Dimension;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.BaseTable;

/**
 * Klasa jest okienkiem w którym umieszczane są elementy kolekcji. Służy jako
 * podgląd dla elementów pobranych przez API, bądź jeśli zajdzie inna potrzeba,
 * 
 * @author kornicameister
 * 
 */
public class ItemsPreview extends JFrame {
	private static final long serialVersionUID = -5983748388561797286L;
	private final TreeSet<BaseTable> elements;
	private JTable table;
	private JScrollPane scrollPane;

	public ItemsPreview(String title, TreeSet<BaseTable> elements) {
		super(title);
		this.elements = elements;

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(600,400));
		
		// init of Preview dialog
		this.initComponents();
		this.layoutComponents();
	}

	/**
	 * Metoda tworzy layout {@link ItemsPreview}
	 */
	private void layoutComponents() {

	}

	/**
	 * Inicjalizacja składowych tego okienka, tj. {@link ItemsPreview}
	 */
	private void initComponents() {
		this.table = new JTable(initTableModel());
		this.scrollPane = new JScrollPane(this.table);
		this.add(this.scrollPane);
	}

	/**
	 * Inicjalizuje TableModel dla {@link ItemsPreview#table}
	 * 
	 * @return gotowy table model zawierający dane
	 */
	private DefaultTableModel initTableModel() {
		DefaultTableModel tableModel = new DefaultTableModel();
		Object columnIDS[] = ((BaseTable) this.elements.toArray()[0])
				.toColumnIdentifiers();
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columnIDS);
		for (BaseTable bt : this.elements) {
			tableModel.addRow(bt.toRowData());
		}
		return tableModel;
	}

	/**
	 * @return TreeSet elementów, które były wyświetlone w podglądzie
	 */
	public TreeSet<BaseTable> getElements() {
		return elements;
	}
}
