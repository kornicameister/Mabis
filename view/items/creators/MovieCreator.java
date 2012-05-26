package view.items.creators;

import java.awt.HeadlessException;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import logger.MabisLogger;
import model.BaseTable;
import model.entity.Author;
import model.entity.Movie;
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
	private LoadFromApi lfa;

	public MovieCreator(String title) throws HeadlessException,
			CreatorContentNullPointerException {
		super(title);
		this.setSize((int) this.getMinimumSize().getWidth() + 190, (int) this
				.getMinimumSize().getHeight()+50);
	}

	@Override
	protected void layoutComponents() {
		super.layoutComponents();
		GroupLayout gl = new GroupLayout(this.contentPanel);
		this.contentPanel.setLayout(gl);

		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);
		
		gl.setHorizontalGroup(
				gl.createSequentialGroup()
				.addComponent(this.coverPanel,250,250,250)
				.addGroup(
						gl.createParallelGroup()
						.addGroup(
								gl.createSequentialGroup()
								.addComponent(this.titleField)
								.addComponent(this.durationField))
						.addComponent(this.directorsPanel)
						.addComponent(this.tagCloud)
						.addComponent(this.descriptionArea)));
		gl.setVerticalGroup(
				gl.createParallelGroup()
				.addComponent(this.coverPanel,250,250,250)
				.addGroup(gl.createSequentialGroup()
						.addGroup(
								gl.createParallelGroup()
								.addComponent(this.titleField,40,40,40)
								.addComponent(this.durationField,40,40,40))
						.addComponent(this.directorsPanel)
						.addComponent(this.tagCloud)
						.addComponent(this.descriptionArea)));

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
		this.descriptionArea.setBorder(BorderFactory.createTitledBorder("Plot"));
		this.titleField.setBorder(BorderFactory.createTitledBorder("Title"));
		this.tagCloud = new TagCloudMiniPanel();
		this.tagCloud.setBorder(BorderFactory.createTitledBorder("Tag cloud"));
		this.durationField = new JFormattedTextField(new SimpleDateFormat("hh:mm"));
		this.durationField.setBorder(BorderFactory.createTitledBorder("Duration"));
		this.coverPanel = new ImagePanel();
		try {
			AuthorSQLFactory asf = new AuthorSQLFactory(SQLStamentType.SELECT,
					new Author());
			asf.executeSQL(true);
			TreeSet<Author> bridge = new TreeSet<>();
			for (Author a : asf.getAuthors()) {
				bridge.add(a);
			}
			this.directorsPanel = new AuthorMiniPanel("Bands", bridge);
			this.directorsPanel
					.addPropertyChangeListener(new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent e) {
							String property = e.getPropertyName();
							if (property.equals("bandSelected")
									|| property.equals("bandCreated")) {
								Author tmp = (Author) e.getNewValue();
								if (selectedMovie.getAuthors() == null) {
									selectedMovie.addAuthor(tmp);
									return;
								}
							}
						}
					});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String arr[] = {"by title"};
		this.searchPanel.setSearchCriteria(arr);
	}

	@Override
	protected void clearContentFields() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Boolean createItem() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Podklasa SwingWorkera, pozwala na wykonanie całej łączności z API poprzez
	 * wątek działający w tle.
	 * Wyspecjalizowana do łączenia się z API dla filmów.
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
				ItemsPreview ip = new ItemsPreview("Collected movies", this.get());
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
				MabisLogger.getLogger().log(Level.SEVERE,"Failed to receive data from background thread \n {0}",e1.getMessage());
			}
		}

		@Override
		protected TreeSet<BaseTable> doInBackground() throws Exception {
			MovieAPI ma = new MovieAPI(MovieApiTarget.IMDB);
			ma.getPcs().addPropertyChangeListener(
					new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							String propertyName = evt.getPropertyName();
							if (propertyName.equals("taskStarted")) {
								taskSize = (Integer) evt.getNewValue();
								step = (searchProgressBar.getMaximum() - searchProgressBar.getMinimum()) / taskSize;
								value = searchProgressBar.getMinimum() + step;
								setProgress(value);
								if(taskSize.equals(1)){
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void fillWithResult(BaseTable table) {
		// TODO Auto-generated method stub

	}

}
