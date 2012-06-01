package view.items.minipanels;

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
		
		gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(this.tableScroller));
		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(this.tableScroller));

		this.contentTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	}

	public void clear() {
		int numRows = this.tableModel.getRowCount();
		for (int i = numRows; i >= 0; i++) {
			this.tableModel.removeRow(i);
		}
		this.contentTable.revalidate();
	}
	
	protected abstract void initTable();
	protected abstract void addRow(Object bt);
	protected abstract MouseListener initScrollMouseListener();
	protected abstract TableModelListener initTableModelListener();
	
	protected KeyListener initTableKeyListener(){
		class TableKeyListener extends KeyAdapter implements KeyListener{
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_DELETE){
					tableModel.removeRow(currentlySelectedRow);
					currentlySelectedRow = -1;
				}
			}
		}
		return new TableKeyListener();
	}
	
	protected MouseListener initTableMouseListener(){
		class TableMouseListener extends MouseAdapter implements MouseListener{
			@Override
			public void mouseClicked(MouseEvent e) {
				currentlySelectedRow = contentTable.rowAtPoint(e.getPoint());
			}
		}
		return new TableMouseListener();
	}
}
