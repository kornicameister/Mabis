/**
 * 
 */
package view.mainwindow;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import logger.MabisLogger;
import model.BaseTable;
import model.entity.AudioAlbum;
import model.entity.AudioUser;
import model.entity.Author;
import model.entity.Book;
import model.entity.BookUser;
import model.entity.Movie;
import model.entity.MovieUser;
import model.entity.User;
import model.utilities.ForeignKeyPair;
import view.enums.CollectionView;
import view.imagePanel.ImagePanel;
import view.mainwindow.collectionTable.CollectionCell;
import view.mainwindow.collectionTable.CollectionTableModel;
import controller.SQLStamentType;
import controller.entity.AudioAlbumSQLFactory;
import controller.entity.AudioUserSQLFactory;
import controller.entity.BookSQLFactory;
import controller.entity.BookUserSQLFactory;
import controller.entity.MovieSQLFactory;
import controller.entity.MovieUserSQLFactory;

//TODO add comments
public class MWCollectionView extends JPanel implements PropertyChangeListener {
	private static final Dimension THUMBAILSIZE = new Dimension(190, 220);
	private static final long serialVersionUID = 4037649477948033295L;
	private JPopupMenu collectionMenu;
	private User connectedUserReference;

	private JScrollPane scrollPanel = null;
	private CollectionView currentView;

	private final ThumbailDescriptor descriptor = new ThumbailDescriptor();
	private final CollectionMediator mediator = new CollectionMediator();
	private TreeMap<ImagePanel, BaseTable> thumbToEntity = new TreeMap<>();
	private DefaultTableModel tableModel;
	private JTable table;

