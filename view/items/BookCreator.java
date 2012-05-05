/**
 * 
 */
package view.items;

import java.awt.GridLayout;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import model.entity.Author;
import model.entity.Genre;
import view.imagePanel.ChoosableImagePanel;

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
	private ChoosableImagePanel coverPanel;
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
		coverPanel = new ChoosableImagePanel(null);
		descriptionArea = new JTextArea();

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
								.addComponent(this.coverPanel)
								.addGroup(
										gl.createSequentialGroup()
												.addComponent(this.tp)
												.addComponent(this.dip)))
				.addComponent(this.descriptionArea));

		return p;
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
		private final JTextField titleOriginal = new JTextField("...");
		/**
		 * pole tekstowe dla tytułu, który jest przetłumaczony
		 */
		private final JTextField titleLocale = new JTextField("...");

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
		private final JTextField isbnField = new JTextField("");
		/**
		 * pole tekstowe dla ilości stron danej ksiązki
		 */
		private final JTextField pages = new JTextField("0");
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

		private void layoutComponents() {
			this.setLayout(new GridLayout(4, 1, 5, 5));
			this.add(this.isbnField);
			this.add(this.pages);
			this.add(this.genreCombobox);
			this.add(this.authorCombobox);
		}

	}
}