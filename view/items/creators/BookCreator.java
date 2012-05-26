/**
 * 
 */
package view.items.creators;

import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;

import logger.MabisLogger;
import model.BaseTable;
import model.entity.Author;
import model.entity.Book;
import model.entity.Genre;
import model.enums.BookIndustryIdentifier;
import model.enums.GenreType;
import settings.GlobalPaths;
import view.imagePanel.ImagePanel;
import view.items.CreatorContentNullPointerException;
import view.items.ItemCreator;
import view.items.itemsprieview.ItemsPreview;
import view.items.minipanels.AuthorMiniPanel;
import view.items.minipanels.TagCloudMiniPanel;
import controller.SQLStamentType;
import controller.api.GoogleBookApi;
import controller.entity.BookSQLFactory;

/**
 * @authorBox kornicameister
 * 
 */
public class BookCreator extends ItemCreator {
	private static final long serialVersionUID = 6954574313564241105L;
	private ImagePanel coverPanel;
	private JTextArea descriptionArea;
	private JTextField titleOriginal;
	private JTextField subTitle;
	private JTextField isbnField;
	private JTextField pages;
	private TagCloudMiniPanel tagCloud;
	private AuthorMiniPanel authorsMiniPanel;
	private JScrollPane descriptionScrollPane;
	private Book selectedBook = new Book();
	private LoadFromApi lfa;

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
												.addComponent(
														this.titleOriginal)
												.addComponent(this.subTitle)
												.addGap(5)
												.addComponent(this.isbnField)
												.addComponent(this.pages)
												.addGap(5)
												.addComponent(
														this.authorsMiniPanel)
												.addComponent(this.tagCloud)))
				.addComponent(descriptionScrollPane));
		gl.setVerticalGroup(gl
				.createSequentialGroup()
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.coverPanel, 250, 250, 250)
								.addGroup(
										gl.createSequentialGroup()
												.addComponent(
														this.titleOriginal,
														GroupLayout.DEFAULT_SIZE,
														60, 80)
												.addComponent(
														this.subTitle,
														GroupLayout.DEFAULT_SIZE,
														60, 80)
												.addComponent(this.isbnField,
														40, 40, 40)
												.addComponent(this.pages, 40,
														40, 40)
												.addComponent(
														this.authorsMiniPanel,
														80, 80, 80)
												.addComponent(this.tagCloud,
														80, 80, 80)))
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
		this.titleOriginal = new JTextField();
		this.subTitle = new JTextField();
		this.subTitle.setBorder(BorderFactory.createTitledBorder("Subtitle"));
		this.titleOriginal.setBorder(BorderFactory.createTitledBorder("Title"));
		this.authorsMiniPanel = new AuthorMiniPanel("Authors",
				new TreeSet<Author>());
		this.authorsMiniPanel
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

		this.coverPanel = new ImagePanel();
		this.isbnField = new JTextField();
		this.pages = new JTextField();
		this.tagCloud = new TagCloudMiniPanel(new TreeSet<Genre>(),
				GenreType.BOOK);
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
		this.titleOriginal.setText("");
		this.subTitle.setText("");
		this.pages.setText("");
		this.authorsMiniPanel.clear();
		this.tagCloud.clear();
		this.isbnField.setText("");
	}

	@Override
	protected Boolean createItem() {
		this.selectedBook.setTitle(this.titleOriginal.getText());
		this.selectedBook.setSubTitle(this.subTitle.getText());

		if (!this.selectedBook.getIdentifiers().values()
				.contains(this.isbnField.getText())) {
			this.selectedBook.addGenre(new Genre(this.isbnField.getText()));
		}
		this.selectedBook.setPages(Integer.valueOf(this.pages.getText()));
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

	class LoadFromApi extends SwingWorker<TreeSet<BaseTable>, Void> {
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

		lfa = new LoadFromApi();
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
		this.selectedBook = (Book) table;
		this.titleOriginal.setText(this.selectedBook.getTitle());
		this.subTitle.setText(this.selectedBook.getSubtitle());
		this.coverPanel.setImage(this.selectedBook.getCover().getImageFile());
		this.descriptionArea.setText(this.selectedBook.getDescription());
		this.pages.setText(this.selectedBook.getPages().toString());
		for (Author a : this.selectedBook.getAuthors()) {
			if (!(a.getFirstName().isEmpty() || a.getLastName().isEmpty())) {
				this.authorsMiniPanel.getAuthorsBox().addItem(
						a.getFirstName() + " " + a.getLastName());
			}
			// TODO add checking if obtained author (coming from google book
			// api) is already present in database
		}
		for (Genre g : this.selectedBook.getGenres()) {
			if (!g.getGenre().equals("null") && !g.getGenre().isEmpty()) {
				this.tagCloud.addTag(g);
			}
		}
		if (this.selectedBook.getIdentifier(BookIndustryIdentifier.ISBN_13) != null) {
			this.isbnField.setText(this.selectedBook
					.getIdentifier(BookIndustryIdentifier.ISBN_13));
		}
		this.authorsMiniPanel.getAuthorsBox().setSelectedIndex(
				this.authorsMiniPanel.getAuthorsBox().getItemCount() - 1);
	}
}