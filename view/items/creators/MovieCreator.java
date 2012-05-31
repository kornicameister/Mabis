package view.items.creators;

import java.awt.HeadlessException;
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
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import logger.MabisLogger;
import model.BaseTable;
import model.entity.Author;
import model.entity.Genre;
import model.entity.Movie;
import model.entity.MovieUser;
import model.entity.Picture;
import model.entity.User;
import model.enums.AuthorType;
import model.enums.GenreType;
import model.enums.ImageType;
import model.utilities.ForeignKey;
import settings.GlobalPaths;
import view.imagePanel.ImagePanel;
import view.items.CreatorContentNullPointerException;
import view.items.ItemCreator;
import view.items.itemsprieview.ItemsPreview;
import view.items.minipanels.AuthorMiniPanel;
import view.items.minipanels.TagCloudMiniPanel;
import controller.SQLStamentType;
import controller.api.MovieAPI;
import controller.api.MovieAPI.MovieApiTarget;
import controller.entity.AuthorSQLFactory;
import controller.entity.GenreSQLFactory;
import controller.entity.MovieSQLFactory;
import controller.entity.MovieUserSQLFactory;

/**
 * 
 * Klasa która definiuje strukturę okienka zapewniającego miejsce do
 * definiowanie nowego filmu.
 * 
 * @author kornicameister
 * 
 */
public class MovieCreator extends ItemCreator {
	private static final long serialVersionUID = -8029181092669910824L;
	private JTextField titleField;
	private ImagePanel coverPanel;
	private TagCloudMiniPanel tagCloud;
	private AuthorMiniPanel directorsPanel;
	private JFormattedTextField durationField;
	private Movie selectedMovie;
	private JTextArea descriptionArea;
	private JScrollPane descriptionScrollPane;
	private LoadFromApi lfa;
	private PropertyChangeListener miniPanelLister = new MiniPanelsListener();

	public MovieCreator(User u, String title) throws HeadlessException,
			CreatorContentNullPointerException {
		super(u, title);
		this.setSize((int) this.getMinimumSize().getWidth() + 190, (int) this
				.getMinimumSize().getHeight() + 50);
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
				.addGroup(gl.createParallelGroup()
						.addComponent(this.coverPanel, 220, 220, 220)
						.addComponent(this.descriptionScrollPane))
				.addGroup(
						gl.createParallelGroup()
								.addGroup(
										gl.createSequentialGroup()
												.addComponent(this.titleField)
												.addComponent(
														this.durationField))
								.addComponent(this.directorsPanel)
								.addComponent(this.tagCloud)
								.addComponent(this.descriptionScrollPane)));
		gl.setVerticalGroup(gl
				.createParallelGroup()
				.addGroup(gl.createSequentialGroup()
						.addComponent(this.coverPanel, 220, 220, 220)
						.addComponent(this.descriptionScrollPane))
				.addGroup(
						gl.createSequentialGroup()
								.addGroup(
										gl.createParallelGroup()
												.addComponent(this.titleField,50, 60, 60)
												.addComponent(this.durationField, 50, 60, 60))
								.addComponent(this.directorsPanel)
								.addComponent(this.tagCloud)));

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
	protected void initComponents() throws CreatorContentNullPointerException {
		super.initComponents();
		this.contentPanel = new JPanel(true);
		this.titleField = new JTextField();
		this.descriptionArea = new JTextArea();
		this.descriptionArea.setLineWrap(true);
		this.descriptionScrollPane = new JScrollPane(this.descriptionArea);
		this.descriptionScrollPane.setBorder(BorderFactory
				.createTitledBorder("Plot"));
		this.titleField.setBorder(BorderFactory.createTitledBorder("Title"));
		this.durationField = new JFormattedTextField(new SimpleDateFormat(
				"hh:mm"));
		this.durationField.setBorder(BorderFactory
				.createTitledBorder("Duration"));
		this.coverPanel = new ImagePanel();

		this.initializeAuthorsMiniPanel();
		this.initializeTagCloud();

		String arr[] = { "by title" };
		this.searchPanel.setSearchCriteria(arr);
	}

