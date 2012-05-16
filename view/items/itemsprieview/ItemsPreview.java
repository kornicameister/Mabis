package view.items.itemsprieview;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.BaseTable;

/**
 * Klasa jest okienkiem w którym umieszczane są elementy kolekcji. Służy jako
 * podgląd dla elementów pobranych przez API, bądź jeśli zajdzie inna potrzeba,
 * 
 * @author kornicameister
 * 
 */
public abstract class ItemsPreview extends JFrame {
	private static final long serialVersionUID = -5983748388561797286L;
	protected final TreeSet<BaseTable> elements;
	private JScrollPane scrollPane;
	protected JPanel contentPanel;

	public ItemsPreview(String title, TreeSet<BaseTable> elements) {
		super(title);
		this.elements = elements;

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(630, 400));

		// init of Preview dialog
		this.initComponents();
	}

	/**
	 * Inicjalizacja składowych tego okienka, tj. {@link ItemsPreview}
	 */
	protected void initComponents() {
		this.contentPanel = new JPanel(true);
		this.contentPanel.setLayout(new GridLayout(0, 3));
		this.scrollPane = new JScrollPane(this.contentPanel);
		this.add(this.scrollPane);
	}

	/**
	 * @return TreeSet elementów, które były wyświetlone w podglądzie
	 */
	public TreeSet<BaseTable> getElements() {
		return elements;
	}
}
