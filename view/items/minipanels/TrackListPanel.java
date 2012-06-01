package view.items.minipanels;

import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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
	private TreeSet<AudioAlbumTrack> tracks;
	private JScrollPane scroller;

	public TrackListPanel() {
		super(true);
		this.initializeTable(new TreeSet<AudioAlbumTrack>());
		this.makeLayout();
	}

	private void makeLayout() {
		GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);
		
		gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(this.scroller));
		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(this.scroller));
	}

	public TrackListPanel(TreeSet<AudioAlbumTrack> tracks) {
		super(true);
		if (tracks == null) {
			this.initializeTable(new TreeSet<AudioAlbumTrack>());
		} else {
			this.initializeTable(tracks);
			this.tracks = tracks;
		}
	}

	private void initializeTable(TreeSet<AudioAlbumTrack> tracks) {
		String header[] = { "LP", "ID", "Lenght", "Title" };
		this.dtm = new DefaultTableModel(header, 0);
		ArrayList<Object> row = new ArrayList<>();
		for (AudioAlbumTrack a : tracks) {
			row.add(a.getId());
			row.add(a.getDuration());
			row.add(a.getName());
			this.dtm.addRow(row.toArray());
			row.clear();
		}
		this.trackListTable = new JTable(this.dtm){
			private static final long serialVersionUID = -4341941906407330416L;
			@Override
			public Class<?> getColumnClass(int column) {
				if(column == 0){
					return ImageIcon.class;
				}
				return Object.class;
			}
		};

		class TrackListTableKeyLister extends KeyAdapter implements KeyListener{

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
			}
		}
		this.trackListTable.addKeyListener(new TrackListTableKeyLister());
		this.scroller = new JScrollPane(this.trackListTable);
	}

	public JTable getTrackListTable() {
		return trackListTable;
	}

	public void clear() {
		int numRows = dtm.getRowCount();
		for (int i = numRows; i >= 0; i++) {
			this.dtm.removeRow(i);
			this.trackListTable.revalidate();
		}
	}

	public void setTracks(TreeSet<AudioAlbumTrack> trackList) {
		ArrayList<Object> row = new ArrayList<>();
		for (AudioAlbumTrack a : trackList) {
			ImageIcon tmp = new ImageIcon(GlobalPaths.MUSIC_ICON.toString());
			row.add(new ImageIcon(tmp.getImage().getScaledInstance(10, 10, Image.SCALE_FAST)));
			row.add(a.getId());
			row.add(a.getDuration());
			row.add(a.getName());
			this.dtm.addRow(row.toArray());
			row.clear();
		}
		this.trackListTable.revalidate();
		this.tracks = trackList;
	}
	
	public TreeSet<AudioAlbumTrack> getTracks() {
		return tracks;
	}
}
