/**
 * 
 */
package view.items.creators;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import model.BaseTable;
import model.entity.AudioAlbum;
import model.entity.Band;
import model.entity.Genre;
import settings.GlobalPaths;
import view.imagePanel.ImagePanel;
import view.items.CreatorContentNullPointerException;
import view.items.ItemCreator;
import view.items.itemsprieview.ItemsPreview;
import view.items.minipanels.BandMiniPanel;
import view.items.minipanels.GenreMiniPanel;
import view.items.minipanels.TrackListPanel;
import controller.SQLStamentType;
import controller.api.AudioAlbumAPI;
import controller.entity.AudioAlbumSQLFactory;
import controller.entity.BandSQLFactory;
import controller.entity.GenreSQLFactory;

/**
 * @author kornicameister
 * 
 */
public class AudioAlbumCreator extends ItemCreator {
	private static final long serialVersionUID = 4214813665020457959L;
	private JTextField titleField;
	private ItemTagCloudPanel tagCloud;
	private BandMiniPanel bandMiniPanel;
	private JFormattedTextField durationField;
	private ImagePanel coverPanel;
	private TrackListPanel trackList;
	private AudioAlbum selectedAlbum = new AudioAlbum();
	private JScrollPane trackListScroll;

	public AudioAlbumCreator(String title)
			throws CreatorContentNullPointerException {
		super(title);
		this.setSize((int) this.getMinimumSize().getWidth() + 200, (int) this
				.getMinimumSize().getHeight());
	}

	@Override
	protected void layoutComponents() {
		super.layoutComponents();
		GroupLayout gl = new GroupLayout(this.contentPanel);
		this.contentPanel.setLayout(gl);

		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);

