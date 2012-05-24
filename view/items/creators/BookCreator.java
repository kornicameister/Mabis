/**
 * 
 */
package view.items.creators;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;

import model.BaseTable;
import model.entity.Author;
import model.entity.Book;
import model.entity.Genre;
import model.enums.BookIndustryIdentifier;
import settings.GlobalPaths;
import view.imagePanel.ImagePanel;
import view.items.CreatorContentNullPointerException;
import view.items.ItemCreator;
import view.items.itemsprieview.ItemsPreview;
import view.items.minipanels.AuthorMiniPanel;
import view.items.minipanels.GenreMiniPanel;
import controller.SQLStamentType;
import controller.api.GoogleBookApi;
import controller.entity.AuthorSQLFactory;
import controller.entity.BookSQLFactory;
import controller.entity.GenreSQLFactory;

/**
 * @authorBox kornicameister
 * 
 */
public class BookCreator extends ItemCreator {
	private static final long serialVersionUID = 6954574313564241105L;
	private ImagePanel coverPanel;
	private JTextArea descriptionArea;
	private TitlesPanel titlesPanel;
	private DetailedInformationPanel detailedInfoPanel;
	private JScrollPane descriptionScrollPane;
	private Book selectedBook = new Book();

	/**
	 * Tworzy kreator/edytor dla nowych książek.
	 * 
	 * @param title
	 * @throws HeadlessException
	 * @throws CreatorContentNullPointerException
	 */
	public BookCreator(String title) throws CreatorContentNullPointerException {
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
				.createParallelGroup()
				.addGroup(
						gl.createSequentialGroup()
								.addComponent(this.coverPanel, 250, 250, 250)
								.addGroup(
										gl.createParallelGroup()
												.addComponent(this.titlesPanel)
												.addComponent(
														this.detailedInfoPanel)))
				.addComponent(descriptionScrollPane));
		gl.setVerticalGroup(gl
				.createSequentialGroup()
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.coverPanel, 250, 250, 250)
								.addGroup(
										gl.createSequentialGroup()
												.addComponent(
														this.titlesPanel,
														GroupLayout.DEFAULT_SIZE,
														60, 80)
												.addComponent(
														this.detailedInfoPanel,
														GroupLayout.DEFAULT_SIZE,
														100, 120)))
				.addComponent(descriptionScrollPane, 140, 140, 140));
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

		this.titlesPanel = new TitlesPanel(true);

		this.detailedInfoPanel = new DetailedInformationPanel(true);
		this.detailedInfoPanel.authorsMiniPanel
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent e) {
						String property = e.getPropertyName();
						if (property.equals("authorSelected")
								|| property.equals("authorCreated")) {
							Author tmp = (Author) e.getNewValue();
							if (selectedBook.getAuthors().size() == 0) {
								selectedBook.addAuthor(tmp);
								return;
							}
							for (Author a : selectedBook.getAuthors()) {
								if (!(a.getFirstName().equals(
										tmp.getFirstName()) && a.getLastName()
										.equals(tmp.getLastName()))) {
									selectedBook.addAuthor(tmp);
									return;
								}
							}
						}
					}
				});
		this.detailedInfoPanel.genresMiniPanel
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent e) {
						String property = e.getPropertyName();
						if (property.equals("genreSelected")
								|| property.equals("genreCreated")) {
							Genre tmp = (Genre) e.getNewValue();
							if (selectedBook.getGenres().size() == 0) {
								selectedBook.addGenre(tmp);
								return;
							}
							for (Genre g : selectedBook.getGenres()) {
								if (!g.getGenre().equals(tmp.getGenre())) {
									selectedBook.addGenre(tmp);
									return;
								}
							}
						}
					}
				});

		this.coverPanel = new ImagePanel();

		this.descriptionArea = new JTextArea();
		this.descriptionScrollPane = new JScrollPane(this.descriptionArea);
		this.descriptionScrollPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"Descripion"));
		String arrayOfCriteria[] = { "by author", "by title" };
		this.searchPanel.setSearchCriteria(arrayOfCriteria);
	}

	@Override
	protected void clearContentFields() {
		this.coverPanel.setImage(new File(GlobalPaths.DEFAULT_COVER_PATH
				.toString()));
		this.descriptionArea.setText("");
		this.titlesPanel.clear();
		this.detailedInfoPanel.clear();
	}

	@Override
	protected Boolean createItem() {
		this.selectedBook.setTitle(this.titlesPanel.titleOriginal.getText());
		this.selectedBook.setSubTitle(this.titlesPanel.subTitle.getText());

		if (!this.selectedBook.getIdentifiers().values()
				.contains(this.detailedInfoPanel.isbnField.getText())) {
			this.selectedBook.addGenre(new Genre(
					this.detailedInfoPanel.isbnField.getText()));
		}
		this.selectedBook.setPages(Integer.valueOf(this.detailedInfoPanel.pages
				.getText()));
		this.selectedBook.setDescription(this.descriptionArea.getText());

		BookSQLFactory bsf = new BookSQLFactory(SQLStamentType.INSERT,
				this.selectedBook);
		try {
			bsf.executeSQL(true);
			return true;
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
			protected Void doInBackground() throws Exception {
				GoogleBookApi gba = new GoogleBookApi();
				gba.getPcs().addPropertyChangeListener(
						new PropertyChangeListener() {

							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								String propertyName = evt.getPropertyName();
								if (propertyName.equals("taskStarted")) {
									taskSize = (Integer) evt.getNewValue();
									step = (searchBar.getMaximum() - searchBar.getMinimum()) / taskSize;
									value = searchBar.getMinimum() + step;
								} else if (propertyName.equals("taskStep")) {
									value = step*(int)evt.getNewValue();
									setProgress(value);
								}
							}
						});
				try {
					setProgress(searchBar.getMinimum());
						TreeMap<String, String> params = new TreeMap<String, String>();
						if (criteria.contains("author")) {
							params.put("inauthor:", query);
						} else if (criteria.contains("title")) {
							params.put("intitle:", query);
						}
						gba.query(params);
					setProgress(searchBar.getMaximum());

					ItemsPreview ip = new ItemsPreview("Collected books", gba.getResult());
					ip.addPropertyChangeListener("selectedItem",
							new PropertyChangeListener() {
								@Override
								public void propertyChange(PropertyChangeEvent e) {
									fillWithResult((BaseTable) e.getNewValue());
								}
							});
					ip.setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		}

		LoadFromApi lfa = new LoadFromApi();
		lfa.setQuery(query);
		lfa.setCriteria(criteria);
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
		this.selectedBook = (Book) table;
		this.titlesPanel.titleOriginal.setText(this.selectedBook.getTitle());
		this.titlesPanel.subTitle.setText(this.selectedBook.getSubtitle());
		this.coverPanel.setImage(this.selectedBook.getCover().getImageFile());
		this.descriptionArea.setText(this.selectedBook.getDescription());
		this.detailedInfoPanel.pages.setText(this.selectedBook.getPages()
				.toString());
		for (Author a : this.selectedBook.getAuthors()) {
			if (!(a.getFirstName().isEmpty() || a.getLastName().isEmpty())) {
				this.detailedInfoPanel.authorsMiniPanel.getAuthorsBox()
						.addItem(a.getFirstName() + " " + a.getLastName());
			}
			// TODO add checking if obtained author (coming from google book
			// api) is already present in database
		}
		for (Genre g : this.selectedBook.getGenres()) {
			if (!g.getGenre().equals("null") && !g.getGenre().isEmpty()) {
				this.detailedInfoPanel.genresMiniPanel.getGenreBox().addItem(
						g.getGenre());
			}
		}
		if (this.selectedBook.getIdentifier(BookIndustryIdentifier.ISBN_13) != null) {
			this.detailedInfoPanel.isbnField.setText(this.selectedBook
					.getIdentifier(BookIndustryIdentifier.ISBN_13));
		}
		this.detailedInfoPanel.authorsMiniPanel.getAuthorsBox()
				.setSelectedIndex(
						this.detailedInfoPanel.authorsMiniPanel.getAuthorsBox()
								.getItemCount() - 1);
		this.detailedInfoPanel.genresMiniPanel.getGenreBox().setSelectedIndex(
				this.detailedInfoPanel.genresMiniPanel.getGenreBox()
						.getItemCount() - 1);
	}

	/**
	 * Panel agregujący. Umieszczone są nim pola, służące do wprowadzania
	 * tytułów
	 * 
	 * @authorBox kornicameister
	 * 
	 */
	protected final class TitlesPanel extends JPanel {
		private static final long serialVersionUID = -8642265229218524372L;
		private final JTextField titleOriginal = new JTextField();
		private final JTextField subTitle = new JTextField();

		/**
		 * Tworzy panel dla tytułów
		 * 
		 * @param isDoubleBuffered
		 *            true - jeśli panel ma mieć podwójne buforowanie </br>
		 *            false - jeśli podwójne buforowanie ma być wyłączone
		 */
		public TitlesPanel(boolean isDoubleBuffered) {
			super(isDoubleBuffered);
			this.layoutComponents();
			this.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					"Titles"));
			this.subTitle.setBorder(BorderFactory
					.createTitledBorder("Subtitle"));
			this.titleOriginal.setBorder(BorderFactory
					.createTitledBorder("Title"));
		}

		protected void clear() {
			this.subTitle.setText("");
			this.titleOriginal.setText("");
		}

		private void layoutComponents() {
			this.setLayout(new GridLayout(2, 1, 5, 5));
			this.add(this.titleOriginal);
			this.add(this.subTitle);
		}

		public JTextField getTitleOriginal() {
			return titleOriginal;
		}

		public JTextField getTitleLocale() {
			return subTitle;
		}
	}

	/**
	 * Panel agregujący pola dla informacji szczegółowych o książce
	 * 
	 * @authorBox kornicameister
	 * 
	 */
	private final class DetailedInformationPanel extends JPanel {
		private static final long serialVersionUID = 3476477969389283620L;
		private final JTextField isbnField = new JTextField();
		private final JTextField pages = new JTextField();

		private GenreMiniPanel genresMiniPanel;
		private AuthorMiniPanel authorsMiniPanel;

		public DetailedInformationPanel(boolean isDoubleBuffered) {
			super(isDoubleBuffered);
			this.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					"Details"));
			try {
				this.loadAuthors();
				this.loadGenres();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			this.initComponents();
			this.layoutComponents();
		}

		private void loadGenres() throws SQLException {
			GenreSQLFactory gsf = new GenreSQLFactory(SQLStamentType.SELECT,
					new Genre());
			gsf.executeSQL(true);
			this.genresMiniPanel = new GenreMiniPanel("Genres", gsf.getGenres());
		}

		private void loadAuthors() throws SQLException {
			AuthorSQLFactory asf = new AuthorSQLFactory(SQLStamentType.SELECT,
					new Author());
			asf.executeSQL(true);
			this.authorsMiniPanel = new AuthorMiniPanel("Authors",
					asf.getAuthors());
		}

		/**
		 * Inicjalizuje komponenty tego JPanel
		 */
		private void initComponents() {
			this.isbnField.setBorder(BorderFactory.createTitledBorder("ISBN"));
			this.isbnField.setToolTipText("Provide ISBN-13 based identifier");
			this.pages.setBorder(BorderFactory.createTitledBorder("Pages"));
		}

		/**
		 * Zawartość pól {@link DetailedInformationPanel#isbnField} oraz
		 * {@link DetailedInformationPanel#pages} zostaje wyciszczone. Indeksy
		 * list {@link DetailedInformationPanel#authorCombobox} oraz
		 * {@link DetailedInformationPanel#genreCombobox} zostaję ustawione na
		 * początkowe pozycje.
		 */
		public void clear() {
			this.isbnField.setText("");
			this.pages.setText("");
			this.authorsMiniPanel.getAuthorsBox().removeAllItems();
			selectedBook.getAuthors().clear();
			this.genresMiniPanel.getGenreBox().removeAllItems();
			selectedBook.getGenres().clear();
		}

		private void layoutComponents() {
			this.setLayout(new GridLayout(4, 1));
			this.add(this.isbnField);
			this.add(this.pages);
			this.add(this.genresMiniPanel);
			this.add(this.authorsMiniPanel);
		}
	}
}