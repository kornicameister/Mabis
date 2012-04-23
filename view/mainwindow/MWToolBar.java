/**
 *
 */
package view.mainwindow;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import logger.MabisLogger;
import view.enums.CollectionView;

/**
 * @author kornicameister
 * 
 */
public class MWToolBar extends JToolBar implements ActionListener {
	private static final long serialVersionUID = 3148020721875579266L;
	private JComboBox<String> groupBy, dbContent, zoomContent, viewModeContent;
	private HashMap<String, String[]> groups;
	private String selectedGroupBy;

	private final HashMap<JComboBox<String>, String> selectedValues = new HashMap<JComboBox<String>, String>();

	/**
	 * @param name
	 * @param orientation
	 */
	public MWToolBar(String name, int orientation) {
		super(name, orientation);
		this.initComponents();
	}

	/**
	 * Metoda prywatna, która tworzy wszystkie komponenty wyświetlane na
	 * {@link MWToolBar}. Zawiera również dodanie wszystkich listenerów do
	 * {@link JComboBox} które zawierają kryteria wyświetlania kolekcji.
	 */
	private void initComponents() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setOpaque(false);
		this.setAutoscrolls(false);
		this.setFloatable(false);
		this.setRollover(false);

		// database
		// TODO add online support !!!
		String z[] = { "Local" };
		this.dbContent = new JComboBox<String>(z);
		JLabel collectionLabel = new JLabel("Collection : ");
		collectionLabel.setLabelFor(this.dbContent);
		this.add(collectionLabel);
		this.add("Collection", this.dbContent);

		// zoom ;-)
		String zz[] = { "100%", "90%", "80%", "70%", "60%", "50%", "40%",
				"30%", "20%", "10%" };
		this.zoomContent = new JComboBox<String>(zz);
		JLabel zoomLabel = new JLabel("Zoom : ");
		zoomLabel.setLabelFor(this.zoomContent);
		this.add(zoomLabel);
		this.add("Zoom", this.zoomContent);

		// view mode ;-)
		String zzz[] = CollectionView.toArray();
		this.viewModeContent = new JComboBox<String>(zzz);
		JLabel viewModeLabel = new JLabel("View : ");
		viewModeLabel.setLabelFor(this.viewModeContent);
		this.add(viewModeLabel);
		this.add("View as", this.viewModeContent);

		// group by
		// hint -> here
		this.groups = new HashMap<String, String[]>();
		String bookGroup[] = { "Author", "ISBN" };
		String movieGroup[] = { "Director" };
		String audioGroup[] = { "Band" };
		String mutualGroup[] = { "No group", "Title", "Year", "Genre" };
		this.groups.put(CollectionView.VIEW_BOOKS.toString(), bookGroup);
		this.groups.put(CollectionView.VIEW_MOVIES.toString(), movieGroup);
		this.groups.put(CollectionView.VIEW_AUDIOS.toString(), audioGroup);
		this.groups.put(CollectionView.VIEW_ALL.toString(), mutualGroup);

		this.selectedGroupBy = CollectionView.VIEW_ALL.toString();

		this.groupBy = new JComboBox<String>(
				this.groups.get(this.selectedGroupBy));
		JLabel groupByLabel = new JLabel("Group by : ");
		groupByLabel.setLabelFor(this.groupBy);
		this.add(groupByLabel);
		this.add("Group by", this.groupBy);

		// adding listeners
		this.dbContent.addActionListener(this);
		this.dbContent.setName("database");
		this.groupBy.addActionListener(this);
		this.groupBy.setName("groupBy");
		this.viewModeContent.addActionListener(this);
		this.viewModeContent.setName("viewAs");
		this.zoomContent.addActionListener(this);
		this.zoomContent.setName("zoom");

		this.selectedValues.put(this.dbContent,
				(String) this.dbContent.getSelectedItem());
		this.selectedValues.put(this.groupBy,
				(String) this.groupBy.getSelectedItem());
		this.selectedValues.put(this.viewModeContent,
				(String) this.viewModeContent.getSelectedItem());
		this.selectedValues.put(this.zoomContent,
				(String) this.zoomContent.getSelectedItem());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		final Object source = arg0.getSource();
		final boolean validSource = source.equals(this.groupBy)
				|| source.equals(this.dbContent)
				|| source.equals(this.viewModeContent)
				|| source.equals(this.zoomContent);
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (validSource) {
					updateCollectionStatus(extracted(source));
				}
			}
		});
		if (source.equals(this.viewModeContent)) {
			this.groupBy.removeAllItems();
			String selectedView = (String) this.viewModeContent
					.getSelectedItem();
			String newContent[] = this.groups.get(selectedView);
			for (String string : newContent) {
				this.groupBy.addItem(string);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private JComboBox<String> extracted(Object source) {
		return (JComboBox<String>) source;
	}

	/**
	 * Metoda prywatna, która zmienia zmienne flagowe informujące o aktualnie
	 * wyświetlanej zawartości kolekcji na {@link MWCollectionView}. <b>Zmiana
	 * wartości flagi powoduje wywołania
	 * {@link JToolBar#firePropertyChange(String, Object, Object)} </b> co jest
	 * przychwytywane w {@link MWCollectionView} aby przeładować zawartość
	 * kolekcji.
	 * 
	 * @param source
	 * @see MWCollectionView
	 * @see JToolBar#firePropertyChange(String, Object, Object)
	 */
	private void updateCollectionStatus(JComboBox<String> source) {
		String oldString = this.selectedValues.get(source);
		String newString = (String) source.getSelectedItem();
		if (oldString.equals(newString)) {
			return;
		}
		this.selectedValues.remove(source);
		this.selectedValues.put(source, newString);
		String params[] = { this.getClass().getSimpleName(), source.getName(),
				oldString, newString };
		MabisLogger.getLogger().log(Level.INFO,
				"{0}: Property {1} changed from {2} to {3}", params);
		this.firePropertyChange(source.getName(), oldString, newString);
	}
}
