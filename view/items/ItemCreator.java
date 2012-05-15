/**
 * 
 */
package view.items;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EtchedBorder;

import logger.MabisLogger;
import model.BaseTable;

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
	 * Panel dla {@link GroupLayout}, aby ustawić miejsce, gdzie będzie
	 * definiownay layout
	 */
	private JPanel contentPane;
	/**
	 * referencja do panelu agregującego przyciski
	 */
	private ICButtonPanel buttonPanel;
	/**
	 * referencja do listenera dla ItemCreatora
	 */
	private ICActionListener listener;

	/**
	 * Lista pobranych elementów kolekcji
	 */
	protected TreeSet<BaseTable> collectedItems;

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
		this.setMinimumSize(new Dimension(300, 500));
		this.setLocationRelativeTo(null);
		this.setSize(this.getMinimumSize());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * Ułóż elementy w oknie
	 */
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
								.addComponent(this.contentPanel)
								.addComponent(this.progressBar))
				.addComponent(this.buttonPanel));
		gl.setVerticalGroup(gl
				.createSequentialGroup()
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.contentPanel)
								.addComponent(this.progressBar))
				.addComponent(this.buttonPanel, GroupLayout.DEFAULT_SIZE, 30,
						30));

		this.revalidate();
		this.pack();
		this.repaint();
	}

	/**
	 * Inicjalizuje komponenty tego kreatora
	 * 
	 * @throws CreatorContentNullPointerException
	 */
	private void initComponents() throws CreatorContentNullPointerException {
		this.listener = new ICActionListener();
		this.contentPanel = this.initContent();
		if (this.contentPanel == null) {
			throw new CreatorContentNullPointerException(
					"Content not initialized");
		}
		this.progressBar = new JProgressBar(JProgressBar.VERTICAL);
		this.buttonPanel = new ICButtonPanel();
	}

	/**
	 * Metoda abstrakcyjna, wywoływana zawsze w konstruktorze klasy
	 * {@link ItemCreator} aby zapewnić, że klasa dziedzicząca z ItemCreator
	 * będzie miała ustawiony odpowiedni content
	 * 
	 * @return JPanel na którym odłożone zostały wszystkie elementy potrzebne
	 *         aby utworzyć konkretny obiekt kolekcji.
	 */
	protected abstract JPanel initContent();

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
	protected abstract Boolean createItem();

	/**
	 * Metoda wywoływana po kliknięciu na {@link ICButtonPanel#getFromNetButton}
	 * . Po wywołeniu akcji, podejmowana jest próba pobrania informacji o danym
	 * obiekcie kolekcji przez publiczne API.
	 * 
	 * 
	 */
	protected abstract void fetchFromAPI();

	private class ICButtonPanel extends JPanel {
		private static final long serialVersionUID = -169864232599710877L;
		/**
		 * Przycisk, który wyzwala akcję powodującą umieszczenie danego obiektu
		 * w bazie danych
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
		private JButton getFromNetButton;

		public ICButtonPanel() {
			this.createButton = new JButton("Create");
			this.createButton.addActionListener(listener);
			this.clearButton = new JButton("Clear");
			this.clearButton.addActionListener(listener);
			this.cancelButton = new JButton("Cancel");
			this.cancelButton.addActionListener(listener);
			this.getFromNetButton = new JButton("Fetch from web");
			this.getFromNetButton.addActionListener(listener);

			this.setLayout(new GridLayout(2, 2));

			this.add(this.getFromNetButton);
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
			if (source.equals(buttonPanel.cancelButton)) {
				setVisible(false);
				dispose();
			} else if (source.equals(buttonPanel.clearButton)) {
				clearContentFields();
			} else if (source.equals(buttonPanel.createButton)) {
				if (!createItem()) {
					JOptionPane.showMessageDialog(getParent(),
							"Failed to create new item",
							"Creation miracle failed",
							JOptionPane.ERROR_MESSAGE);
				}
			} else if (source.equals(buttonPanel.getFromNetButton)) {
				fetchFromAPI();
			}
			MabisLogger.getLogger().log(Level.INFO,
					"ItemCreator action called :: {0}",
					source.getActionCommand());
		}

	}

}
