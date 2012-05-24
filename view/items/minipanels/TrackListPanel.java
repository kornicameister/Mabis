package view.items.minipanels;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.utilities.AudioAlbumTrack;

public class TrackListPanel extends JPanel {
	private static final long serialVersionUID = 4109556422700649333L;
	private JTable trackListTable;
	private DefaultTableModel dtm;

	public TrackListPanel() {
		super(true);
		this.initializeTable(new TreeSet<AudioAlbumTrack>());
	}

	public TrackListPanel(TreeSet<AudioAlbumTrack> tracks) {
		super(true);
		if (tracks == null) {
			this.initializeTable(new TreeSet<AudioAlbumTrack>());
		} else {
			this.initializeTable(tracks);
		}
	}

	private void initializeTable(TreeSet<AudioAlbumTrack> tracks) {
		String header[] = { "ID", "Lenght", "Title" };
		this.dtm = new DefaultTableModel(header, 0);
		ArrayList<Object> row = new ArrayList<>();
		for (AudioAlbumTrack a : tracks) {
			row.add(a.getId());
			row.add(a.getDuration());
			row.add(a.getName());
			this.dtm.addRow(row.toArray());
			row.clear();
		}
		this.trackListTable = new JTable(this.dtm);

		this.trackListTable.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
			}
		});

		this.add(this.trackListTable);
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
			row.add(a.getId());
			row.add(a.getDuration());
			row.add(a.getName());
			this.dtm.addRow(row.toArray());
			this.trackListTable.revalidate();
			row.clear();
		}
	}
}
