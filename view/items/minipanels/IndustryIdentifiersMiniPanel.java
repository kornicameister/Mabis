package view.items.minipanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.TreeMap;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.utilities.BookIndustryIdentifier;

public class IndustryIdentifiersMiniPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -3958286587031427043L;
	private JTable iiTable;
	private DefaultTableModel iiModel;
	private TreeMap<Integer,BookIndustryIdentifier> rowToBii = new TreeMap<>();
	private int currentlySelectedRow = -1;
	private JButton newIIButton,
					selectIIButton;
	private JScrollPane scrollForTable;
	
	public IndustryIdentifiersMiniPanel() {
		this.initializeTable();
		this.initComponents();
		this.layoutComponents();
	}

	private void initComponents() {
		this.newIIButton = new JButton("N");
		this.newIIButton.setName("New industry identifier");
		this.newIIButton.addActionListener(this);

		this.selectIIButton = new JButton("S");
		this.selectIIButton.setName("Select industry identifier");
		this.selectIIButton.addActionListener(this);
		
		this.scrollForTable = new JScrollPane(this.iiTable);
	}

	private void layoutComponents() {
		GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);

		gl.setHorizontalGroup(gl
				.createSequentialGroup()
				.addGroup(
						gl.createParallelGroup()
								.addComponent(this.newIIButton, 50, 50, 50)
								.addComponent(this.selectIIButton, 50, 50,
										50)).addGap(5)
				.addComponent(this.scrollForTable));
		gl.setVerticalGroup(gl
				.createParallelGroup()
				.addGroup(
						gl.createSequentialGroup()
								.addComponent(this.newIIButton)
								.addComponent(this.selectIIButton))
				.addComponent(this.scrollForTable, 90, 90, 90));

		this.iiTable.getColumnModel().getColumn(0).setMaxWidth(40);
	}

	private void initializeTable() {
		String columnNames[] = {"LP","Number","Type"};
		this.iiModel = new DefaultTableModel(columnNames,0);
		this.iiTable = new JTable(this.iiModel){
			private static final long serialVersionUID = 7141622026027712315L;
			@Override
			public Class<?> getColumnClass(int column) {
				if(column == 0){
					return ImageIcon.class;
				}
				return Object.class;
			}
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		class TableMouseListener extends MouseAdapter implements MouseListener{
			@Override
			public void mouseClicked(MouseEvent e) {
				currentlySelectedRow = iiTable.rowAtPoint(e.getPoint());
			}
		}
		
		class TableKeyListener extends KeyAdapter implements KeyListener{
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyChar() == KeyEvent.VK_DELETE){
					iiModel.removeRow(currentlySelectedRow);
					rowToBii.remove(new Integer(currentlySelectedRow));
					iiTable.revalidate();
					currentlySelectedRow = -1;
				}
			}
		}
		this.iiTable.addKeyListener(new TableKeyListener());
		this.iiTable.addMouseListener(new TableMouseListener());
	}
	
	public Collection<BookIndustryIdentifier> getII(){
		return this.rowToBii.values();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if(source.equals(this.newIIButton)){
			
		}else if(source.equals(this.selectIIButton)){
			
		}
	}
}
