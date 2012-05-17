package view.items.itemsprieview;

import java.awt.Dimension;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

import model.BaseTable;

/**
 * Klasa jest okienkiem w którym umieszczane są elementy kolekcji. Służy jako
 * podgląd dla elementów pobranych przez API, bądź jeśli zajdzie inna potrzeba,
 * 
 * @author kornicameister
 * 
 */
public abstract class ItemsPreview extends JFrame{
	private static final long serialVersionUID = -5983748388561797286L;
	protected final TreeSet<BaseTable> elements;
	protected JTabbedPane tabbedPanel;
	private JPanel contentPane;

	public ItemsPreview(String title, TreeSet<BaseTable> elements) {
		super(title);
		this.elements = elements;

		setDefaultLookAndFeelDecorated(true);
		this.setLocationRelativeTo(null);
		this.setSize(this.getMinimumSize());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.initComponents();
		this.layoutComponents();
		
		this.setMinimumSize(new Dimension(450,450));
		this.setSize(new Dimension(450,450));
	}

	private void layoutComponents() {
		GroupLayout gl = new GroupLayout(getContentPane());
		getContentPane().setLayout(gl);

		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);
		
		gl.setHorizontalGroup(gl.createParallelGroup().addComponent(this.tabbedPanel,400,400,400));
		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(this.tabbedPanel,400,400,400));
		this.pack();
	}

	/**
	 * Inicjalizacja składowych tego okienka, tj. {@link ItemsPreview}
	 */
	protected void initComponents() {
		this.contentPane = new JPanel(true);
		this.contentPane.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));
		this.setContentPane(this.contentPane);
		
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
