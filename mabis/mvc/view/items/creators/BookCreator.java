/**
 * 
 */
package mvc.view.items.creators;

import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;

import logger.MabisLogger;
import mvc.controller.SQLStamentType;
import mvc.controller.api.GoogleBookApi;
import mvc.controller.entity.AuthorSQLFactory;
import mvc.controller.entity.BookSQLFactory;
import mvc.controller.entity.BookUserSQLFactory;
import mvc.controller.entity.GenreSQLFactory;
import mvc.controller.exceptions.SQLEntityExistsException;
import mvc.model.BaseTable;
import mvc.model.entity.Author;
import mvc.model.entity.Book;
import mvc.model.entity.BookUser;
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
import mvc.view.items.minipanels.AuthorMiniPanel;
import mvc.view.items.minipanels.IndustryIdentifiersMiniPanel;
import mvc.view.items.minipanels.TagCloudMiniPanel;
import settings.GlobalPaths;
import settings.io.SettingsException;
import settings.io.SettingsLoader;

/**
 * Klasa definiuje Creator/Editor dla obiektu kolekcji jakim jest {@link Book}.
 * Rozszerza abstrakcyjną klasą {@link ItemCreator} definiując content adekwatny
 * do utworzenia/edycji albumu muzycznego. Umożliwia również dostęp do
 * {@link GoogleBookApi}. Korzysta z następujących mini paneli:
 * <ul>
 * <li> {@link TagCloudMiniPanel}</li>
 * <li> {@link AuthorMiniPanel}</li>
 * <li> {@link IndustryIdentifiersMiniPanel}</li>
 * </ul>
 * 
 * @author kornicameister
 * @see GoogleBookApi
 * @see AuthorMiniPanel
 * @see IndustryIdentifiersMiniPanel
 * @see TagCloudMiniPanel
 */
public class BookCreator extends ItemCreator {
	private static final long serialVersionUID = 6954574313564241105L;
	private ImagePanel coverPanel;
	private JTextArea descriptionArea;
	private JTextField titleOriginal, subTitle, pages;
	private TagCloudMiniPanel tagCloud;
	private AuthorMiniPanel authorsMiniPanel;
	private IndustryIdentifiersMiniPanel iiMiniPanel;
	private JScrollPane descriptionScrollPane;
	private LoadFromGoogleBookAPI lfa;

