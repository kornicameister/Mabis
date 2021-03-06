/**
 * 
 */
package mvc.view.items;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;

import logger.MabisLogger;
import mvc.controller.SQLStamentType;
import mvc.controller.entity.AuthorSQLFactory;
import mvc.controller.entity.GenreSQLFactory;
import mvc.controller.exceptions.SQLEntityExistsException;
import mvc.model.BaseTable;
import mvc.model.entity.Author;
import mvc.model.entity.Genre;
import mvc.model.entity.User;
import mvc.model.enums.AuthorType;
import mvc.model.enums.GenreType;
import mvc.view.MabisFrameInterface;
import mvc.view.WindowClosedListener;
import mvc.view.imagePanel.ImageFileFilter;
import mvc.view.imagePanel.ImageFilePreview;
import mvc.view.imagePanel.ImagePanel;
import mvc.view.items.minipanels.AuthorMiniPanel;
import mvc.view.items.minipanels.BandMiniPanel;
import mvc.view.items.minipanels.SearchMiniPanel;
import mvc.view.items.minipanels.TagCloudMiniPanel;

/**
 * Klasa bazowa kreatora nowego obiektu dla kolekcji. Definiuje podstawową
 * strukturę okna, jego layout oraz zachowanie niezależne od typu definiowanego
 * obiektu dla kolekcji. {@link ItemCreator} może być także użyty do edycji
 * istniejącego obiektu kolekcji.
 * 
 * @author kornicameister
 * 
 */
public abstract class ItemCreator extends JFrame implements MabisFrameInterface {
	private static final long serialVersionUID = -2333519518489232774L;
	protected JPanel contentPanel;
	private JPanel contentPane;
	private ICButtonPanel buttonPanel;
	private ICActionListener listener;
	protected TreeSet<BaseTable> collectedItems;
	protected SearchMiniPanel searchPanel;
	protected User selectedUser;
	protected boolean editingMode;
	protected BaseTable editedItem;
	protected ImagePanel coverPanel;
	protected GenreType genreType;
	protected TagCloudMiniPanel tagCloud;
	protected AuthorMiniPanel authorMiniPanel;
	protected AuthorType authorType;

	/**
	 * Konstruktor klasy bazowej kreatora nowego obiektu
	 * 
	 * @param title
	 *            tytuł okna
	 * @param genreType
	 * @throws HeadlessException
	 * @throws CreatorContentNullPointerException
	 */
	public ItemCreator(User u, String title, GenreType genreType, AuthorType at) {
		super(title);
		this.editingMode = false;
		this.selectedUser = u;
		this.genreType = genreType;
		this.authorType = at;
		this.initializeTagCloud();
		this.initializeAuthorsMiniPanel();
		this.initComponents();
		this.layoutComponents();

		setDefaultLookAndFeelDecorated(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowClosedListener());
	}

