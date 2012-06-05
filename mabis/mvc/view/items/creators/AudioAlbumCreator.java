/**
 * 
 */
package mvc.view.items.creators;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import logger.MabisLogger;
import mvc.controller.SQLStamentType;
import mvc.controller.api.AudioAlbumAPI;
import mvc.controller.entity.AudioAlbumSQLFactory;
import mvc.controller.entity.AudioUserSQLFactory;
import mvc.controller.entity.BandSQLFactory;
import mvc.controller.entity.GenreSQLFactory;
import mvc.controller.exceptions.SQLEntityExistsException;
import mvc.model.BaseTable;
import mvc.model.entity.AudioAlbum;
import mvc.model.entity.AudioUser;
import mvc.model.entity.Author;
import mvc.model.entity.Band;
import mvc.model.entity.Genre;
import mvc.model.entity.Picture;
import mvc.model.entity.User;
import mvc.model.enums.AuthorType;
import mvc.model.enums.GenreType;
import mvc.model.enums.ImageType;
import mvc.model.utilities.ForeignKey;
import mvc.view.imagePanel.ImagePanel;
import mvc.view.items.CreatorContentNullPointerException;
import mvc.view.items.ItemCreator;
import mvc.view.items.itemsprieview.ItemsPreview;
import mvc.view.items.minipanels.BandMiniPanel;
import mvc.view.items.minipanels.TagCloudMiniPanel;
import mvc.view.items.minipanels.TrackListPanel;
import settings.GlobalPaths;
import settings.io.SettingsException;
import settings.io.SettingsLoader;

/**
 * Klasa definiuje Creator/Editor dla obiektu kolekcji jakim jest
 * {@link AudioAlbum}. Rozszerza abstrakcyjną klasą {@link ItemCreator}
 * definiując content adekwatny do utworzenia/edycji albumu muzycznego.
 * Umożliwia również dostęp do {@link AudioAlbumAPI}. Korzysta z następujących
 * mini paneli:
 * <ul>
 * <li> {@link TagCloudMiniPanel}</li>
 * <li> {@link BandMiniPanel}</li>
 * <li> {@link TrackListPanel}</li>
 * </ul>
 * 
 * @author kornicameister
 * @see AudioAlbumAPI
 * @see BandMiniPanel
 * @see TrackListPanel
 * @see TagCloudMiniPanel
 */
public class AudioAlbumCreator extends ItemCreator {
	private static final long serialVersionUID = 4214813665020457959L;
	private JTextField titleField;
	private TagCloudMiniPanel tagCloud;
	private BandMiniPanel bandMiniPanel;
	private JFormattedTextField durationField;
	private ImagePanel coverPanel;
	private TrackListPanel trackList;
	private LoadFromAudioAlbumApi lfa;

