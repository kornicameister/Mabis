/**
 * 
 */
package mvc.view.mainwindow;

import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import logger.MabisLogger;
import mvc.controller.SQLFactory;
import mvc.controller.SQLStamentType;
import mvc.controller.entity.AudioAlbumSQLFactory;
import mvc.controller.entity.AudioUserSQLFactory;
import mvc.controller.entity.BookSQLFactory;
import mvc.controller.entity.BookUserSQLFactory;
import mvc.controller.entity.MovieSQLFactory;
import mvc.controller.entity.MovieUserSQLFactory;
import mvc.controller.exceptions.SQLEntityExistsException;
import mvc.model.BaseTable;
import mvc.model.entity.AudioAlbum;
import mvc.model.entity.AudioUser;
import mvc.model.entity.Author;
import mvc.model.entity.Book;
import mvc.model.entity.BookUser;
import mvc.model.entity.Movie;
import mvc.model.entity.MovieUser;
import mvc.model.entity.User;
import mvc.model.utilities.BookIndustryIdentifier;
import mvc.model.utilities.ForeignKeyPair;
import mvc.view.enums.CollectionView;
import mvc.view.items.ItemCreator;
import mvc.view.items.creators.AudioAlbumCreator;
import mvc.view.items.creators.BookCreator;
import mvc.view.items.creators.MovieCreator;
import mvc.view.items.itemsprieview.ItemsPreview;
import mvc.view.mainwindow.collectionTable.CollectionCell;
import mvc.view.mainwindow.collectionTable.CollectionTableModel;
import mvc.view.utilities.StatusBar;

