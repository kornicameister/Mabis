/**
 * 
 */
package view.mainwindow;

import java.awt.LayoutManager;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;

import controller.SQLStamentType;
import controller.entity.AudioAlbumSQLFactory;
import controller.entity.BookSQLFactory;
import controller.entity.MovieSQLFactory;

import model.entity.AudioAlbum;
import model.entity.Book;
import model.entity.Movie;

//TODO add comments
public class MWCollectionView extends JPanel {
	private static final long serialVersionUID = 4037649477948033295L;
	private JPopupMenu collectionMenu;

	private final HashMap<Integer, Movie> collectedMovies = new HashMap<Integer, Movie>();
	private final HashMap<Integer, Book> collectedBook = new HashMap<Integer, Book>();
	private final HashMap<Integer, AudioAlbum> collectedAudioAlbums = new HashMap<Integer, AudioAlbum>();
	private final CollectionMediator mediator = new CollectionMediator();

	public MWCollectionView(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.initComponents();
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Collection"));
		initPopupMenu();
	}

	private void initPopupMenu() {
		// TODO add listener
		this.collectionMenu = new JPopupMenu("Collection popup menu");
		collectionMenu.add(new JMenuItem("Edit"));
		collectionMenu.add(new JMenuItem("Remove"));
		collectionMenu.add(new JMenuItem("Publish/Unpublish"));
		collectionMenu.setSelected(this);
	}

	void loadLocalCollection() {

	}
	
	/**
	 * Klasa której głównym zadaniem jest pośredniczenie między kolekcją
	 * zlokalizowaną w bazie danych  (tj. danymi), a ich wizualną reprezentacją (tj. widokiem)
	 * który prezentowany jest użytkownikowi na {@link MWCollectionView}.
	 * 
	 * @author kornicameister
	 * @version 0.1
	 */
	private final class CollectionMediator {
		private final MovieSQLFactory msf = new MovieSQLFactory(SQLStamentType.SELECT, new Movie());  
		private final BookSQLFactory bsf = new BookSQLFactory(SQLStamentType.SELECT, new Book());
		private final AudioAlbumSQLFactory aasf = new AudioAlbumSQLFactory(SQLStamentType.SELECT, new AudioAlbum());
		
		public CollectionMediator() {
			
		}
	}
}
