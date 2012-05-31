/**
 * 
 */
package view.mainwindow;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import view.items.ItemCreator;
import view.items.creators.AudioAlbumCreator;
import view.items.creators.BookCreator;
import view.items.creators.MovieCreator;
import view.items.itemsprieview.ItemsPreview;
import view.mainwindow.collectionTable.CollectionCell;
import view.mainwindow.collectionTable.CollectionTableModel;
import view.utilities.StatusBar;
import controller.SQLFactory;
import controller.SQLStamentType;
import controller.entity.AudioAlbumSQLFactory;
import controller.entity.AudioUserSQLFactory;
import controller.entity.BookSQLFactory;
import controller.entity.BookUserSQLFactory;
import controller.entity.MovieSQLFactory;
import controller.entity.MovieUserSQLFactory;

//TODO add comments
public class MWCollectionView extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 4037649477948033295L;
	private static final String DELETE_CMD = "Delete",
							    EDIT_CMD = "Edit",
							    VIEW_CMD = "VieW";
	private JPopupMenu collectionMenu;
	private User connectedUserReference;

	private JScrollPane scrollPanel = null;
	private CollectionView currentView;

	private final CollectionMediator mediator = new CollectionMediator();
	private DefaultTableModel tableModel;
	private JTable collectionTable;
	private PopupActionListener collectionMenuListener;
	private StatusBar statusBar;

	public MWCollectionView(MainWindow mw,LayoutManager layout, boolean isDoubleBuffered) {
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
		this.collectionTable.setDefaultRenderer(BaseTable.class, new CollectionCell());
		this.collectionTable.setRowHeight(200);
		this.scrollPanel = new JScrollPane(this.collectionTable,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.add(this.scrollPanel);
		initPopupMenu();
	}

	private void initPopupMenu() {
		this.collectionMenu = new JPopupMenu("Collection popup menu");
		this.collectionMenuListener = new PopupActionListener();
		
		class CollectionTableMouseListener extends MouseAdapter implements MouseListener{
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.isPopupTrigger()){
					collectionMenu.show(e.getComponent(),e.getX(),e.getY());
					collectionMenuListener.currentRow = collectionTable.rowAtPoint(e.getPoint());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger()){
					collectionMenu.show(e.getComponent(),e.getX(),e.getY());
					collectionMenuListener.currentRow = collectionTable.rowAtPoint(e.getPoint());
				}
			}
			
		}
		
		JMenuItem view = new JMenuItem(VIEW_CMD); 
		JMenuItem edit = new JMenuItem(EDIT_CMD);
		JMenuItem remove = new JMenuItem(DELETE_CMD);
		view.addActionListener(this.collectionMenuListener); this.collectionMenu.add(view);
		edit.addActionListener(this.collectionMenuListener); this.collectionMenu.add(edit);
		remove.addActionListener(this.collectionMenuListener); this.collectionMenu.add(remove);
		this.collectionTable.addMouseListener(new CollectionTableMouseListener());
	}

	private void reprintCollection(CollectionView view) {
		this.currentView = view;
		
		class SmallPrinter {
			public void printAudios() {
				for (AudioAlbum a : mediator.collectedAlbums) {
					Object data[] = {a};
					tableModel.addRow(data);
				}
			}

			public void printBooks() {
				for (Book b : mediator.collectedBooks) {

					Object data[] = {b};
					tableModel.addRow(data);
				}
			}

			public void printMovies() {
				for (Movie m : mediator.collectedMovies) {
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
		collectionTable.revalidate();
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
		} else if (e.getPropertyName().equals(DELETE_CMD)){
			BaseTable bt = (BaseTable) e.getNewValue();
			switch(bt.getTableType()){
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
			this.statusBar.setMessage("Removed item :[" + bt.getPrimaryKey() + "] -> " + bt.getTitle());
		} else if(e.getPropertyName().equals(EDIT_CMD)){
			BaseTable bt = (BaseTable) e.getNewValue();
			ItemCreator ic = null;
			switch(bt.getTableType()){
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
				ic = new AudioAlbumCreator(connectedUserReference, bt.getTitle());
				ic.setEditableItem(bt);
				ic.setVisible(true);
				break;
			default:
				break;
			}
			this.statusBar.setMessage("Editing item :[" + bt.getPrimaryKey() + "] -> " + bt.getTitle());
		} else if(e.getPropertyName().equals(VIEW_CMD)){
			BaseTable bt = (BaseTable) e.getNewValue();
			ItemsPreview preview = new ItemsPreview(bt.getTitle(), bt);
			preview.setVisible(true);
			this.statusBar.setMessage("Previewed item :[" + bt.getPrimaryKey() + "] -> " + bt.getTitle());
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
	
	
	class PopupActionListener implements ActionListener{
		int currentRow = -1;
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) e.getSource();
			String command = source.getActionCommand();
			
			BaseTable bt = (BaseTable) tableModel.getValueAt(currentRow, 0);
			
			if(command.equals(VIEW_CMD)){
				
			}else if(command.equals(EDIT_CMD)){
				
			}else if(command.equals(DELETE_CMD)){
				SQLFactory sf = null;
				boolean wasDeleted = false;
				switch(bt.getTableType()){
				case MOVIE:
					try{
						sf = new MovieSQLFactory(SQLStamentType.DELETE, bt);
						sf.addWhereClause("idMovie", bt.getPrimaryKey().toString());
						sf.executeSQL(true);
						wasDeleted = true;
					}catch(SQLException e1){
						MabisLogger.getLogger().log(Level.WARNING,"Failed to remove {0}", bt.getTitle());
					}
					break;
				case BOOK:
					try{
						sf = new BookSQLFactory(SQLStamentType.DELETE, bt);
						sf.addWhereClause("idBook", bt.getPrimaryKey().toString());
						sf.executeSQL(true);
						wasDeleted = true;
					}catch(SQLException e1){
						MabisLogger.getLogger().log(Level.WARNING,"Failed to remove {0}", bt.getTitle());
					}
					break;
				case AUDIO_ALBUM:
					try{
						sf = new AudioAlbumSQLFactory(SQLStamentType.DELETE, bt);
						sf.addWhereClause("idAudio", bt.getPrimaryKey().toString());
						sf.executeSQL(true);
						wasDeleted = true;
					}catch(SQLException e1){
						MabisLogger.getLogger().log(Level.WARNING,"Failed to remove {0}", bt.getTitle());
					}
					break;
				default:
					break;
				}
				if(wasDeleted){
					tableModel.removeRow(currentRow);
					collectionTable.revalidate();
					firePropertyChange(command, null, bt);
				}
			}
			currentRow = -1;
		}
	}
}
