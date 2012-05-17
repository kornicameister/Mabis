package view.items.itemsprieview;

import java.awt.Dimension;
import java.util.TreeSet;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import model.BaseTable;

/**
 * Klasa jest okienkiem w którym umieszczane są elementy kolekcji. Służy jako
 * podgląd dla elementów pobranych przez API, bądź jeśli zajdzie inna potrzeba,
 * 
 * @author kornicameister
 * 
 */
public abstract class ItemsPreview extends JDialog {
	private static final long serialVersionUID = -5983748388561797286L;
	protected final TreeSet<BaseTable> elements;
	protected JTabbedPane tabbedPanel;
	
	public ItemsPreview(String title, TreeSet<BaseTable> elements) {
		super();
		this.elements = elements;

		this.setSize(this.getMinimumSize());
		setDefaultLookAndFeelDecorated(false);
		
		this.initComponents();
		this.layoutComponents();
		
		this.setMinimumSize(new Dimension(450,450));
		this.setSize(new Dimension(450,450));
	}

	private void layoutComponents() {
		this.add(this.tabbedPanel);
	}

	/**
	 * Inicjalizacja składowych tego okienka, tj. {@link ItemsPreview}
	 */
	protected void initComponents() {
		this.tabbedPanel = new JTabbedPane(JTabbedPane.TOP);
		this.tabbedPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	/**
	 * @return TreeSet elementów, które były wyświetlone w podglądzie
	 */
	public TreeSet<BaseTable> getElements() {
		return elements;
	}
}
