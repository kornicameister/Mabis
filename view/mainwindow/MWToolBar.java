/**
 * 
 */
package view.mainwindow;

import java.awt.FlowLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JToolBar;

/**
 * @author kornicameister
 *
 */
public class MWToolBar extends JToolBar {
	private static final long serialVersionUID = 3148020721875579266L;
	private String groupBy;

	/**
	 * 
	 */
	public MWToolBar() {
		super();
		this.initComponents();
	}

	/**
	 * @param orientation
	 */
	public MWToolBar(int orientation) {
		super(orientation);
		this.initComponents();
	}

	/**
	 * @param name
	 */
	public MWToolBar(String name) {
		super(name);
		this.initComponents();
	}

	/**
	 * @param name
	 * @param orientation
	 */
	public MWToolBar(String name, int orientation) {
		super(name, orientation);
		this.initComponents();
	}
	
	private void initComponents(){
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setOpaque(false);
		this.setAutoscrolls(false);
		this.setFloatable(false);
		this.setRollover(false);

		// database
		String z[] = { "Online", "Local" };
		JComboBox<String> dbContent = new JComboBox<String>(z);
		JLabel collectionLabel = new JLabel("Collection : ");
		collectionLabel.setLabelFor(dbContent);
		this.add(collectionLabel);
		this.add("Collection", dbContent);

		// zoom ;-)
		String zz[] = { "100%", "90%", "80%", "70%", "60%", "50%", "40%",
				"30%", "20%", "10%" };
		JComboBox<String> zoomContent = new JComboBox<String>(zz);
		JLabel zoomLabel = new JLabel("Zoom : ");
		zoomLabel.setLabelFor(zoomContent);
		this.add("Zoom", zoomLabel);
		this.add("Zoom", zoomContent);

		// view mode ;-)
		String zzz[] = { "All", "Books", "Movies", "Music" };
		JComboBox<String> viewModeContent = new JComboBox<String>(zzz);
		JLabel viewModeLabel = new JLabel("View : ");
		viewModeLabel.setLabelFor(viewModeContent);
		this.add(viewModeLabel);
		this.add("View as", viewModeContent);

		// group by
		// hint -> here
		HashMap<String, String[]> groups = new HashMap<String, String[]>();
		String bookGroup[] = { "Author", "ISBN" };
		String movieGroup[] = { "Director" };
		String audioGroup[] = { "Band" };
		String mutualGroup[] = { "No group", "Title", "Year", "Genre" };
		groups.put("book", bookGroup);
		groups.put("movie", movieGroup);
		groups.put("audio", audioGroup);
		groups.put("null", mutualGroup);

		this.groupBy = "null";

		JComboBox<String> groupBy = new JComboBox<String>(
				groups.get(this.groupBy));
		JLabel groupByLabel = new JLabel("Group by : ");
		groupByLabel.setLabelFor(groupBy);
		this.add(groupByLabel);
		this.add("Group by", groupBy);

		JButton revalidateButton = new JButton("Evaluate");
		this.add(revalidateButton);
	}

}