	/**
	 * Tworzy kreator/edytor dla nowych książek.
	 * 
	 * @param title
	 * @throws HeadlessException
	 * @throws CreatorContentNullPointerException
	 */
	public BookCreator(User u, String title) {
		super(u, title);
		try {
			SettingsLoader.loadFrame(this);
		} catch (SettingsException e) {
			MabisLogger.getLogger().log(Level.WARNING,
					"Failed to load frame {0} from settigns", this.getName());
			this.setSize((int) this.getMinimumSize().getWidth() + 220,
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
				.createParallelGroup()
				.addGroup(
						gl.createSequentialGroup()
								.addGroup(
										gl.createParallelGroup()
												.addComponent(this.coverPanel,
														220, 220, 220)
												.addComponent(
														descriptionScrollPane))
								.addGroup(
										gl.createParallelGroup()
												.addComponent(
														this.titleOriginal)
												.addGroup(
														gl.createSequentialGroup()
																.addComponent(
																		this.subTitle)
																.addComponent(
																		this.pages))
												.addGap(5)
												.addComponent(this.iiMiniPanel)
												.addComponent(
														this.authorsMiniPanel)
												.addComponent(this.tagCloud)))
				.addComponent(descriptionScrollPane));
		gl.setVerticalGroup(gl
				.createSequentialGroup()
				.addGroup(
						gl.createParallelGroup()
								.addGroup(
										gl.createSequentialGroup()
												.addComponent(this.coverPanel,
														220, 220, 220)
												.addComponent(
														descriptionScrollPane))
								.addGroup(
										gl.createSequentialGroup()
												.addComponent(
														this.titleOriginal,
														GroupLayout.DEFAULT_SIZE,
														60, 80)
												.addGroup(
														gl.createParallelGroup()
																.addComponent(
																		this.subTitle,
																		GroupLayout.DEFAULT_SIZE,
																		60, 80)
																.addComponent(
																		this.pages,
																		GroupLayout.DEFAULT_SIZE,
																		60, 80))
												.addComponent(this.iiMiniPanel)
												.addComponent(
														this.authorsMiniPanel)
												.addComponent(this.tagCloud))));
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
		this.titleOriginal = new JTextField();
		this.subTitle = new JTextField();
		this.subTitle.setBorder(BorderFactory.createTitledBorder("Subtitle"));
		this.titleOriginal.setBorder(BorderFactory.createTitledBorder("Title"));

		this.initAuthorsMiniPanel();
		this.initGenresPanel();

		this.coverPanel = new ImagePanel();
		this.iiMiniPanel = new IndustryIdentifiersMiniPanel();
		this.iiMiniPanel.setBorder(BorderFactory.createTitledBorder("ISBN"));
		this.pages = new JTextField();
		this.pages.setBorder(BorderFactory.createTitledBorder("Pages"));
		this.descriptionArea = new JTextArea();
		this.descriptionArea.setLineWrap(true);
		this.descriptionScrollPane = new JScrollPane(this.descriptionArea);
		this.descriptionScrollPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"Descripion"));
		String arrayOfCriteria[] = {"by author", "by title"};
		this.searchPanel.setSearchCriteria(arrayOfCriteria);
	}

	/**
	 * Inicjalzuje chmurę tagów, tj. {@link TagCloudMiniPanel}, pobierając
	 * informacje o gatunkach dla konkretnego typu obiektów.
	 */
	private void initGenresPanel() {
		try {
			GenreSQLFactory gsf = new GenreSQLFactory(SQLStamentType.SELECT,
					new Genre());
			gsf.addWhereClause("type", GenreType.BOOK.toString());
			gsf.executeSQL(true);
			this.tagCloud = new TagCloudMiniPanel(gsf.getGenres(),
					GenreType.BOOK);
			this.tagCloud.setBorder(BorderFactory.createTitledBorder("Genres"));
		} catch (SQLException | SQLEntityExistsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicjalizuje {@link AuthorMiniPanel} pobierając dane o wykonawcach o
	 * autorach z bazy danych
	 */
	private void initAuthorsMiniPanel() {
		try {
			AuthorSQLFactory asf = new AuthorSQLFactory(SQLStamentType.SELECT,
					new Author());
			asf.addWhereClause("type", AuthorType.BOOK_AUTHOR.toString());
			asf.executeSQL(true);
			this.authorsMiniPanel = new AuthorMiniPanel(asf.getAuthors(),
					AuthorType.BOOK_AUTHOR);
			this.authorsMiniPanel.setBorder(BorderFactory
					.createTitledBorder("Writers"));
		} catch (SQLException | SQLEntityExistsException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void clearContentFields() {
		this.coverPanel.setImage(new File(GlobalPaths.DEFAULT_COVER_PATH
				.toString()));
		this.descriptionArea.setText("");
		this.titleOriginal.setText("");
		this.subTitle.setText("");
		this.pages.setText("");
		this.authorsMiniPanel.clear();
		this.tagCloud.clear();
		this.iiMiniPanel.clearTable();
	}

	@Override
	protected Boolean execute() {
		Book selectedBook = new Book();

		if (editingMode) {
			selectedBook = (Book) this.editedItem;
		}

		try {
			selectedBook.setIdentifiers(this.iiMiniPanel.getII());
			selectedBook.setTitle(this.titleOriginal.getText());
			selectedBook.setSubTitle(this.subTitle.getText());
			selectedBook.setPages(Integer.valueOf(this.pages.getText()));
			selectedBook.setDescription(this.descriptionArea.getText());
			selectedBook.setGenres(this.tagCloud.getTags());
			selectedBook.setDirectors(this.authorsMiniPanel.getAuthors());
			selectedBook.setCover(new Picture(this.coverPanel.getImageFile(),
					ImageType.FRONT_COVER));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (this.editingMode) {
			try {
				BookSQLFactory bsf = new BookSQLFactory(SQLStamentType.UPDATE,
						selectedBook);
				bsf.addWhereClause("idBook", selectedBook.getPrimaryKey().toString());
				return bsf.executeSQL(true) > 0;
			} catch (SQLException | SQLEntityExistsException e) {
				e.printStackTrace();
			}
		} else {
			try {
				BookSQLFactory bsf = new BookSQLFactory(SQLStamentType.INSERT,
						selectedBook);
				bsf.executeSQL(true);

				BookUser bu = new BookUser();
				bu.addMultiForeignKey(-1, new ForeignKey(selectedBook,
						"idBook", selectedBook.getPrimaryKey()),
						new ForeignKey(this.selectedUser, "idUser",
								selectedUser.getPrimaryKey()));

				BookUserSQLFactory busf = new BookUserSQLFactory(
						SQLStamentType.INSERT, bu);
				busf.executeSQL(true);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (SQLEntityExistsException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Match",
						JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
								GlobalPaths.OK_SIGN.toString()));
				return true;
			}
		}
		this.firePropertyChange("itemAffected", null, selectedBook);
		return true;
	}

	/**
	 * Podklasa {@link SwingWorker} która implementuje zachowanie właściwe do
	 * wykonania procesu pobierania danych z {@link GoogleBookApi} w tle.
	 * 
	 * @author tomasz
	 * @see GoogleBookApi
	 */
	class LoadFromGoogleBookAPI extends SwingWorker<TreeSet<BaseTable>, Void> {
		private String query, criteria;
		private Integer taskSize;
		private int step, value;

		public void setQuery(String query) {
			this.query = query;
		}

		public void setCriteria(String c) {
			this.criteria = c;
		}

		@Override
		protected void done() {
			try {
				ItemsPreview ip = new ItemsPreview("Collected books",
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
			GoogleBookApi gba = new GoogleBookApi();
			gba.getPcs().addPropertyChangeListener(
					new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							String propertyName = evt.getPropertyName();
							if (propertyName.equals("taskStarted")) {
								taskSize = (Integer) evt.getNewValue();
								step = (searchProgressBar.getMaximum() - searchProgressBar
										.getMinimum()) / taskSize;
								value = searchProgressBar.getMinimum() + step;
							} else if (propertyName.equals("taskStep")) {
								value = step * (int) evt.getNewValue();
								setProgress(value);
							}
						}
					});
			try {
				setProgress(searchProgressBar.getMinimum());
				TreeMap<String, String> params = new TreeMap<String, String>();
				if (criteria.contains("author")) {
					params.put("inauthor:", query);
				} else if (criteria.contains("title")) {
					params.put("intitle:", query);
				}
				gba.query(params);
				setProgress(searchProgressBar.getMaximum());
				return gba.getResult();
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

		lfa = new LoadFromGoogleBookAPI();
		lfa.setQuery(query);
		lfa.setCriteria(criteria);
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
		Book b = (Book) table;
		this.titleOriginal.setText(b.getTitle());
		this.subTitle.setText(b.getSubtitle());
		this.coverPanel.setImage(b.getCover().getImageFile());
		this.descriptionArea.setText(b.getDescription());
		this.pages.setText(b.getPages().toString());
		this.iiMiniPanel.setIIS(b.getIdentifiers());

		// check up - 1, if loaded directors are the same (in terms of first
		// name/last name) as those
		// that can be found in AuthorsMiniPanel
		for (Author a : b.getAuthors()) {
			if (this.editingMode) {
				this.authorsMiniPanel.addRow(a);
			} else {
				int index = Collections.binarySearch(
						this.authorsMiniPanel.getDatabaseAuthors(), a,
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
					a.setType(AuthorType.BOOK_AUTHOR);
					this.authorsMiniPanel.addRow(a);
				} else {
					this.authorsMiniPanel.addRow(this.authorsMiniPanel
							.getDatabaseAuthors().get(index));
				}
			}
		}

		// check up - 2, the same thing, but now it does concern genres
		for (Genre g : b.getGenres()) {
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
					g.setType(GenreType.BOOK);
					this.tagCloud.addRow(g);
				} else {
					this.tagCloud.addRow(this.tagCloud.getDatabaseTags().get(
							index));
				}
			}
		}
	}
}