	public MWCollectionView(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.initComponents();
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Collection"));

		this.tableModel = new CollectionTableModel();
		this.table = new JTable(tableModel);
		this.table.setDefaultRenderer(BaseTable.class, new CollectionCell());
		this.table.setRowHeight(200);
		this.scrollPanel = new JScrollPane(this.table,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.add(this.scrollPanel);
		initPopupMenu();
	}

	private void initPopupMenu() {
		this.collectionMenu = new JPopupMenu("Collection popup menu");
		collectionMenu.add(new JMenuItem("Edit"));
		collectionMenu.add(new JMenuItem("Remove"));
		collectionMenu.add(new JMenuItem("Publish/Unpublish"));
		collectionMenu.setSelected(this);
		this.scrollPanel.add(this.collectionMenu);
	}

	private void reprintCollection(CollectionView view) {
		this.currentView = view;
		
		class SmallPrinter {
			public void printAudios() {
				for (AudioAlbum a : mediator.collectedAlbums) {

					ImagePanel thumb = new ImagePanel(a.getCover()
							.getImageFile(), THUMBAILSIZE);
					thumb.setPreferredSize(MWCollectionView.THUMBAILSIZE);
					thumb.setMaximumSize(MWCollectionView.THUMBAILSIZE);
					thumb.setMinimumSize(MWCollectionView.THUMBAILSIZE);

					thumb.addPropertyChangeListener(descriptor);
					thumbToEntity.put(thumb, a);
					Object data[] = {a};
					tableModel.addRow(data);
				}
			}

			public void printBooks() {
				for (Book b : mediator.collectedBooks) {

					ImagePanel thumb = new ImagePanel(b.getCover()
							.getImageFile(), THUMBAILSIZE);
					thumb.setPreferredSize(MWCollectionView.THUMBAILSIZE);
					thumb.setMaximumSize(MWCollectionView.THUMBAILSIZE);
					thumb.setMinimumSize(MWCollectionView.THUMBAILSIZE);

					thumb.addPropertyChangeListener(descriptor);
					thumbToEntity.put(thumb, b);
					Object data[] = {b};
					tableModel.addRow(data);
				}
			}

			public void printMovies() {
				for (Movie m : mediator.collectedMovies) {

					ImagePanel thumb = new ImagePanel(m.getCover()
							.getImageFile(), THUMBAILSIZE);
					thumb.setPreferredSize(MWCollectionView.THUMBAILSIZE);
					thumb.setMaximumSize(MWCollectionView.THUMBAILSIZE);
					thumb.setMinimumSize(MWCollectionView.THUMBAILSIZE);

					thumb.addPropertyChangeListener(descriptor);
					thumbToEntity.put(thumb, m);
					Object data[] = {m};
					tableModel.addRow(data);
				}
			}
		}

		SmallPrinter sm = new SmallPrinter();

		switch (view) {
		case VIEW_AUDIOS:
			sm.printAudios();
			break;
		case VIEW_BOOKS:
			sm.printBooks();
			break;
		case VIEW_MOVIES:
			sm.printMovies();
			break;
		default:
			sm.printAudios();
			sm.printBooks();
			sm.printMovies();
			break;
		}
		table.revalidate();
		System.out.println(tableModel.getRowCount());
	}

	private void groupViewBy(String value) {
		switch (this.currentView) {
		case VIEW_AUDIOS:
			if (value.equals("Band")) {
				Collections.sort(mediator.collectedAlbums,
						new Comparator<AudioAlbum>() {

							@Override
							public int compare(AudioAlbum a0, AudioAlbum a1) {
								return a0.getBand().compareTo(a1.getBand());
							}
						});
			}
			break;
		case VIEW_BOOKS:
			if (value.equals("Author")) {
				Collections.sort(mediator.collectedBooks,
						new Comparator<Book>() {
							@Override
							public int compare(Book arg0, Book arg1) {
								Object[] b1 = arg0.getAuthors().toArray();
								Object[] b2 = arg1.getAuthors().toArray();
								int res = 0;
								for (int i = 0; i < (b1.length > b2.length ? b2.length
										: b1.length); i++) {
									res = ((Author) b1[i])
											.compareTo((Author) b2[i]);
									if (res != 0) {
										return res;
									}
								}
								return res;
							}
						});
			} else if (value.equals("ISBN")) {
				return;
			}
			break;
		case VIEW_MOVIES:
			if (value.equals("Director")) {
				Collections.sort(mediator.collectedMovies,
						new Comparator<Movie>() {
							@Override
							public int compare(Movie a, Movie b) {
								Object[] b1 = a.getAuthors().toArray();
								Object[] b2 = b.getAuthors().toArray();
								int res = 0;
								for (int i = 0; i < (b1.length > b2.length ? b2.length
										: b1.length); i++) {
									res = ((Author) b1[i])
											.compareTo((Author) b2[i]);
									if (res != 0) {
										return res;
									}
								}
								return res;
							}
						});
			}
			break;
		default:
			if (value.equals("Title")) {
				class TitleComparator implements Comparator<BaseTable> {
					@Override
					public int compare(BaseTable a, BaseTable b) {
						return a.getTitle().compareTo(b.getTitle()) * -1;
					}
				}
				Collections.sort(mediator.collectedAlbums,
						new TitleComparator());
				Collections
						.sort(mediator.collectedBooks, new TitleComparator());
				Collections.sort(mediator.collectedMovies,
						new TitleComparator());
			} else {
				Collections.sort(mediator.collectedAlbums);
				Collections.sort(mediator.collectedBooks);
				Collections.sort(mediator.collectedMovies);
			}
			break;
		}
		reprintCollection(currentView);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals("connectedUser")) {
			this.connectedUserReference = (User) e.getNewValue();
			MabisLogger
					.getLogger()
					.log(Level.INFO,
							this.getClass().getName()
									+ ": updating connected user info, connected user login:"
									+ this.connectedUserReference.getLogin());
			return;
		}

		if (e.getPropertyName().equals("zoomChanged")) {
			this.rezoomCollection((double) e.getNewValue());
		} else if (e.getPropertyName().equals("contentChanged")) {
			this.reprintCollection((CollectionView) e.getNewValue());
		} else if (e.getPropertyName().equals("groupByChanged")) {
			this.groupViewBy((String) e.getNewValue());
		}
	}

	private void rezoomCollection(double factor) {

	}

	/**
	 * Metoda ładuje lokalną kolecję na widok kolekcji. Pobiera dane do widoku
	 * kolekcji poprzez mediatora ( {@link CollectionMediator} ). <b>Ważne: </b>
	 * Metoda do poprawnego działania wymaga połączenie z bazą danych oraz
	 * zweryfikowanego użytkownika, który jest zalogowany w aplikacji. Metoda
	 * wywoływana powinna być tylko raz, zaraz po nawiązaniu połączenia i
	 * zalogowaniu się przez użytownika do programu.
	 * 
	 * @throws SQLException
	 */
	// TODO dodać ładowania według predefiniowanych ustawień !!!
	public void loadCollection() throws SQLException {
		this.mediator.loadCollection();
		this.reprintCollection(CollectionView.VIEW_ALL);
	}

