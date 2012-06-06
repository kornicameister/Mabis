package mvc.view.items.itemsprieview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import logger.MabisLogger;
import mvc.model.BaseTable;
import mvc.model.entity.Movie;
import mvc.view.MabisFrameInterface;
import mvc.view.WindowClosedListener;
import settings.io.SettingsException;
import settings.io.SettingsLoader;

/**
 * Klasa jest okienkiem w którym umieszczane są elementy kolekcji. Służy jako
 * podgląd dla elementów pobranych przez API, bądź jeśli zajdzie inna potrzeba,
 * 
 * @author kornicameister
 * 
 */
public class ItemsPreview extends JFrame
		implements
			ActionListener,
			MabisFrameInterface {
	private static final long serialVersionUID = -5983748388561797286L;
	protected final TreeSet<BaseTable> elements;
	protected JTabbedPane tabbedPanel;
	private JButton acceptSelectedButton;
	private JButton cancelButton;
	private static Dimension dim = new Dimension(620, 470);

	/**
	 * Konstruuje poodglad elementow kolekcji. Elementy, ktore znajda sie na
	 * podgladzie to te znajdujace sie w kolekcji opisywanej jako
	 * <i>elements</i>
	 * 
	 * @param title
	 *            tytul okna
	 * @param elements
	 *            elementy do wyswietlenia
	 */
	public ItemsPreview(String title, TreeSet<BaseTable> elements) {
		super();
		this.elements = elements;
		this.initComponents();
		this.layoutComponents();
		this.addWindowListener(new WindowClosedListener());

		try {
			SettingsLoader.load(this);
		} catch (SettingsException e) {
			e.printStackTrace();
			this.setSize(this.getMinimumSize());
			this.setMinimumSize(dim);
			this.setSize(dim);
		}
		this.setTitle(title);
	}

	/**
	 * Dzialadnie podobne do {@link ItemsPreview#ItemsPreview(String, TreeSet)}.
	 * Roznica polega na tym, ze ten drugi dziala na calej kolekcji obiektow, a
	 * nie na jednym z nich
	 * 
	 * @param title
	 * @param bt
	 */
	public ItemsPreview(String title, BaseTable bt) {
		super();
		this.elements = new TreeSet<>();
		this.elements.add(bt);

		this.initComponents();
		this.layoutComponents();
		this.addWindowListener(new WindowClosedListener());

		try {
			SettingsLoader.load(this);
		} catch (SettingsException e) {
			this.setSize(this.getMinimumSize());
			this.setMinimumSize(dim);
			this.setSize(dim);
		}
		this.acceptSelectedButton.setVisible(false);
		this.setTitle(title);
	}

	@Override
	public void layoutComponents() {

		GroupLayout gl = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(gl);

		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);

		gl.setHorizontalGroup(gl
				.createParallelGroup()
				.addComponent(this.tabbedPanel)
				.addGroup(
						gl.createSequentialGroup().addGap(50)
								.addComponent(this.acceptSelectedButton)
								.addComponent(this.cancelButton)));
		gl.setVerticalGroup(gl
				.createSequentialGroup()
				.addComponent(this.tabbedPanel)
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.acceptSelectedButton, 40,
										40, 40)
								.addComponent(this.cancelButton, 40, 40, 40)));
	}

	@Override
	public void initComponents() {
		this.tabbedPanel = new JTabbedPane(JTabbedPane.BOTTOM);
		this.tabbedPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		for (BaseTable bs : this.elements) {
			try {
				this.tabbedPanel.addTab(bs.getTitle(), new JScrollPane(
						new PreviewChunk((Movie) bs)));
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
			JScrollPane selected = (JScrollPane) this.tabbedPanel
					.getSelectedComponent();
			PreviewChunk chunk = (PreviewChunk) selected.getViewport()
					.getView();
			this.firePropertyChange("selectedItem", null, chunk.previedItem);
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
