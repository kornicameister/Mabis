/**
 * 
 */
package view.items;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import logger.MabisLogger;
import model.BaseTable;
import model.entity.Author;
import model.entity.Book;
import model.entity.Genre;
import model.enums.BookIndustryIdentifier;
import settings.GlobalPaths;
import view.imagePanel.ImagePanel;
import view.items.itemsprieview.ItemsPreview;
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
	public BookCreator(String title) throws HeadlessException,
			CreatorContentNullPointerException {
		super(title);
		this.setSize((int) this.getMinimumSize().getWidth() + 200, (int) this
				.getMinimumSize().getHeight() - 70);
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
								.addComponent(this.coverPanel)
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
								.addComponent(this.coverPanel,
										GroupLayout.DEFAULT_SIZE, 200, 220)
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
				.addComponent(descriptionScrollPane));

		this.revalidate();
		this.pack();
	}

	@Override
	public void initComponents() {
		super.initComponents();
		contentPanel = new JPanel(true);
		contentPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Creator"));

		titlesPanel = new TitlesPanel(true);
		detailedInfoPanel = new DetailedInformationPanel(true);
		coverPanel = new ImagePanel(new File(
				GlobalPaths.DEFAULT_COVER_PATH.toString()));
		coverPanel
				.setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
						"Cover"));

		descriptionArea = new JTextArea();
		descriptionScrollPane = new JScrollPane(this.descriptionArea);
		descriptionScrollPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"Descripion"));
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
	protected void fetchFromAPI() {
		if (this.collectedItems != null) {
			this.collectedItems.clear();
		}
		GoogleBookApi gba = new GoogleBookApi();
		try {
			String title = new String();
			if (this.titlesPanel.titleOriginal.getText() != null
					&& !this.titlesPanel.titleOriginal.getText().isEmpty()) {
				title = this.titlesPanel.titleOriginal.getText();
			}
			TreeMap<String, String> params = new TreeMap<String, String>();
			if (this.detailedInfoPanel.authorBox.getSelectedItem() != null) {
				String tmp = (String) this.detailedInfoPanel.authorBox
						.getSelectedItem();
				params.put("inauthor:", tmp);
			}
			gba.query(title, params);
			collectedItems = gba.getResult();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// init panel with obtained collection items so as to allow
		// user to choose one selected
		ItemsPreview ip = new ItemsPreview("Collected books",
				this.collectedItems);
		ip.addPropertyChangeListener("selectedItem",
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent e) {
						fillWithResult((BaseTable) e.getNewValue());
					}
				});
		ip.setVisible(true);
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
				this.detailedInfoPanel.authorBox.addItem(a.getFirstName() + " "
						+ a.getLastName());
			}
			// TODO add checking if obtained author (coming from google book
			// api) is already present in database
		}
		for (Genre g : this.selectedBook.getGenres()) {
			if (!g.getGenre().equals("null") && !g.getGenre().isEmpty()) {
				this.detailedInfoPanel.genreBox.addItem(g.getGenre());
			}
		}
		if (this.selectedBook.getIdentifier(BookIndustryIdentifier.ISBN_13) != null) {
			this.detailedInfoPanel.isbnField.setText(this.selectedBook
					.getIdentifier(BookIndustryIdentifier.ISBN_13));
		}
		this.detailedInfoPanel.authorBox
				.setSelectedIndex(this.detailedInfoPanel.authorBox
						.getItemCount() - 1);
		this.detailedInfoPanel.genreBox
				.setSelectedIndex(this.detailedInfoPanel.genreBox
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
		/**
		 * pole tekstowe dla tytułu w oryginalnym języku
		 */
		private final JTextField titleOriginal = new JTextField();
		/**
		 * pole tekstowe dla tytułu, który jest przetłumaczony
		 */
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
		/**
		 * pole tekstowe dla wprowadzania numeru isbn dla książki Ma dodany
		 * odpowiedni walidator, który nie pozwala na wprowadzenie numeru o złym
		 * formacie
		 */
		private final JTextField isbnField = new JTextField();
		/**
		 * pole tekstowe dla ilości stron danej ksiązki
		 */
		private final JTextField pages = new JTextField();
		private final JComboBox<String> genreBox = new JComboBox<>();
		private final JComboBox<String> authorBox = new JComboBox<>();

		// TODO add loading these values from database
		private ArrayList<Author> authors;
		private ArrayList<Genre> genres;
		private JButton newGenreButton = new JButton("N");
		private JButton selectGenreButton = new JButton("S");
		private JButton newAuthorButton = new JButton("N");
		private JButton selectAuthorButton = new JButton("S");

		DetailedInfoActionListener actionListener = new DetailedInfoActionListener();

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
			} finally {
				if (authors == null) {
					authors = new ArrayList<>();
				}
				if (genres == null) {
					genres = new ArrayList<>();
				}
			}

			this.initComponents();
			this.layoutComponents();
		}

		private void loadGenres() throws SQLException {
			GenreSQLFactory gsf = new GenreSQLFactory(SQLStamentType.SELECT,
					new Genre());
			gsf.executeSQL(true);
			this.genres = new ArrayList<>();
			this.genres.addAll(gsf.getGenres());
		}

		private void loadAuthors() throws SQLException {
			AuthorSQLFactory asf = new AuthorSQLFactory(SQLStamentType.SELECT,
					new Author());
			asf.executeSQL(true);
			this.authors = new ArrayList<>();
			this.authors.addAll(asf.getAuthors());
		}

		/**
		 * Inicjalizuje komponenty tego JPanel
		 */
		private void initComponents() {
			this.isbnField.setBorder(BorderFactory.createTitledBorder("ISBN"));
			this.isbnField.setToolTipText("Provide ISBN-13 based identifier");
			this.pages.setBorder(BorderFactory.createTitledBorder("Pages"));

			this.genreBox.setName("genreBox");
			this.newGenreButton.setToolTipText("Create new genre");
			this.newGenreButton.setName("Create new genre");
			this.newGenreButton.addActionListener(this.actionListener);
			this.selectGenreButton.setToolTipText("Select genre");
			this.selectGenreButton.setName("Select genre");
			this.selectGenreButton.addActionListener(this.actionListener);

			this.authorBox.setName("authorBox");
			this.newAuthorButton.setToolTipText("Create new author");
			this.newAuthorButton.setName("Create new author");
			this.newAuthorButton.addActionListener(this.actionListener);
			this.selectAuthorButton.setToolTipText("Select author");
			this.selectAuthorButton.setName("Select author");
			this.selectAuthorButton.addActionListener(this.actionListener);
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
			this.authorBox.setSelectedIndex(-1);
			this.genreBox.setSelectedIndex(-1);
		}

		private void layoutComponents() {
			this.setLayout(new GridLayout(4, 1));
			this.add(this.isbnField);
			this.add(this.pages);
			this.layoutGenreComboBox();
			this.layoutAuthorComboBox();
		}

		private void layoutAuthorComboBox() {
			JPanel tmp = new JPanel();
			tmp.setBorder(BorderFactory.createTitledBorder("Authors"));
			GroupLayout gl = new GroupLayout(tmp);
			tmp.setLayout(gl);

			gl.setHorizontalGroup(gl.createSequentialGroup()
					.addComponent(this.authorBox, 140, 140, 140)
					.addComponent(this.newAuthorButton, 50, 50, 50)
					.addComponent(this.selectAuthorButton, 50, 50, 50));
			gl.setVerticalGroup(gl.createParallelGroup()
					.addComponent(this.authorBox, 25, 25, 25)
					.addComponent(this.newAuthorButton)
					.addComponent(this.selectAuthorButton));
			this.add(tmp);
		}

		private void layoutGenreComboBox() {
			JPanel tmp = new JPanel();
			tmp.setBorder(BorderFactory.createTitledBorder("Genre"));
			GroupLayout gl = new GroupLayout(tmp);
			tmp.setLayout(gl);

			gl.setHorizontalGroup(gl.createSequentialGroup()
					.addComponent(this.genreBox, 140, 140, 140)
					.addComponent(this.newGenreButton, 50, 50, 50)
					.addComponent(this.selectGenreButton, 50, 50, 50));
			gl.setVerticalGroup(gl.createParallelGroup()
					.addComponent(this.genreBox, 25, 25, 25)
					.addComponent(this.newGenreButton)
					.addComponent(this.selectGenreButton));
			this.add(tmp);
		}

		/**
		 * Nasłuchuje na wydarzenia pochodzące od przycisków sterujących
		 * dodawaniem nowych autorów i gatunków
		 * 
		 * @author kornicameister
		 * 
		 */
		private class DetailedInfoActionListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton source = (JButton) e.getSource();
				if (source.equals(newAuthorButton)) {
					String returned = JOptionPane.showInputDialog(null,
							"Input:", source.getName(),
							JOptionPane.PLAIN_MESSAGE);
					if (returned != null) {
						String parts[] = returned.split(" ");
						String firstName = parts[0];
						String lastName = new String();
						for (int i = 1; i < parts.length; i++) {
							lastName += parts[i];
							if (i < parts.length - 1) {
								lastName += " ";
							}
						}
						Author tmp = new Author(firstName, lastName);
						if (!selectedBook.getAuthors().contains(tmp)) {
							selectedBook.addAuthor(tmp);
						}
						authorBox.addItem(returned);
					}
				} else if (source.equals(newGenreButton)) {
					String returned = JOptionPane.showInputDialog(null,
							"Input:", source.getName(),
							JOptionPane.PLAIN_MESSAGE);
					if (returned != null) {
						Genre tmp = new Genre(returned);
						if (!selectedBook.getGenres().contains(tmp)) {
							selectedBook.addGenre(tmp);
						}
						genreBox.addItem(returned);
					}
				} else if (source.equals(selectAuthorButton)
						|| source.equals(selectGenreButton)) {
					if (authors.size() == 0 || genres.size() == 0) {
						return;
					}
					Object arr[] = null;
					if (source.equals(selectAuthorButton)) {
						arr = authors.toArray();
					} else {
						arr = genres.toArray();
					}
					Object returned = JOptionPane.showInputDialog(source,
							"Select one from following box", source.getName(),
							JOptionPane.QUESTION_MESSAGE, null, arr, arr[0]);
					if (returned != null) {
						if (source.equals(selectAuthorButton)) {
							Author a = (Author) returned;
							authorBox.addItem(a.getFirstName() + " "
									+ a.getLastName());
							selectedBook.addAuthor(a);
						} else {
							Genre g = (Genre) returned;
							genreBox.addItem(g.getGenre());
							selectedBook.addGenre(g);
						}
					}
				}
				MabisLogger.getLogger().log(Level.INFO,
						"Action called by clicking at {0}", source.getName());
			}
		}
	}
}