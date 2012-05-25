package view.items.minipanels;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.entity.Genre;
import controller.SQLStamentType;
import controller.entity.GenreSQLFactory;

/**
 * Klasa symuluje chmurę tagów. Tagi mogą być do niej dodawana pojedynczo lub
 * jako cała {@link ArrayList}
 * 
 * @author kornicameister
 * 
 */
public class TagCloudMiniPanel extends JPanel {
	private static final long serialVersionUID = -8170767178911147951L;
	private ArrayList<Genre> tags = new ArrayList<>();
	private TreeMap<Genre, JLabel> tagToLabel = new TreeMap<>();
	private JPanel contentPanel = new JPanel();
	private GenreMiniPanel gmp;

	public TagCloudMiniPanel() {
		// this.contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.contentPanel.setLayout(new BoxLayout(this.contentPanel,
				BoxLayout.Y_AXIS));

		GenreSQLFactory gsf = new GenreSQLFactory(SQLStamentType.SELECT,
				new Genre());
		try {
			gsf.executeSQL(true);
			gmp = new GenreMiniPanel("Tags", gsf.getGenres());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.add(gmp, BorderLayout.PAGE_START);
		this.add(this.contentPanel);

		this.gmp.getGenreBox().setVisible(false);
		this.gmp.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent e) {
				String property = e.getPropertyName();
				if (property.equals("genreSelected")
						|| property.equals("genreCreated")) {
					Genre tmp = (Genre) e.getNewValue();
					if (tags.size() == 0) {
						addTag(tmp);
						return;
					}
					for (Genre g : tags) {
						if (!g.getGenre().equals(tmp.getGenre())) {
							addTag(g);
							return;
						}
					}
				}
			}
		});
	}

	void addTag(Genre g) {
		if (!this.tags.contains(g)) {
			this.tags.add(g);
			// add to content panel
			JLabel tmp = new JLabel(g.toString());
			this.tagToLabel.put(g, tmp);
			this.contentPanel.add(tmp);
		}
	}

	void setTags(ArrayList<Genre> genres) {
		this.tags = genres;
		this.contentPanel.removeAll();
		for (Genre g : this.tags) {
			JLabel tmp = new JLabel(g.toString());
			this.contentPanel.add(tmp);
			this.tagToLabel.put(g, tmp);
		}
	}

	void removeTag(Genre g) {
		this.tags.remove(g);
		this.contentPanel.remove(this.tagToLabel.get(g));
		this.repaint();
	}

	public void clear() {
		this.tags.clear();
		this.contentPanel.removeAll();
		this.repaint();
	}

	/**
	 * @return the tags
	 */
	public ArrayList<Genre> getTags() {
		return tags;
	}
}