	public AudioAlbumCreator(User u, String title)
			throws CreatorContentNullPointerException {
		super(u, title);
		try {
			SettingsLoader.loadFrame(this);
		} catch (SettingsException e) {
			MabisLogger.getLogger().log(Level.WARNING,
					"Failed to load frame {0} from settigns", this.getName());
			this.setSize((int) this.getMinimumSize().getWidth() + 200,
					(int) this.getMinimumSize().getHeight());
			this.setTitle(title);
		}
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
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.coverPanel, 220, 220, 220)
								.addComponent(this.tagCloud, 220, 220, 220))
				.addGroup(
						gl.createParallelGroup()
								.addGroup(
										gl.createSequentialGroup()
												.addComponent(this.titleField)
												.addComponent(
														this.durationField))
								.addComponent(this.bandMiniPanel)
								.addComponent(this.trackList)));
		gl.setVerticalGroup(gl
				.createParallelGroup()
				.addGroup(
						gl.createSequentialGroup()
								.addComponent(this.coverPanel, 220, 220, 220)
								.addComponent(this.tagCloud))
				.addGroup(
						gl.createSequentialGroup()
								.addGroup(
										gl.createParallelGroup()
												.addComponent(this.titleField,
														60, 60, 60)
												.addComponent(
														this.durationField, 60,
														60, 60))
								.addComponent(this.bandMiniPanel, 120, 140, 150)
								.addComponent(this.trackList, 120, 140, 150)));
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (!editingMode) {
					coverPanel.setImage(new File(GlobalPaths.DEFAULT_COVER_PATH
							.toString()));
				}
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
		this.trackList.setBorder(BorderFactory.createTitledBorder("TrackList"));

		this.initializeTagCloud();
		this.initializeBandMiniPanel();

		this.durationField = new JFormattedTextField(new SimpleDateFormat(
				"hh:mm"));
		this.durationField.setBorder(BorderFactory
				.createTitledBorder("Duration"));
		this.coverPanel = new ImagePanel();
		String arrayOfCriteria[] = {"by album"};
		this.searchPanel.setSearchCriteria(arrayOfCriteria);
	}

	/**
	 * Inicjalizuje {@link BandMiniPanel} pobierając dane o wykonawcach albumów
	 * muzycznych z bazy danych.
	 */
	private void initializeBandMiniPanel() {
		try {
			BandSQLFactory asf = new BandSQLFactory(SQLStamentType.SELECT,
					new Band());
			asf.addWhereClause("type", AuthorType.AUDIO_ALBUM_BAND.toString());
			asf.executeSQL(true);
			this.bandMiniPanel = new BandMiniPanel(asf.getBands(),
					AuthorType.AUDIO_ALBUM_BAND);
			this.bandMiniPanel.setBorder(BorderFactory
					.createTitledBorder("Bands"));
		} catch (SQLException | SQLEntityExistsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicjalzuje chmurę tagów, tj. {@link TagCloudMiniPanel}, pobierając
	 * informacje o gatunkach dla konkretnego typu obiektów.
	 */
	private void initializeTagCloud() {
		try {
			GenreSQLFactory gsf = new GenreSQLFactory(SQLStamentType.SELECT,
					new Genre());
			gsf.addWhereClause("type", GenreType.AUDIO.toString());
			gsf.executeSQL(true);
			this.tagCloud = new TagCloudMiniPanel(gsf.getGenres(),
					GenreType.AUDIO);
			this.tagCloud.setBorder(BorderFactory
					.createTitledBorder("Tag cloud"));
		} catch (SQLException | SQLEntityExistsException e) {
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
	}

	@Override
	protected Boolean execute() {
		AudioAlbum selectedAlbum = new AudioAlbum();

		if (editingMode) {
			selectedAlbum = (AudioAlbum) this.editedItem;
		}

		try {
			selectedAlbum.setTitle(this.titleField.getText());
			selectedAlbum.setDuration((Long) this.durationField.getValue());
			selectedAlbum.setTrackList(this.trackList.getTracks());
			selectedAlbum.setDirectors(this.bandMiniPanel.getAuthors());
			selectedAlbum.setGenres(this.tagCloud.getTags());
			selectedAlbum.setCover(new Picture(this.coverPanel.getImageFile(),ImageType.FRONT_COVER));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (this.editingMode) {
			try {
				AudioAlbumSQLFactory aasf = new AudioAlbumSQLFactory(SQLStamentType.UPDATE, selectedAlbum);
				aasf.addWhereClause("idAudioAlbum", selectedAlbum.getPrimaryKey().toString());
				return aasf.executeSQL(true) > 0;
			} catch (SQLException | SQLEntityExistsException e) {
				e.printStackTrace();
			}
		} else {
			try {
				AudioUser au = new AudioUser();

				AudioAlbumSQLFactory aasf = new AudioAlbumSQLFactory(
						SQLStamentType.INSERT, selectedAlbum);
				AudioUserSQLFactory ausf = new AudioUserSQLFactory(
						SQLStamentType.INSERT, au);

				aasf.executeSQL(true);

				au.addMultiForeignKey(-1, new ForeignKey(selectedAlbum,
						"idAudio", selectedAlbum.getPrimaryKey()),
						new ForeignKey(this.selectedUser, "idUser",
								this.selectedUser.getPrimaryKey()));

				ausf.executeSQL(true);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (SQLEntityExistsException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Match",
						JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
								GlobalPaths.OK_SIGN.toString()));
				return true;
			}
		}
		this.firePropertyChange("itemAffected", null, selectedAlbum);
		return true;
	}

	/**
	 * Podklasa SwingWorkera, pozwala na wykonanie całej łączności z API poprzez
	 * wątek działający w tle. Skonfigurowana aby pobrać dane przez
	 * {@link AudioAlbumAPI}.
	 * 
	 * @author kornicameister
	 * @see AudioAlbumAPI
	 */
	class LoadFromAudioAlbumApi extends SwingWorker<TreeSet<BaseTable>, Void> {
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

		lfa = new LoadFromAudioAlbumApi();
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
		for (Author aa : a.getAuthors()) {
			Band b = (Band) aa;
			if (this.editingMode) {
				this.bandMiniPanel.addRow(b);
			} else {
				int index = Collections.binarySearch(
						this.bandMiniPanel.getDatabaseAuthors(), b,
						new Comparator<Author>() {
							@Override
							public int compare(Author o1, Author o2) {
								int result = o1.getLastName().compareTo(
										o2.getLastName());
								if (result == 0) {
									result = o1.getFirstName().compareTo(
											o2.getFirstName());
								}
								return result;
							}
						});
				if (index < 0) {
					b.setType(AuthorType.AUDIO_ALBUM_BAND);
					this.bandMiniPanel.addRow(b);
				} else {
					this.bandMiniPanel.addRow(this.bandMiniPanel
							.getDatabaseAuthors().get(index));
				}
			}
		}

		// check up - 2, the same thing, but now it does concern genres
		for (Genre g : a.getGenres()) {
			if (this.editingMode) {
				this.tagCloud.addRow(g);
			} else {
				int index = Collections.binarySearch(
						this.tagCloud.getDatabaseTags(), g,
						new Comparator<Genre>() {
							@Override
							public int compare(Genre g1, Genre g2) {
								int result = g1.getType().compareTo(
										g2.getType());
								if (result == 0) {
									result = g1.getGenre().compareTo(
											g2.getGenre());
								}
								return result;
							}
						});
				if (index < 0) {
					g.setType(GenreType.AUDIO);
					this.tagCloud.addRow(g);
				} else {
					this.tagCloud.addRow(this.tagCloud.getDatabaseTags().get(
							index));
				}
			}
		}
	}

}
