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
	private JComboBox<String> groupBy, zoomContent;
	private JComboBox<CollectionView> viewModeContent;
	private HashMap<CollectionView, String[]> groups;
	private CollectionView selectedGroupBy;

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

		// zoom ;-)
		String zz[] = { "100%", "90%", "80%", "70%", "60%", "50%", "40%",
				"30%", "20%", "10%" };
		this.zoomContent = new JComboBox<String>(zz);
		JLabel zoomLabel = new JLabel("Zoom : ");
		zoomLabel.setLabelFor(this.zoomContent);
		this.add(zoomLabel);
		this.add("Zoom", this.zoomContent);

		// view mode ;-)
		CollectionView zzz[] = CollectionView.toArray();
		this.viewModeContent = new JComboBox<CollectionView>(zzz);
		JLabel viewModeLabel = new JLabel("View : ");
		viewModeLabel.setLabelFor(this.viewModeContent);
		this.add(viewModeLabel);
		this.add("View as", this.viewModeContent);

		// group by
		this.groups = new HashMap<CollectionView, String[]>();
		String bookGroup[] = { "Author", "ISBN" };
		String movieGroup[] = { "Director" };
		String audioGroup[] = { "Band" };
		String mutualGroup[] = { "No group", "Title" };
		this.groups.put(CollectionView.VIEW_BOOKS, bookGroup);
		this.groups.put(CollectionView.VIEW_MOVIES, movieGroup);
		this.groups.put(CollectionView.VIEW_AUDIOS, audioGroup);
		this.groups.put(CollectionView.VIEW_ALL, mutualGroup);
		this.selectedGroupBy = CollectionView.VIEW_ALL;
		this.groupBy = new JComboBox<String>(
				this.groups.get(this.selectedGroupBy));
		JLabel groupByLabel = new JLabel("Group by : ");
		groupByLabel.setLabelFor(this.groupBy);
		this.add(groupByLabel);
		this.add("Group by", this.groupBy);

		// adding listeners
		this.groupBy.addActionListener(this);
		this.groupBy.setName("groupBy");
		this.viewModeContent.addActionListener(this);
		this.viewModeContent.setName("viewAs");
		this.zoomContent.addActionListener(this);
		this.zoomContent.setName("zoom");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox<?> source = (JComboBox<?>) e.getSource();
		if (source.equals(this.zoomContent)) {
			Double factor = (double) ((10.0 - this.zoomContent
					.getSelectedIndex())) / 10.0;
			this.firePropertyChange("zoomChanged", new Double(0.0), factor);
			MabisLogger.getLogger().log(Level.INFO, "Zoom set to {0}%",
					(factor * 100.0));
		} else if (source.equals(this.viewModeContent)) {
			// changing viewModeContent required updating groupBy
			final CollectionView newViewMode = (CollectionView) this.viewModeContent
					.getSelectedItem();
			final CollectionView oldViewMode = (CollectionView) this.selectedGroupBy;

			if (newViewMode.equals(oldViewMode)) {
				return;
			}
			MabisLogger.getLogger().log(Level.INFO, "Content set to {0}",
					newViewMode);

			this.groupBy.removeAllItems();
			for (String s : this.groups.get(newViewMode)) {
				this.groupBy.addItem(s);
			}
			if (newViewMode != CollectionView.VIEW_ALL) {
				for (String s : this.groups.get(CollectionView.VIEW_ALL)) {
					if (!s.equals("No group")) {
						this.groupBy.addItem(s);
					}
				}
			}

			final MWToolBar mwb = this;

			this.selectedGroupBy = newViewMode;
			java.awt.EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					mwb.firePropertyChange("contentChanged", oldViewMode,
							newViewMode);
				}
			});
		} else if (source.equals(this.groupBy)) {
			if (this.groupBy.getSelectedItem() != null) {
				MabisLogger.getLogger().log(Level.INFO, "GroupBy set to {0}",
						this.groupBy.getSelectedItem());
				this.firePropertyChange("groupByChanged", null,
						this.groupBy.getSelectedItem());
			}
		}
	}
}
