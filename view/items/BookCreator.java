/**
 * 
 */
package view.items;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import model.entity.Author;
import model.entity.Genre;
import settings.GlobalPaths;
import view.imagePanel.ChoosableImagePanel;
import view.imagePanel.ImagePanel;
import controller.api.GoogleBookApi;

/**
 * @author kornicameister
 * 
 */
public class BookCreator extends ItemCreator {
	private static final long serialVersionUID = 6954574313564241105L;

	/**
	 * {@link ChoosableImagePanel} z okładką danej książki. Użytkownik może
	 * wybrać okładkę sam lub zostanie ona pobrana z internetu.
	 */
	private ImagePanel coverPanel;
	/**
	 * {@link JTextArea} w której będzie widoczny opis książki.
	 */
	private JTextArea descriptionArea;
	/**
	 * referencje do {@link TitlesPanel}
	 */
	private TitlesPanel tp;
	/**
	 * referencja do {@link DetailedInformationPanel}
	 */
	private DetailedInformationPanel dip;

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
	public JPanel initContent() {
		JPanel p = new JPanel(true);
		p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		GroupLayout gl = new GroupLayout(p);
		p.setLayout(gl);

		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);

		tp = new TitlesPanel(true);
		dip = new DetailedInformationPanel(true);
		coverPanel = new ImagePanel(new File(
				GlobalPaths.DEFAULT_COVER_PATH.toString()));
		coverPanel
				.setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
						"Cover"));
		descriptionArea = new JTextArea();
		descriptionArea.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"Descripion"));

		gl.setHorizontalGroup(gl
				.createParallelGroup()
				.addGroup(
						gl.createSequentialGroup()
								.addComponent(this.coverPanel)
								.addGroup(
										gl.createParallelGroup()
												.addComponent(this.tp)
												.addComponent(this.dip)))
				.addComponent(this.descriptionArea));
		gl.setVerticalGroup(gl
				.createSequentialGroup()
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.coverPanel,
										GroupLayout.DEFAULT_SIZE, 200, 220)
								.addGroup(
										gl.createSequentialGroup()
												.addComponent(
														this.tp,
														GroupLayout.DEFAULT_SIZE,
														60, 80)
												.addComponent(
														this.dip,
														GroupLayout.DEFAULT_SIZE,
														100, 120)))
				.addComponent(this.descriptionArea));

		this.revalidate();
		this.pack();
		return p;
	}

	@Override
	protected void clearContentFields() {
		this.coverPanel.setImage(new File(GlobalPaths.DEFAULT_COVER_PATH
				.toString()));
		this.descriptionArea.setText("");
		this.tp.clear();
		this.dip.clear();
	}

	@Override
	protected Boolean createItem() {
		return false;
	}

	@Override
	protected void fetchFromAPI() {
		GoogleBookApi gba = new GoogleBookApi();
		try {
			TreeMap<String, String> params = new TreeMap<String, String>();
			params.put("inauthor:", "Stephen King");
			gba.query("Carrie", params);
			collectedItems = gba.getResult();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Panel agregujący. Umieszczone są nim pola, służące do wprowadzania
	 * tytułów
	 * 
	 * @author kornicameister
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
		private final JTextField titleLocale = new JTextField();

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
		}

		/**
		 * Czyści tekst, który był widoczny w {@link JTextField} dla tytułów
		 */
		protected void clear() {
			this.titleLocale.setText("");
			this.titleOriginal.setText("");
		}

		/**
		 * 
		 */
		private void layoutComponents() {
			this.setLayout(new GridLayout(2, 1, 5, 5));
			this.add(this.titleOriginal);
			this.add(this.titleLocale);
		}

		/**
		 * @return pole tekstowe dla oryginalnego tytułu
		 */
		public JTextField getTitleOriginal() {
			return titleOriginal;
		}

		/**
		 * @return pole tekstowe dla tytułu przetłumaczonego
		 */
		public JTextField getTitleLocale() {
			return titleLocale;
		}
	}

	/**
	 * Panel agregujący pola dla informacji szczegółowych o książce
	 * 
	 * @author kornicameister
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
		/**
		 * {@link JComboBox} z gatunkami. Użytkownik wybiera z tego pola jeden
		 * gatunek który najlepiej będzie opisywał daną książkę.
		 */
		private final JComboBox<Genre> genreCombobox = new JComboBox<Genre>();
		/**
		 * {@link JComboBox} z autorami (pisarzami). Użytkownik wybiera z tego
		 * pola pisarza danej książki.
		 */
		private final JComboBox<Author> authorCombobox = new JComboBox<Author>();

		public DetailedInformationPanel(boolean isDoubleBuffered) {
			super(isDoubleBuffered);
			this.layoutComponents();
			this.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					"Details"));
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
			this.genreCombobox.setSelectedIndex(0);
			this.authorCombobox.setSelectedIndex(0);
		}

		private void layoutComponents() {
			this.setLayout(new GridLayout(4, 1, 5, 5));
			this.add(this.isbnField);
			this.add(this.pages);
			this.add(this.genreCombobox);
			this.add(this.authorCombobox);
		}

	}
}
