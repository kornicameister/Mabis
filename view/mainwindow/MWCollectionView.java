/**
 * 
 */
package view.mainwindow;

import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;

import controller.SQLStamentType;
import controller.entity.AudioAlbumSQLFactory;
import controller.entity.BookSQLFactory;
import controller.entity.MovieSQLFactory;

import logger.MabisLogger;
import model.entity.AudioAlbum;
import model.entity.Book;
import model.entity.Movie;
import model.enums.TableType;

//TODO add comments
public class MWCollectionView extends JPanel implements PropertyChangeListener {
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
	 * zlokalizowaną w bazie danych (tj. danymi), a ich wizualną reprezentacją
	 * (tj. widokiem) który prezentowany jest użytkownikowi na
	 * {@link MWCollectionView}.
	 * 
	 * @author kornicameister
	 * @version 0.1
	 */
	private final class CollectionMediator {
		private final MovieSQLFactory msf = new MovieSQLFactory(
				SQLStamentType.FETCH_ALL, new Movie());
		private final BookSQLFactory bsf = new BookSQLFactory(
				SQLStamentType.FETCH_ALL, new Book());
		private final AudioAlbumSQLFactory aasf = new AudioAlbumSQLFactory(
				SQLStamentType.FETCH_ALL, new AudioAlbum());

		private String currentView, currentGroup;

		public CollectionMediator() {
			this.currentView = new String();
			this.currentGroup = new String();
		}

		void loadSelected(String display) {

			if (display.equals(this.currentView)) {
				// THIS IS ALREADY BEING DISPLAYED
				return;
			}
			this.currentView = display;
			if (display.equals(TableType.BOOK.toString())) {
				// load and print books only
				this.loadBooks();
			} else if (display.equals(TableType.AUDIO_ALBUM.toString())) {
				// load and print audio albums only
				this.loadAudios();
			} else if (display.equals(TableType.MOVIE.toString())) {
				// load and print movies only
				this.loadMovies();
			} else if (display.equals("all")) {
				// oooooooo print every item you can find in db, hell yeah
				this.loadAll();
			}
		}

		public void regroupView(String viewValue) {
			if (this.currentGroup.equals(viewValue)) {
				return;
			}
		}

		private void loadAll() {
			this.loadBooks();
			this.loadAudios();
			this.loadMovies();
		}

		private void loadMovies() {

		}

		private void loadAudios() {

		}

		private void loadBooks() {

		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		String group = null;
		String value = null;
		if (arg0.getNewValue() instanceof String
				&& arg0.getPropertyName() instanceof String) {
			value = (String) arg0.getNewValue();
			group = (String) arg0.getPropertyName();
			if (group.equals("viewAs")) {
				this.mediator.loadSelected(value);
				MabisLogger
						.getLogger()
						.log(Level.INFO,
								this.getClass().getName()
										+ ": updating collection view, current view set to {0}",
								value);
			} else {
				this.mediator.regroupView(value);
				MabisLogger
						.getLogger()
						.log(Level.INFO,
								this.getClass().getName()
										+ ": updating collection group, current group set to {0}",
								value);
			}
		}
	}
}
