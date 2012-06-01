package view.items.minipanels;

import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.utilities.AudioAlbumTrack;
import settings.GlobalPaths;

public class TrackListPanel extends JPanel {
	private static final long serialVersionUID = 4109556422700649333L;
	private JTable trackListTable;
	private DefaultTableModel dtm;
	private TreeMap<Integer,AudioAlbumTrack> rowToTracks;
	private JScrollPane scroller;
	private int currentlySelectedRow = -1;

	public TrackListPanel() {
		super(true);
		this.rowToTracks = new TreeMap<>();
		this.initializeTable();
		this.makeLayout();
	}

	public TrackListPanel(TreeSet<AudioAlbumTrack> tracks) {
		super(true);
		this.rowToTracks = new TreeMap<>();
		this.initializeTable();
		this.makeLayout();
		for(AudioAlbumTrack aat : tracks){
			this.addRow(aat); 
		}
	}

	private void makeLayout() {
		GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);
		
		gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(this.scroller));
		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(this.scroller));
		
		this.trackListTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		this.trackListTable.getColumnModel().getColumn(0).setMaxWidth(40);
		this.trackListTable.getColumnModel().getColumn(1).setMaxWidth(40);
	}

	private void initializeTable() {
		String columnNames[] = {"|","LP","Lenght","Name",};
		this.dtm = new DefaultTableModel(columnNames, 0);
		this.trackListTable = new JTable(this.dtm){
			private static final long serialVersionUID = -4341941906407330416L;
			@Override
			public Class<?> getColumnClass(int column) {
				if(column == 0){
					return ImageIcon.class;
				}
				return Object.class;
			}
			
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column > 1){
					return true;
				}
				return false;
			}
		};
		
		class TableMouseListener extends MouseAdapter implements MouseListener{
			@Override
			public void mouseClicked(MouseEvent e) {
				currentlySelectedRow = trackListTable.rowAtPoint(e.getPoint());
				addRow(new AudioAlbumTrack((short) 0, "", ""));
			}
		}
		
		class TableKeyListener extends KeyAdapter implements KeyListener{
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_DELETE){
					dtm.removeRow(currentlySelectedRow);
					rowToTracks.remove(currentlySelectedRow);
					trackListTable.revalidate();
					currentlySelectedRow = -1;
				}else if(e.getKeyChar() == '\n'){
					Short id = (Short) dtm.getValueAt(dtm.getRowCount()-1,1);
					String lenght = (String) dtm.getValueAt(dtm.getRowCount()-1, 2);
					String name = (String) dtm.getValueAt(dtm.getRowCount()-1, 3);
					
					rowToTracks.put(new Integer(dtm.getRowCount()-1),new AudioAlbumTrack(id, name, lenght));
					
				}
			}
		}
		
		this.trackListTable.addKeyListener(new TableKeyListener());
		this.trackListTable.addMouseListener(new TableMouseListener());
		this.scroller = new JScrollPane(this.trackListTable);
	}

	public void clear() {
		int numRows = dtm.getRowCount();
		for (int i = numRows; i >= 0; i++) {
			this.dtm.removeRow(i);
			this.trackListTable.revalidate();
		}
	}

	public void addRow(AudioAlbumTrack a) {
		Object data[] = { null, a.getId(), a.getDuration(), a.getName() };
		ImageIcon tmp = new ImageIcon(GlobalPaths.MUSIC_ICON.toString());
		data[0] = new ImageIcon(tmp.getImage().getScaledInstance(10, 10, Image.SCALE_FAST));
		
		this.dtm.addRow(data);
		this.rowToTracks.put(this.rowToTracks.size(),a);
		this.trackListTable.revalidate();
	}
	
	public void setTracks(TreeSet<AudioAlbumTrack> trackList) {
		for (AudioAlbumTrack a : trackList) {
			this.addRow(a);
		}
	}
	
	public Collection<AudioAlbumTrack> getTracks() {
		return rowToTracks.values();
	}
}
