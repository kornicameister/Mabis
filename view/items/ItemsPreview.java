package view.items;

import java.util.TreeSet;

import javax.swing.JFrame;
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
	private DefaultTableModel tableModel;

	public ItemsPreview(String title, TreeSet<BaseTable> elements) {
		super(title);
		this.elements = elements;

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
		Object columnIDS[] = ((BaseTable) this.elements.toArray()[0]).toColumnIdentifiers();
		this.tableModel = new DefaultTableModel();
		this.tableModel.setColumnIdentifiers(columnIDS);
		for (BaseTable bt : this.elements) {
			this.tableModel.addRow(bt.toRowData());
		}

		this.table = new JTable(this.tableModel);
	}

	/**
	 * @return TreeSet elementów, które były wyświetlone w podglądzie
	 */
	public TreeSet<BaseTable> getElements() {
		return elements;
	}
}