	private void initializeAuthorsMiniPanel() {
		try {
			AuthorSQLFactory asf = new AuthorSQLFactory(SQLStamentType.SELECT, new Author());
			asf.addWhereClause("type", AuthorType.MOVIE_DIRECTOR.toString());
			asf.executeSQL(true);
			this.directorsPanel = new AuthorMiniPanel(asf.getAuthors(), AuthorType.MOVIE_DIRECTOR);
			this.directorsPanel.setBorder(BorderFactory.createTitledBorder("Authors"));
			this.directorsPanel.addPropertyChangeListener("author", this.miniPanelLister);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initializeTagCloud() {
		// loading genres
		try {
			GenreSQLFactory gsf = new GenreSQLFactory(SQLStamentType.SELECT,
					new Genre());
			gsf.addWhereClause("type", GenreType.MOVIE.toString());
			gsf.executeSQL(true);
			this.tagCloud = new TagCloudMiniPanel(gsf.getGenres(),GenreType.MOVIE);
			this.tagCloud.setBorder(BorderFactory.createTitledBorder("Tag cloud"));
			this.tagCloud.addPropertyChangeListener("tag", this.miniPanelLister);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	class MiniPanelsListener implements PropertyChangeListener{

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if(evt.getPropertyName().equals("author")){
				Author tmp = (Author) evt.getNewValue();
				if(!selectedMovie.getAuthors().contains(tmp)){
					selectedMovie.addAuthor(tmp);
				}
			}else if(evt.getPropertyName().equals("tag")){
				Genre tmp = (Genre) evt.getNewValue();
				if(!selectedMovie.getGenres().contains(tmp)){
					selectedMovie.addGenre(tmp);
				}
			}
		}
		
	}

	@Override
	protected void clearContentFields() {
		this.titleField.setText("");
		this.durationField.setText("");
		this.descriptionArea.setText("");
		this.coverPanel.setImage(new File(GlobalPaths.DEFAULT_COVER_PATH
				.toString()));
		this.directorsPanel.clear();
		this.tagCloud.clear();
	}

	@Override
	protected Boolean execute() {
		try {
			this.selectedMovie.setTitle(this.titleField.getText());
			this.selectedMovie.setCover(new Picture(this.coverPanel.getImageFile(),ImageType.FRONT_COVER));
			this.selectedMovie.setDescription(this.descriptionArea.getText());
			this.selectedMovie.setDuration((Long) this.durationField.getValue());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if(this.editingMode){
			try {
				MovieSQLFactory msf = new MovieSQLFactory(SQLStamentType.UPDATE, this.selectedMovie);
				return msf.executeSQL(true) > 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			try{
				MovieSQLFactory msf = new MovieSQLFactory(SQLStamentType.INSERT, this.selectedMovie);
				this.selectedMovie.setPrimaryKey(msf.executeSQL(true));
				
				MovieUser mu = new MovieUser();
				mu.addMultiForeignKey(-1,
						new ForeignKey(this.selectedMovie, "idMovie", this.selectedMovie.getPrimaryKey()),
						new ForeignKey(this.selectedUser, "idUser", this.selectedUser.getPrimaryKey()));
				
				MovieUserSQLFactory musf = new MovieUserSQLFactory(SQLStamentType.INSERT, mu);
				musf.executeSQL(true);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		this.firePropertyChange("itemAffected", null, this.selectedMovie);
		return true;
	}

	/**
	 * Podklasa SwingWorkera, pozwala na wykonanie całej łączności z API poprzez
	 * wątek działający w tle. Wyspecjalizowana do łączenia się z API dla
	 * filmów.
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
				ItemsPreview ip = new ItemsPreview("Collected movies",this.get());
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
			MovieAPI ma = new MovieAPI(MovieApiTarget.IMDB);
			ma.getPcs().addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					String propertyName = evt.getPropertyName();
					if (propertyName.equals("taskStarted")) {
						taskSize = (Integer) evt.getNewValue();
						step = (searchProgressBar.getMaximum() - searchProgressBar.getMinimum()) / taskSize;
						value = searchProgressBar.getMinimum() + step;
						setProgress(value);
						if (taskSize.equals(1)) {
							value = 0;
						}
					} else if (propertyName.equals("taskStep")) {
						value += step;
						setProgress(value);
					}
				}
			});

			try {
				setProgress(searchProgressBar.getMinimum());
					TreeMap<String, String> params = new TreeMap<>();
					params.put("movie", query);
					ma.query(params);
				setProgress(searchProgressBar.getMaximum());
				return ma.getResult();
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
		Movie m = (Movie) table;
		this.titleField.setText(m.getTitle());
		this.durationField.setText(m.getDuration());
		this.descriptionArea.setText(m.getDescription());
		this.coverPanel.setImage(m.getCover().getImageFile());

		//check up - 1, if loaded directors are the same (in terms of first name/last name) as those
		//that can be found in AuthorsMiniPanel
		for(Author a : m.getAuthors()){
			if(this.editingMode){
				this.directorsPanel.addRow(a);
			}else{
				int index = Collections.binarySearch(this.directorsPanel.getAuthors(),
						a,
						new Comparator<Author>() {
							@Override
							public int compare(Author o1, Author o2) {
								int result = o1.getLastName().compareTo(o2.getLastName());
								if(result == 0){
									result = o1.getFirstName().compareTo(o2.getFirstName());
								}
								return result;
							}
						});
				if(index < 0){
					this.directorsPanel.addRow(a);
					this.directorsPanel.getAuthors().add(a);
					a.setType(AuthorType.MOVIE_DIRECTOR);
				}else{
					a.setPrimaryKey(this.directorsPanel.getAuthors().get(index).getPrimaryKey());
					this.directorsPanel.addRow(a);
				}
			}
		}
		
		//check up - 2, the same thing, but now it does concern genres
		for(Genre g : m.getGenres()){
			if(this.editingMode){
				this.tagCloud.addRow(g);
			}else{
				int index= Collections.binarySearch(
						this.tagCloud.getTags(), 
						g,
						new Comparator<Genre>() {
							@Override
							public int compare(Genre g1, Genre g2) {
								int result = g1.getType().compareTo(g2.getType());
								if(result == 0){
									result = g1.getGenre().compareTo(g2.getGenre());
								}
								return result;
							}
						});
				if(index < 0){
					this.tagCloud.addRow(g);
					this.tagCloud.getTags().add(g);
					g.setType(GenreType.MOVIE);
				}else{
					g.setPrimaryKey(this.tagCloud.getTags().get(index).getPrimaryKey());
					this.tagCloud.addRow(g);
				}
			}
		}
	}

}
