/**
 * 
 */
package view.items.creators;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import logger.MabisLogger;
import model.BaseTable;
import model.entity.AudioAlbum;
import model.entity.Band;
import model.entity.Genre;
import model.enums.GenreType;
import settings.GlobalPaths;
import view.imagePanel.ImagePanel;
import view.items.CreatorContentNullPointerException;
import view.items.ItemCreator;
import view.items.itemsprieview.ItemsPreview;
import view.items.minipanels.BandMiniPanel;
import view.items.minipanels.TagCloudMiniPanel;
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
	private TagCloudMiniPanel tagCloud;
	private BandMiniPanel bandMiniPanel;
	private JFormattedTextField durationField;
	private ImagePanel coverPanel;
	private TrackListPanel trackList;
	private AudioAlbum selectedAlbum = new AudioAlbum();
	private JScrollPane trackListScroll;
	private LoadFromApi lfa;

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
		
		this.initializeTagCloud();
		this.initializeBandMiniPanel();
		
		this.durationField = new JFormattedTextField(new SimpleDateFormat(
				"hh:mm"));
		this.durationField.setBorder(BorderFactory
				.createTitledBorder("Duration"));
		this.coverPanel = new ImagePanel();
		String arrayOfCriteria[] = { "by album" };
		this.searchPanel.setSearchCriteria(arrayOfCriteria);
	}
	
	private void initializeBandMiniPanel() {
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
	}

	private void initializeTagCloud(){
		try {
			GenreSQLFactory gsf = new GenreSQLFactory(SQLStamentType.SELECT, new Genre());
			gsf.addWhereClause("type", GenreType.AUDIO.toString());
			gsf.executeSQL(true);
			this.tagCloud = new TagCloudMiniPanel(gsf.getGenres(),GenreType.AUDIO);
			this.tagCloud.setBorder(BorderFactory.createTitledBorder("Tag cloud"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		for (Genre g : this.tagCloud.getTags()) {
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

	/**
	 * Podklasa SwingWorkera, pozwala na wykonanie całej łączności z API poprzez
	 * wątek działający w tle
	 * 
	 * @author kornicameister
	 * 
	 */
	class LoadFromApi extends SwingWorker<TreeSet<BaseTable>, Void> {
		private String query;
		private Integer taskSize;
		private int step, value;

		public void setQuery(String query) {
			this.query = query;
		}

		@Override
		protected void done() {
			try {
				ItemsPreview ip = new ItemsPreview("Collected audio albums",
						this.get());
				ip.addPropertyChangeListener("selectedItem",
						new PropertyChangeListener() {
							@Override
							public void propertyChange(PropertyChangeEvent e) {
								fillWithResult((BaseTable) e.getNewValue());
							}
						});
				ip.setVisible(true);
			} catch (InterruptedException | ExecutionException e1) {
				e1.printStackTrace();
				MabisLogger.getLogger().log(Level.SEVERE,
						"Failed to receive data from background thread \n {0}",
						e1.getMessage());
			}
		}

		@Override
		protected TreeSet<BaseTable> doInBackground() throws Exception {
			AudioAlbumAPI aaa = new AudioAlbumAPI();
			aaa.getPcs().addPropertyChangeListener(
					new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							String propertyName = evt.getPropertyName();
							if (propertyName.equals("taskStarted")) {
								taskSize = (Integer) evt.getNewValue();
								step = (searchProgressBar.getMaximum() - searchProgressBar
										.getMinimum()) / taskSize;
								value = searchProgressBar.getMinimum() + step;
								setProgress(value);
							} else if (propertyName.equals("taskStep")) {
								value += step;
								setProgress(value);
							}
						}
					});

			try {
				setProgress(searchProgressBar.getMinimum());
				TreeMap<String, String> params = new TreeMap<String, String>();
				params.put("album", query);
				aaa.query(params);
				setProgress(searchProgressBar.getMaximum());
				return aaa.getResult();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	protected void fetchFromAPI(String query, String criteria) {
		if (this.collectedItems != null) {
			this.collectedItems.clear();
		}

		lfa = new LoadFromApi();
		lfa.setQuery(query);
		lfa.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress" == evt.getPropertyName()) {
					searchProgressBar.setValue((Integer) evt.getNewValue());
				}
			}
		});
		lfa.execute();
	}

	@Override
	protected void cancelAPISearch() {
		while (!lfa.isCancelled()) {
			lfa.cancel(true);
		}
		MabisLogger.getLogger().log(Level.INFO, "Terminated search operation");
		this.searchProgressBar.setValue(0);
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

	
}