		gl.setHorizontalGroup(gl
				.createSequentialGroup()
				.addComponent(this.coverPanel, 250, 250, 250)
				.addGroup(
						gl.createParallelGroup()
								.addGroup(
										gl.createSequentialGroup()
												.addComponent(this.titleField)
												.addComponent(
														this.durationField))
								.addComponent(this.bandMiniPanel)
								.addComponent(this.trackListScroll)
								.addComponent(this.tagCloud)));
		gl.setVerticalGroup(gl
				.createParallelGroup()
				.addComponent(this.coverPanel, 250, 250, 250)
				.addGroup(
						gl.createSequentialGroup()
								.addGroup(
										gl.createParallelGroup()
												.addComponent(this.titleField,
														35, 35, 35)
												.addComponent(
														this.durationField, 35,
														35, 35))
								.addComponent(this.bandMiniPanel, 70, 70, 70)
								.addComponent(this.trackListScroll, 100, 100,
										100)
								.addComponent(this.tagCloud, 100, 100, 100)));
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				coverPanel.setImage(new File(GlobalPaths.DEFAULT_COVER_PATH
						.toString()));
			}
		});
		this.revalidate();
		this.pack();
	}

	@Override
	public void initComponents() {
		super.initComponents();
		this.contentPanel = new JPanel(true);
		this.titleField = new JTextField();
		this.titleField.setBorder(BorderFactory.createTitledBorder("Title"));
		this.trackList = new TrackListPanel();
		this.trackListScroll = new JScrollPane(this.trackList);
		this.tagCloud = new ItemTagCloudPanel();
		this.tagCloud.setBorder(BorderFactory.createTitledBorder("Tag cloud"));
		try {
			BandSQLFactory asf = new BandSQLFactory(SQLStamentType.SELECT,
					new Band());
			asf.executeSQL(true);
			TreeSet<Band> bridge = new TreeSet<>();
			for (Band b : asf.getBands()) {
				bridge.add(b);
			}
			this.bandMiniPanel = new BandMiniPanel("Bands", bridge);
			this.bandMiniPanel
					.addPropertyChangeListener(new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent e) {
							String property = e.getPropertyName();
							if (property.equals("bandSelected")
									|| property.equals("bandCreated")) {
								Band tmp = (Band) e.getNewValue();
								if (selectedAlbum.getBand() == null) {
									selectedAlbum.setBand(tmp);
									return;
								} else if (!selectedAlbum.getBand().getName()
										.equals(tmp.getName())) {
									selectedAlbum.setBand(tmp);
									return;
								}
							}
						}
					});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.durationField = new JFormattedTextField(new SimpleDateFormat(
				"hh:mm"));
		this.durationField.setBorder(BorderFactory
				.createTitledBorder("Duration"));
		this.coverPanel = new ImagePanel();
		String arrayOfCriteria[] = { "by album" };
		this.searchPanel.setSearchCriteria(arrayOfCriteria);
	}

	@Override
	protected void clearContentFields() {
		this.titleField.setText("");
		this.trackList.clear();
		this.tagCloud.clear();
		this.durationField.setText("");
		this.bandMiniPanel.clear();
		this.selectedAlbum = new AudioAlbum();
	}

	@Override
	protected Boolean createItem() {
		this.selectedAlbum.setTitle(this.titleField.getText());
		this.selectedAlbum.setDuration((Long) this.durationField.getValue());
		// this.selectedAlbum.setTrackList(this.trackList.getText());
		for (Genre g : this.tagCloud.tags) {
			this.selectedAlbum.addGenre(g);
		}
		AudioAlbumSQLFactory aasf = new AudioAlbumSQLFactory(
				SQLStamentType.INSERT, this.selectedAlbum);
		try {
			return aasf.executeSQL(true) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void fetchFromAPI(String query, String criteria) {
		if (this.collectedItems != null) {
			this.collectedItems.clear();
		}
	
		class LoadFromApi extends SwingWorker<Void, Void> {
			private String query;
			private Integer taskSize;
			private int step, value;

			public void setQuery(String query) {
				this.query = query;
			}

			@Override
			protected Void doInBackground() throws Exception {
				AudioAlbumAPI aaa = new AudioAlbumAPI();
				aaa.getPcs().addPropertyChangeListener(
						new PropertyChangeListener() {

							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								String propertyName = evt.getPropertyName();
								if(propertyName.equals("taskStarted")){
									taskSize = (Integer) evt.getNewValue();
									step = (searchBar.getMaximum() - searchBar.getMinimum())/taskSize;
									value = searchBar.getMinimum() + step;
									setProgress(value);
								}else if(propertyName.equals("taskStep")){
									value += step;
									setProgress(value);
								}
							}
						});

				try {
					setProgress(searchBar.getMinimum());
						TreeMap<String, String> params = new TreeMap<String, String>();
						params.put("album", query);
						aaa.query(params);
					setProgress(searchBar.getMaximum());
					
					ItemsPreview ip = new ItemsPreview("Collected audio albums",aaa.getResult());
					ip.addPropertyChangeListener("selectedItem",
							new PropertyChangeListener() {
								@Override
								public void propertyChange(PropertyChangeEvent e) {
									fillWithResult((BaseTable) e.getNewValue());
								}
							});
					ip.setVisible(true);
					return null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		}

		LoadFromApi lfa = new LoadFromApi();
		lfa.setQuery(query);
		lfa.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress" == evt.getPropertyName()) {
					searchBar.setValue((Integer) evt.getNewValue());
				}
			}
		});
		lfa.execute();
	}

	@Override
	protected void fillWithResult(BaseTable table) {
		AudioAlbum a = (AudioAlbum) table;
		this.titleField.setText(a.getTitle());
		this.durationField.setText(a.getDuration());
		this.coverPanel.setImage(a.getCover().getImageFile());
		this.trackList.setTracks(a.getTrackList());
		// add setting left field
	}

	/**
	 * Klasa symuluje chmurę tagów. Tagi mogą być do niej dodawana pojedynczo
	 * lub jako cała {@link ArrayList}
	 * 
	 * @author kornicameister
	 * 
	 */
	class ItemTagCloudPanel extends JPanel {
		private static final long serialVersionUID = -8170767178911147951L;
		private ArrayList<Genre> tags = new ArrayList<>();
		private TreeMap<Genre, JLabel> tagToLabel = new TreeMap<>();
		private JPanel contentPanel = new JPanel();
		private GenreMiniPanel gmp;

		public ItemTagCloudPanel() {
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

		void clear() {
			this.tags.clear();
			this.contentPanel.removeAll();
			this.repaint();
		}
	}
}
