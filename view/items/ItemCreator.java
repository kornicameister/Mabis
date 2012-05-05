/**
 * 
 */
package view.items;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EtchedBorder;

/**
 * Klasa bazowa kreatora nowego obiektu dla kolekcji. Definiuje podstawową
 * strukturę okna, jego layout oraz zachowanie niezależne od typu definiowanego
 * obiektu dla kolekcji. {@link ItemCreator} może być także użyty do edycji
 * istniejącego obiektu kolekcji.
 * 
 * @author kornicameister
 * 
 */
public abstract class ItemCreator extends JFrame {
	private static final long serialVersionUID = -2333519518489232774L;
	private JPanel contentPanel = null;
	/**
	 * {@link JProgressBar}, który obrazuje postęp w wypełnianiu wymaganych pól
	 * dla danego obiektu
	 */
	private JProgressBar progressBar = null;
	/**
	 * Przycisk, który wyzwala akcję powodującą umieszczenie danego obiektu w
	 * bazie danych
	 */
	private JButton createButton;
	/**
	 * Przycisk powoduje wyczyszczenie wszystkich pól danego contentu
	 * 
	 * @see ItemCreator#contentPanel
	 */
	private JButton clearButton;
	/**
	 * Przycisk powoduje zakończenie działania kreatora
	 */
	private JButton cancelButton;
	/**
	 * Panel dla {@link GroupLayout}, aby ustawić miejsce, gdzie będzie
	 * definiownay layout
	 */
	private JPanel contentPane;

	/**
	 * Konstruktor klasy bazowej kreatora nowego obiektu
	 * 
	 * @param title
	 *            tytuł okna
	 * @throws HeadlessException
	 * @throws CreatorContentNullPointerException
	 */
	public ItemCreator(String title) throws HeadlessException,
			CreatorContentNullPointerException {
		super(title);
		this.initComponents();
		this.layoutComponents();

		/**
		 * setting size, look and feel, minimum size
		 */
		setDefaultLookAndFeelDecorated(true);
		this.setMinimumSize(new Dimension(300, 400));
		this.setLocationRelativeTo(null);
		this.setSize(this.getMinimumSize());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void layoutComponents() {
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
				.addGroup(
						gl.createSequentialGroup()
								.addComponent(this.contentPanel).addGap(10)
								.addComponent(this.progressBar))
				.addGroup(
						gl.createSequentialGroup()
								.addComponent(this.createButton)
								.addComponent(this.clearButton)
								.addComponent(this.cancelButton)));
		gl.setVerticalGroup(gl.createSequentialGroup().addGroup(
				gl.createParallelGroup()
						.addComponent(this.contentPanel)
						.addComponent(this.progressBar)
						.addGroup(
								gl.createParallelGroup()
										.addComponent(this.createButton)
										.addComponent(this.clearButton)
										.addComponent(this.cancelButton))));
		this.revalidate();
		this.pack();
		this.repaint();
	}

	private void initComponents() throws CreatorContentNullPointerException {
		this.contentPanel = this.initContent();
		if (this.contentPanel == null) {
			throw new CreatorContentNullPointerException(
					"Content not initialized");
		}
		this.progressBar = new JProgressBar(JProgressBar.VERTICAL);
		this.createButton = new JButton("Create");
		this.clearButton = new JButton("Clear");
		this.cancelButton = new JButton("Cancel");
	}

	/**
	 * Metoda abstrakcyjna, wywoływana zawsze w konstruktorze klasy
	 * {@link ItemCreator} aby zapewnić, że klasa dziedzicząca z ItemCreator
	 * będzie miała ustawiony odpowiedni content
	 * 
	 * @return JPanel na którym odłożone zostały wszystkie elementy potrzebne
	 *         aby utworzyć konkretny obiekt kolekcji.
	 */
	public abstract JPanel initContent();

}
