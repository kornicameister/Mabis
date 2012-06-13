package mvc.view.items.minipanels;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TreeMap;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import mvc.model.entity.AudioAlbum;
import mvc.model.entity.Book;
import mvc.model.entity.Movie;

/**
 * Klasa, która jest bazą dla całej rodziny mini paneli. Ponieważ każdy mini
 * panel składa sie z tabeli bedacej miejscem gdzie wyswietlane sa dane istnotne
 * dla uzytkownika. Z tego powodu, procz tabeli, definiowany jest takze scroller
 * ( {@link JScrollPane} ) dla tejze tabeli. Dodatkowo zainicjalizowane zostaly
 * takze odpowiednie struktury danych oraz {@link DefaultTableModel} dla tabeli.
 * 
 * @author tomasz
 * 
 */
public abstract class MiniPanel extends JPanel {
	private static final long serialVersionUID = 5994956857095533751L;
	protected JTable contentTable;
	protected DefaultTableModel tableModel;
	protected TreeMap<Integer, Object> rowToEntity;
	protected JScrollPane tableScroller;
	protected int currentlySelectedRow = -1;

	public MiniPanel() {
		super(true);

		this.rowToEntity = new TreeMap<>();

		this.initTable();
		this.initScroller();
		this.makeLayout();

		this.contentTable.addKeyListener(this.initTableKeyListener());
		this.contentTable.addMouseListener(this.initTableMouseListener());
		this.tableModel.addTableModelListener(this.initTableModelListener());
	}

	private void initScroller() {
		this.tableScroller = new JScrollPane(this.contentTable);
		this.tableScroller.addMouseListener(this.initScrollMouseListener());
	}

	protected void makeLayout() {
		GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);

		gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(
				this.tableScroller));
		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(
				this.tableScroller));

		this.contentTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	}

	/**
	 * Metoda usuwa z tabeli wszystkie dane
	 */
	public void clear() {
		int numRows = this.tableModel.getRowCount();
		if (numRows == 0) {
			return;
		}
		for (int i = numRows; i >= 0; i++) {
			this.tableModel.removeRow(i);
		}
		this.contentTable.revalidate();
	}

	/**
	 * Metoda abstrakcyjna. Redefinicja w klasach dziedziczących, kolejnych mini
	 * panelach, pozwala tak zainicjalizowac tabele, aby jej struktura
	 * odpowiadala celowi mini panelu.
	 */
	protected abstract void initTable();

	/**
	 * Metoda abstrakcyjna, redefiniowania w klasach mini panelu. Redefinicja
	 * jest naturalnym wynikiem faktu, ze kazdy mini panel posiada inna
	 * strukture tabeli.
	 * 
	 * @param bt
	 *            obiekt kolekcji z ktorego pobierane sa dane do kolejnego
	 *            wiersza tabeli
	 * 
	 * @see Movie
	 * @see Book
	 * @see AudioAlbum
	 */
	protected abstract void addRow(Object bt);

	/**
	 * Metoda abstrakcyjna, jako kolejna konswekwencja innych struktur tabel w
	 * kolejnych mini panelach. Sugeruje dodanie do {@link JScrollPane} tej
	 * klasy nasłuchu dla zdarzen pochodzacych od myszy. Redefinicja tej metody
	 * pozwala na następujące zachowanie: </br> Kliknięcie myszy powoduje
	 * dodanie pustego wiersza do tabeli, który może zostać wypełniony danymi
	 * 
	 * @return referencje do {@link MouseListener}
	 */
	protected abstract MouseListener initScrollMouseListener();

	/**
	 * Metoda abstrakcyjna, definicja w klasach rozszerzających dodają do tabeli
	 * {@link TableModelListener}, który rejestruje każdą modyfikację danych
	 * tabeli.
	 * 
	 * @return referencje do {@link TableModelListener}
	 */
	protected abstract TableModelListener initTableModelListener();

	/**
	 * Listener, który pozwala na usuwanie elementów z tabeli dzięki przyciskowi
	 * Delete
	 * 
	 * @return referencje do {@link KeyListener}
	 */
	protected KeyListener initTableKeyListener() {
		class TableKeyListener extends KeyAdapter implements KeyListener {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_DELETE) {
					tableModel.removeRow(currentlySelectedRow);
					currentlySelectedRow = -1;
				}
			}
		}
		return new TableKeyListener();
	}

	protected MouseListener initTableMouseListener() {
		class TableMouseListener extends MouseAdapter implements MouseListener {
			@Override
			public void mouseClicked(MouseEvent e) {
				currentlySelectedRow = contentTable.rowAtPoint(e.getPoint());
			}
		}
		return new TableMouseListener();
	}
}