//TODO add comments
public class MWCollectionView extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 4037649477948033295L;
	private static final String DELETE_CMD = "Delete", EDIT_CMD = "Edit",
			VIEW_CMD = "View";
	private JPopupMenu collectionMenu;
	private User connectedUserReference;

	private JScrollPane scrollPanel = null;
	private CollectionView currentView;

	private final CollectionMediator mediator = new CollectionMediator();
	private DefaultTableModel tableModel;
	private JTable collectionTable;
	private PopupActionListener collectionMenuListener;
	private StatusBar statusBar;
	private String currentGroupBy = "";

	public MWCollectionView(MainWindow mw, LayoutManager layout,
			boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.initComponents();
		this.statusBar = mw.getBottomPanel().getStatusBar();
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Collection"));

		this.tableModel = new CollectionTableModel();
		this.collectionTable = new JTable(tableModel);
		this.collectionTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		this.collectionTable.getColumnModel().getColumn(0).setMaxWidth(220);
		this.collectionTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		this.collectionTable.getColumnModel().getColumn(0).setMinWidth(180);
		this.collectionTable.setRowHeight(200);
		this.collectionTable.setDefaultRenderer(BaseTable.class,new CollectionCell());
		this.scrollPanel = new JScrollPane(this.collectionTable);

		this.add(this.scrollPanel);
		initPopupMenu();
	}

	private void initPopupMenu() {
		this.collectionMenu = new JPopupMenu("Collection popup menu");
		this.collectionMenuListener = new PopupActionListener();

		class CollectionTableMouseListener extends MouseAdapter implements
				MouseListener {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					collectionMenu.show(e.getComponent(), e.getX(), e.getY());
					collectionMenuListener.currentRow = collectionTable
							.rowAtPoint(e.getPoint());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					collectionMenu.show(e.getComponent(), e.getX(), e.getY());
					collectionMenuListener.currentRow = collectionTable
							.rowAtPoint(e.getPoint());
				}
			}

		}

		JMenuItem view = new JMenuItem(VIEW_CMD);
		JMenuItem edit = new JMenuItem(EDIT_CMD);
		JMenuItem remove = new JMenuItem(DELETE_CMD);
		view.addActionListener(this.collectionMenuListener);
		this.collectionMenu.add(view);
		edit.addActionListener(this.collectionMenuListener);
		this.collectionMenu.add(edit);
		remove.addActionListener(this.collectionMenuListener);
		this.collectionMenu.add(remove);
		this.collectionTable.addMouseListener(new CollectionTableMouseListener());
	}

	private void reprintCollection(CollectionView view) {
		this.currentView = view;
		this.clearCollectionView();

		class SmallPrinter {
			public void printAudios() {
				for (AudioAlbum a : mediator.collectedAlbums) {
					Object data[] = { this.getScaledImage(a) ,a};
					tableModel.addRow(data);
				}
			}

			public void printBooks() {
				for (Book b : mediator.collectedBooks) {

					Object data[] = {this.getScaledImage(b), b};
					tableModel.addRow(data);
				}
			}

			public void printMovies() {
				for (Movie m : mediator.collectedMovies) {
					Object data[] = {this.getScaledImage(m), m};
					tableModel.addRow(data);
				}
			}

			private ImageIcon getScaledImage(BaseTable t) {
				File imagePath = ((Movie)t).getCover().getImageFile();
				ImageIcon i = new ImageIcon(imagePath.getAbsolutePath());
				
				final double height = 180.0;
				double a = height / i.getIconHeight();
				double newWidth = a * i.getIconWidth();
				
				return new ImageIcon(i.getImage().getScaledInstance((int) newWidth, (int) height, Image.SCALE_FAST));
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
		collectionTable.revalidate();
	}

	private void clearCollectionView() {
		if (this.tableModel.getRowCount() > 0) {
			for (int i = this.tableModel.getRowCount() - 1; i != 0; i--) {
				this.tableModel.removeRow(i);
			}
			this.tableModel.removeRow(0);
			if (this.tableModel.getRowCount() != 0) {
				MabisLogger
						.getLogger()
						.log(Level.SEVERE,
								"Not all rows were removed during collection reprinting");
			}
			this.collectionTable.revalidate();
		}
	}

	private void groupViewBy(String value) {
		class TitleComparator implements Comparator<BaseTable> {
			@Override
			public int compare(BaseTable a, BaseTable b) {
				int res = a.getTitle().compareTo(b.getTitle());
				if(res == 0){
					res = a.getSubtitle().compareTo(b.getSubtitle());
				}
				return res;
			}
		}
		
		this.currentGroupBy = value;
		
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
			}else if (value.equals("Title")) {
				Collections.sort(mediator.collectedAlbums,new TitleComparator());
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
				Collections.sort(mediator.collectedBooks,
						new Comparator<Book>() {
							private BookIndustryIdentifier bii2, bii1;

							@Override
							public int compare(Book o1, Book o2) {
								Object[] b1 = o1.getIdentifiers().toArray();
								Object[] b2 = o2.getIdentifiers().toArray();
								int res = 0;
								for (int i = 0 ; i < (b1.length > b2.length ? b2.length : b1.length) ; i++) {
									bii1 = (BookIndustryIdentifier) b1[i];
									bii2 = (BookIndustryIdentifier) b2[i];
									res = bii1.compareTo(bii2);
									if(res != 0){
										return res;
									}
								}
								return res;
							}
						});
			}else if (value.equals("Title")) {
				Collections.sort(mediator.collectedBooks,new TitleComparator());
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
			}else if (value.equals("Title")) {
				Collections.sort(mediator.collectedMovies,new TitleComparator());
			}
			break;
		default:
			if (value.equals("Title")) {
				Collections.sort(mediator.collectedBooks,new TitleComparator());
			}else{
				Collections.sort(mediator.collectedBooks);
			}
			if (value.equals("Title")) {
				Collections.sort(mediator.collectedMovies,new TitleComparator());
			}else{
				Collections.sort(mediator.collectedMovies);
			}
			if (value.equals("Title")) {
				Collections.sort(mediator.collectedAlbums,new TitleComparator());
			}else{
				Collections.sort(mediator.collectedAlbums);
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
		} else if (e.getPropertyName().equals(DELETE_CMD)) {
			BaseTable bt = (BaseTable) e.getNewValue();
			this.statusBar.setMessage("Removed item :[" + bt.getPrimaryKey()
					+ "] -> " + bt.getTitle());
			switch (bt.getTableType()) {
			case MOVIE:
				mediator.collectedMovies.remove(bt);
				break;
			case BOOK:
				mediator.collectedBooks.remove(bt);
				break;
			case AUDIO_ALBUM:
				mediator.collectedAlbums.remove(bt);
				break;
			default:
				break;
			}
		} else if (e.getPropertyName().equals(EDIT_CMD)) {
			BaseTable bt = (BaseTable) e.getNewValue();
			this.statusBar.setMessage("Editing item :[" + bt.getPrimaryKey()
					+ "] -> " + bt.getTitle());
			ItemCreator ic = null;
			switch (bt.getTableType()) {
			case MOVIE:
				ic = new MovieCreator(connectedUserReference, bt.getTitle());
				ic.setEditableItem(bt);
				ic.setVisible(true);
				break;
			case BOOK:
				ic = new BookCreator(connectedUserReference, bt.getTitle());
				ic.setEditableItem(bt);
				ic.setVisible(true);
				break;
			case AUDIO_ALBUM:
				ic = new AudioAlbumCreator(connectedUserReference,
						bt.getTitle());
				ic.setEditableItem(bt);
				ic.setVisible(true);
				break;
			default:
				break;
			}
		} else if (e.getPropertyName().equals(VIEW_CMD)) {
			BaseTable bt = (BaseTable) e.getNewValue();
			this.statusBar.setMessage("Previewed item :[" + bt.getPrimaryKey()
					+ "] -> " + bt.getTitle());
			ItemsPreview preview = new ItemsPreview(bt.getTitle(), bt);
			preview.setVisible(true);
		} else if (e.getPropertyName().equals("itemAffected")) {
			BaseTable bt = (BaseTable) e.getNewValue();
			this.statusBar.setMessage("Affected item :[" + bt.getPrimaryKey() + "] -> " + bt.getTitle());
			switch (bt.getTableType()) {
			case MOVIE:
				this.mediator.collectedMovies.add((Movie) bt);
				break;
			case BOOK:
				this.mediator.collectedBooks.add((Book) bt);
				break;
			case AUDIO_ALBUM:
				this.mediator.collectedAlbums.add((AudioAlbum) bt);
				break;
			default:
				break;
			}
			this.reprintCollection(this.currentView);
			this.groupViewBy(this.currentGroupBy);
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
		try {
			this.mediator.loadCollection();
		} catch (SQLEntityExistsException e) {
			e.printStackTrace();
		}
		this.reprintCollection(CollectionView.VIEW_ALL);
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

		void loadCollection() throws SQLException, SQLEntityExistsException {
			loadAudios();
			loadBooks();
			loadMovies();
			Integer params[] = { 
					tableModel.getRowCount(),
					this.collectedAlbums.size(),
					this.collectedBooks.size(),
					this.collectedMovies.size() };
			MabisLogger
					.getLogger()
					.log(Level.INFO,
							"Collected {0} items from database, including {1} audios, {2} books and {3} movies",
							params);
		}

		private void loadMovies() throws SQLException, SQLEntityExistsException {
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
		}

		private void loadAudios() throws SQLException, SQLEntityExistsException {
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
		}

		private void loadBooks() throws SQLException, SQLEntityExistsException {
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
		}
	}

	class PopupActionListener implements ActionListener {
		int currentRow = -1;

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) e.getSource();
			String command = source.getActionCommand();

			BaseTable bt = (BaseTable) tableModel.getValueAt(currentRow, 1);

			if (command.equals(VIEW_CMD) || command.equals(EDIT_CMD)) {
				propertyChange(new PropertyChangeEvent(this, command, null, bt));
			} else if (command.equals(DELETE_CMD)) {
				SQLFactory sf = null;
				boolean wasDeleted = false;
				try {
					switch (bt.getTableType()) {
					case MOVIE:
						sf = new MovieSQLFactory(SQLStamentType.DELETE, bt);
						sf.addWhereClause("idMovie", bt.getPrimaryKey()
								.toString());
						sf.executeSQL(true);
						wasDeleted = true;
						break;
					case BOOK:
						sf = new BookSQLFactory(SQLStamentType.DELETE, bt);
						sf.addWhereClause("idBook", bt.getPrimaryKey()
								.toString());
						sf.executeSQL(true);
						wasDeleted = true;
						break;
					case AUDIO_ALBUM:
						sf = new AudioAlbumSQLFactory(SQLStamentType.DELETE, bt);
						sf.addWhereClause("idAudioAlbum", bt.getPrimaryKey()
								.toString());
						sf.executeSQL(true);
						wasDeleted = true;
						break;
					default:
						break;
					}
				} catch (SQLException | SQLEntityExistsException e1) {
					MabisLogger.getLogger().log(Level.WARNING,
							"Failed to remove {0}", bt.getTitle());
				}
				if (wasDeleted) {
					tableModel.removeRow(currentRow);
					collectionTable.revalidate();
					propertyChange(new PropertyChangeEvent(this, command, null,
							bt));
				}
			}
			currentRow = -1;
		}
	}
}