	@Override
	public void layoutComponents() {
		this.contentPane = new JPanel(true);
		this.contentPane.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));

		this.setContentPane(this.contentPane);

		GroupLayout gl = new GroupLayout(getContentPane());
		getContentPane().setLayout(gl);

		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);

		gl.setHorizontalGroup(gl
				.createParallelGroup()
				.addComponent(this.searchPanel)
				.addGroup(
						gl.createSequentialGroup().addComponent(
								this.contentPanel))
				.addComponent(this.buttonPanel));
		gl.setVerticalGroup(gl
				.createSequentialGroup()
				.addComponent(this.searchPanel, 100, 120, 140)
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.contentPanel))
				.addComponent(this.buttonPanel, 70, 80, 105));

		this.pack();
	}

	@Override
	public void initComponents() throws CreatorContentNullPointerException {
		this.listener = new ICActionListener();
		this.buttonPanel = new ICButtonPanel();
		this.buttonPanel.setBorder(BorderFactory.createTitledBorder("Buttons"));
		this.searchPanel = new SearchMiniPanel();
		this.searchPanel.setBorder(BorderFactory
				.createTitledBorder("Search using API"));
		this.searchPanel
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals("query")) {
							fetchFromAPI((String) evt.getNewValue(),
									(String) evt.getOldValue());
						} else if (evt.getPropertyName().equals("cancelSearch")) {
							cancelAPISearch();
						}
					}
				});

		this.coverPanel = new ImagePanel();
		this.coverPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() > 1) {
					JFileChooser imageChooser = new JFileChooser();
					imageChooser.setFileFilter(new ImageFileFilter());
					imageChooser
							.setAccessory(new ImageFilePreview(imageChooser));
					if (imageChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						coverPanel.setImage(imageChooser.getSelectedFile());
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				if (coverPanel.getImageFile() != null) {
					coverPanel.setToolTipText("<html><body><b>Cover:</b></br>"
							+ coverPanel.getImageFile().getName()
							+ "</body></html>");
				}
			}
		});
	}
	/**
	 * Metoda wywoływana po naciśnięciu {@link ICButtonPanel#clearButton}.
	 * Efektem jej wywołania powinno być wyczyszczenie wszystkich kretora,
	 * łącznie z ustawieniem okładki na okładkę domyślną
	 * 
	 * @see ICButtonPanel
	 */
	protected abstract void clearContentFields();

	/**
	 * Metoda wywoływana po kliknięciu {@link ICButtonPanel#createButton}.
	 * Powoduje pobranie danych z pól danego kreatora, utworzenie adekwatnego
	 * obiektu i podjęcie próby umieszczenia go w bazie danych.
	 * 
	 * @return true - jeśli item kolekcji został utworzony, false w innym
	 *         wypadku
	 */
	protected abstract Boolean execute();

	/**
	 * Po wywołeniu akcji, podejmowana jest próba pobrania informacji o danym
	 * obiekcie kolekcji przez publiczne API. Wszystkie reimplementacje tej
	 * metody używają klasy {@link SwingWorker} aby nie blokować głównego okna i
	 * cały proces pobierania danych wykonać w tle.
	 * 
	 * @param query
	 *            o co pytamy api
	 * @param criteria
	 *            typ zapytania, może to być np. szukaj <b>query</b> po autorach
	 * 
	 */
	protected abstract void fetchFromAPI(String query, String criteria);

	/**
	 * Metoda pozwala na zakończenie procesu wyszukiwania.
	 */
	protected abstract void cancelAPISearch();

	/**
	 * Metoda powinna zostać wywołana po wybraniu przez użytkownika elementu
	 * zwróconego za pomocą dowolnego api
	 * 
	 * @param table
	 *            tabela z danymi
	 */
	protected abstract void fillWithResult(BaseTable table);

	/**
	 * Prosta metoda do ustawienia obiektu kolekcji. Używana głównie jeśli
	 * obiekt ma być edytowany !
	 * 
	 * @param bt
	 */
	public void setEditableItem(BaseTable bt) {
		this.editingMode = true;
		this.fillWithResult(bt);
		this.searchPanel.setVisible(false);
		this.buttonPanel.createButton.setText("Update");
		this.buttonPanel.clearButton.setVisible(false);
		this.editedItem = bt;
	}

	private void initializeTagCloud() {
		try {
			GenreSQLFactory gsf = new GenreSQLFactory(SQLStamentType.SELECT,
					new Genre());
			gsf.addWhereClause("type", this.genreType.toString());
			gsf.executeSQL(true);
			this.tagCloud = new TagCloudMiniPanel(gsf.getGenres(),
					GenreType.AUDIO);
			this.tagCloud.setBorder(BorderFactory
					.createTitledBorder("Tag cloud"));
		} catch (SQLException | SQLEntityExistsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metoda inicjalizuje mini panel dla autorow elementow kolekcji, zgodnie z
	 * natura uruchomionego kreatora. Poprzedzona slowem
	 * <strong>protected</strong> poniewaz {@link BandMiniPanel} nie jest do
	 * konca tym samym co {@link AuthorMiniPanel}, niemniej Band jest dzieckiem
	 * {@link Author} w hierarchi dziedziczenia klas, wiec wydaje sie to
	 * calkowicie naturalne, ze te dwa mini panele dziedzicza od siebie.
	 * 
	 * @see ItemCreator#authorType
	 */
	protected void initializeAuthorsMiniPanel() {
		try {
			AuthorSQLFactory asf = new AuthorSQLFactory(SQLStamentType.SELECT,
					new Author());
			asf.addWhereClause("type", this.authorType.toString());
			asf.executeSQL(true);
			this.authorMiniPanel = new AuthorMiniPanel(asf.getAuthors(),
					this.authorType);
			this.authorMiniPanel.setBorder(BorderFactory
					.createTitledBorder("Authors"));
		} catch (SQLException | SQLEntityExistsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Klasa rozszerzająca JPanel, stanowi kontener dla przycisków sterujących
	 * działaniem kreatora
	 * 
	 * @author tomasz
	 * 
	 */
	private class ICButtonPanel extends JPanel {
		private static final long serialVersionUID = -169864232599710877L;
		private JButton createButton;
		private JButton clearButton;
		private JButton cancelButton;

		public ICButtonPanel() {
			this.createButton = new JButton("Create");
			this.createButton.addActionListener(listener);
			this.clearButton = new JButton("Clear");
			this.clearButton.addActionListener(listener);
			this.cancelButton = new JButton("Cancel");
			this.cancelButton.addActionListener(listener);

			this.setLayout(new GridLayout(1, 3));
			this.add(this.createButton);
			this.add(this.clearButton);
			this.add(this.cancelButton);
		}
	}

	/**
	 * {@link ActionListener} dla {@link ItemCreator}. Nasłuchuje wykonanie
	 * akcji dla przycisków umieszczonych bezpośrednio na {@link ICButtonPanel}.
	 * 
	 * @author kornicameister
	 * 
	 */
	private class ICActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent s) {
			JButton source = (JButton) s.getSource();
			MabisLogger.getLogger().log(Level.INFO,
					"ItemCreator action called :: {0}",
					source.getActionCommand());
			if (source.equals(buttonPanel.cancelButton)) {
				setVisible(false);
				dispose();
			} else if (source.equals(buttonPanel.clearButton)) {
				clearContentFields();
			} else if (source.equals(buttonPanel.createButton)) {
				if (!execute()) {
					JOptionPane.showMessageDialog(getParent(),
							"Failed to create new item",
							"Creation miracle failed",
							JOptionPane.ERROR_MESSAGE);
				} else {
					setVisible(false);
					dispose();
				}
			}
			MabisLogger.getLogger().log(Level.INFO,
					"ItemCreator action called :: {0}",
					source.getActionCommand());
		}

	}

}