/**
 * 
 */
package view.items;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import logger.MabisLogger;
import model.BaseTable;
import model.entity.User;

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

	protected JPanel contentPanel;
	private JPanel contentPane;
	private ICButtonPanel buttonPanel;
	private ICActionListener listener;
	protected TreeSet<BaseTable> collectedItems;
	protected ICSearchPanel searchPanel;
	protected JProgressBar searchProgressBar;
	protected User selectedUser;
	protected boolean editingMode;

	/**
	 * Konstruktor klasy bazowej kreatora nowego obiektu
	 * 
	 * @param title
	 *            tytuł okna
	 * @throws HeadlessException
	 * @throws CreatorContentNullPointerException
	 */
	public ItemCreator(User u,String title) throws HeadlessException,
			CreatorContentNullPointerException {
		super(title);
		this.editingMode = false;
		this.selectedUser = u;
		this.initComponents();
		this.layoutComponents();

		setDefaultLookAndFeelDecorated(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * Ułóż elementy w oknie
	 */
	protected void layoutComponents() {
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
				.addComponent(this.searchProgressBar)
				.addGroup(
						gl.createSequentialGroup().addComponent(
								this.contentPanel))
				.addComponent(this.buttonPanel));
		gl.setVerticalGroup(gl
				.createSequentialGroup()
				.addComponent(this.searchPanel, 30, 30, 30)
				.addComponent(this.searchProgressBar, 40, 40, 40)
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.contentPanel))
				.addComponent(this.buttonPanel, 30, 30, 30));

		this.pack();
	}

	/**
	 * Inicjalizuje komponenty tego kreatora
	 * 
	 * @throws CreatorContentNullPointerException
	 */
	protected void initComponents() throws CreatorContentNullPointerException {
		this.listener = new ICActionListener();
		this.buttonPanel = new ICButtonPanel();
		this.searchPanel = new ICSearchPanel();
		this.searchProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		this.searchProgressBar.setMinimum(0);
		this.searchProgressBar.setMaximum(100);
		this.searchProgressBar.setStringPainted(true);
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
	 * Metoda wywoływana po kliknięciu na {@link ICButtonPanel#getFromNetButton}
	 * . Po wywołeniu akcji, podejmowana jest próba pobrania informacji o danym
	 * obiekcie kolekcji przez publiczne API.
	 * 
	 * @param query
	 * 
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
	 * Prosta metoda do ustawienia obiektu kolekcji.
	 * Używana głównie jeśli obiekt ma być edytowany !
	 * @param bt
	 */
	public void setEditableItem(BaseTable bt) {
		this.editingMode = true;
		this.fillWithResult(bt);
	}
	

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

	public class ICSearchPanel extends JPanel {
		private static final long serialVersionUID = 806539065413054227L;
		private JTextField searchQuery = new JTextField();
		private JComboBox<String> criteria;
		private JButton searchButton = new JButton("Search");
		private JButton cancelButton = new JButton("Cancel");

		public ICSearchPanel() {
			super(true);

			this.setLayout(new GridLayout(1, 4));
			this.add(this.searchQuery);
			this.add(this.searchButton);
			this.add(this.cancelButton);

			this.searchButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					fetchFromAPI(searchQuery.getText(), (String) criteria.getSelectedItem());
				}
			});
			
			this.cancelButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					cancelAPISearch();
				}
			});
		}

		public void setSearchCriteria(String[] arr) {
			this.criteria = new JComboBox<String>(arr);
			this.criteria.setSelectedIndex(0);
			this.add(this.criteria, 1);
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