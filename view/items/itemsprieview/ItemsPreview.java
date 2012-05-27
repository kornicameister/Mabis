package view.items.itemsprieview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import logger.MabisLogger;
import model.BaseTable;

/**
 * Klasa jest okienkiem w którym umieszczane są elementy kolekcji. Służy jako
 * podgląd dla elementów pobranych przez API, bądź jeśli zajdzie inna potrzeba,
 * 
 * @author kornicameister
 * 
 */
public class ItemsPreview extends JFrame implements ActionListener {
	private static final long serialVersionUID = -5983748388561797286L;
	protected final TreeSet<BaseTable> elements;
	protected JTabbedPane tabbedPanel;
	private JButton acceptSelectedButton;
	private JButton cancelButton;
	private static Dimension dim = new Dimension(620, 470);

	public ItemsPreview(String title, TreeSet<BaseTable> elements) {
		super(title);
		this.elements = elements;

		this.setSize(this.getMinimumSize());
		setDefaultLookAndFeelDecorated(false);

		this.initComponents();
		this.layoutComponents();

		this.setMinimumSize(dim);
		this.setSize(dim);
//		this.setResizable(false);
	}

	private void layoutComponents() {
		this.setLayout(new BorderLayout());
		this.tabbedPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 380));
		this.add(this.tabbedPanel, BorderLayout.PAGE_START);
		this.acceptSelectedButton.setPreferredSize(new Dimension(Integer.MAX_VALUE / 2, 30));
		this.add(this.acceptSelectedButton, BorderLayout.CENTER);
		this.cancelButton.setPreferredSize(new Dimension(Integer.MAX_VALUE / 2,30));
		this.add(this.cancelButton, BorderLayout.PAGE_END);
	}

	/**
	 * Inicjalizacja składowych tego okienka, tj. {@link ItemsPreview}
	 */
	protected void initComponents() {
		this.tabbedPanel = new JTabbedPane(JTabbedPane.TOP);
		this.tabbedPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		for (BaseTable bs : this.elements) {
			try {
				this.tabbedPanel.addTab(bs.getTitle(), new JScrollPane(PreviewArbiter.determineTyp(bs)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.acceptSelectedButton = new JButton("Accept");
		this.acceptSelectedButton.addActionListener(this);
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(this);
	}

	/**
	 * @return TreeSet elementów, które były wyświetlone w podglądzie
	 */
	public TreeSet<BaseTable> getElements() {
		return elements;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source.equals(this.acceptSelectedButton)) {
			this.firePropertyChange(
					"selectedItem",
					null,
					((PreviewChunk) this.tabbedPanel.getSelectedComponent()).previedItem);
		}
		// no matter which one from two buttons was clicked, closing preview
		// anyway
		this.setVisible(false);
		this.dispose();
		MabisLogger.getLogger().log(Level.INFO,
				this.getClass().getSimpleName() + " action called :: {0}",
				source.getActionCommand());
	}
}