	/**
	 * Klasa tworząca popup z opisem dla danego obiektu kolekcji. Nasłuchuje
	 * zmian właściwości <b>focusable</b> dla ImagePanel.
	 * 
	 * @author kornicameister
	 * @see ImagePanel
	 * @see MWCollectionView#thumbToEntity
	 */
	class ThumbailDescriptor implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("focusable")) {
				boolean hasFocus = (boolean) evt.getNewValue();
				if (!hasFocus) {
					MabisLogger.getLogger().log(Level.INFO,
							"No focus at the moment, hiding popup description");
					return;
				}
				final BaseTable bt = thumbToEntity.get((ImagePanel) evt
						.getSource());
				final ImagePanel panel = (ImagePanel) evt.getSource();
				panel.setToolTipText(bt.getTitle());
				MabisLogger.getLogger().log(Level.INFO,
						"Focus gained, showing popup");
			}
		}

	}

	/**
	 * Klasa której głównym zadaniem jest pośredniczenie między kolekcją
	 * zlokalizowaną w bazie danych (tj. danymi), a ich wizualną reprezentacją
	 * (tj. widokiem) który prezentowany jest użytkownikowi na
	 * {@link MWCollectionView}.
	 * 
	 * @author kornicameister
	 * @version 0.1
	 */
	private final class CollectionMediator {
		private ArrayList<Movie> collectedMovies = new ArrayList<>();
		private ArrayList<Book> collectedBooks = new ArrayList<>();
		private ArrayList<AudioAlbum> collectedAlbums = new ArrayList<>();

		void loadCollection() throws SQLException {
			loadAudios();
			loadBooks();
			loadMovies();
		}

		private void loadMovies() throws SQLException {
			MovieUserSQLFactory musf = new MovieUserSQLFactory(
					SQLStamentType.SELECT, new MovieUser());
			musf.addWhereClause("idUser", connectedUserReference
					.getPrimaryKey().toString());
			musf.executeSQL(true); // have all pair of keys from movieUser
			TreeSet<ForeignKeyPair> keys = musf.getMovieUserKeys();
			if (keys.isEmpty()) {
				return;
			}
			MovieSQLFactory msf = new MovieSQLFactory(SQLStamentType.SELECT,
					new Movie());
			for (ForeignKeyPair fkp : keys) {
				msf.addWhereClause("idMovie", fkp.getKey("idMovie").getValue()
						.toString());
			}
			msf.executeSQL(true);
			this.collectedMovies.addAll(msf.getMovies());
			MabisLogger.getLogger().log(Level.INFO,
					"Succesffuly obtained {0} movies for collection view",
					this.collectedMovies.size());
		}

		private void loadAudios() throws SQLException {
			AudioUserSQLFactory ausf = new AudioUserSQLFactory(
					SQLStamentType.SELECT, new AudioUser());
			ausf.addWhereClause("idUser", connectedUserReference
					.getPrimaryKey().toString());
			ausf.executeSQL(true); // have all pair of keys from movieUser
			TreeSet<ForeignKeyPair> keys = ausf.getAudioUserKeys();
			if (keys.isEmpty()) {
				return;
			}
			AudioAlbumSQLFactory aasf = new AudioAlbumSQLFactory(
					SQLStamentType.SELECT, new AudioAlbum());
			for (ForeignKeyPair fkp : keys) {
				aasf.addWhereClause("idAudioAlbum", fkp.getKey("idAudio")
						.getValue().toString());

			}
			aasf.executeSQL(true);
			this.collectedAlbums.addAll(aasf.getValues());
			MabisLogger
					.getLogger()
					.log(Level.INFO,
							"Succesffuly obtained {0} audio albums for collection view",
							this.collectedAlbums.size());
		}

		private void loadBooks() throws SQLException {
			BookUserSQLFactory ausf = new BookUserSQLFactory(
					SQLStamentType.SELECT, new BookUser());
			ausf.addWhereClause("idUser", connectedUserReference
					.getPrimaryKey().toString());
			ausf.executeSQL(true); // have all pair of keys from movieUser
			TreeSet<ForeignKeyPair> keys = ausf.getBookUserKeys();
			if (keys.isEmpty()) {
				return;
			}
			BookSQLFactory aasf = new BookSQLFactory(SQLStamentType.SELECT,
					new Book());
			for (ForeignKeyPair fkp : keys) {
				aasf.addWhereClause("idBook", fkp.getKey("idBook").getValue()
						.toString());

			}
			aasf.executeSQL(true);
			this.collectedBooks.addAll(aasf.getBooks());
			MabisLogger.getLogger().log(Level.INFO,
					"Succesffuly obtained {0} books for collection view",
					this.collectedBooks.size());
		}

	}
}
