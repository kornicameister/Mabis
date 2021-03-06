package mvc.view.items.minipanels;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import mvc.model.utilities.AudioAlbumTrack;
import settings.GlobalPaths;

/**
 * Mini panel, który definiuje listę utworów dla albumu muzycznego
 * 
 * @author tomasz
 * @see AudioAlbumTrack
 */
public class TrackListPanel extends MiniPanel {
	private static final long serialVersionUID = 4109556422700649333L;

	public TrackListPanel() {
		super();
	}

	/**
	 * KOnstruje {@link TrackListPanel} z gotową listą utworów
	 * 
	 * @param tracks
	 */
	public TrackListPanel(TreeSet<AudioAlbumTrack> tracks) {
		super();
		for (AudioAlbumTrack aat : tracks) {
			this.addRow(aat);
		}
	}

	@Override
	protected void makeLayout() {
		super.makeLayout();
		this.contentTable.getColumnModel().getColumn(0).setMaxWidth(40);
		this.contentTable.getColumnModel().getColumn(1).setMaxWidth(40);
	}

	@Override
	protected void initTable() {
		String columnNames[] = {"||", "LP", "Lenght", "Name",};
		this.tableModel = new DefaultTableModel(columnNames, 0);
		this.contentTable = new JTable(this.tableModel) {
			private static final long serialVersionUID = -4341941906407330416L;
			@Override
			public Class<?> getColumnClass(int column) {
				if (column == 0) {
					return ImageIcon.class;
				}
				return Object.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column > 0) {
					return true;
				}
				return false;
			}
		};
	}

	@Override
	protected TableModelListener initTableModelListener() {
		return new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				int eRow = e.getLastRow(), eCol = e.getColumn();
				switch (e.getType()) {
					case TableModelEvent.INSERT :
						rowToEntity
								.put(eRow,
										new AudioAlbumTrack(
												(Integer) tableModel
														.getValueAt(eRow, 1),
												(String) tableModel.getValueAt(
														eRow, 3),
												durationToLong(
														(String) tableModel
																.getValueAt(
																		eRow, 2))
														.toString()));
						// System.out.println("New row inserted at " + eRow);
						break;
					case TableModelEvent.UPDATE :
						// System.out.println("Update at [" + eRow + "," + eCol
						// + "]");
						AudioAlbumTrack a = (AudioAlbumTrack) rowToEntity.get(e
								.getLastRow());
						Object val = tableModel.getValueAt(eRow, eCol);
						switch (e.getColumn()) {
							case 1 :
								a.setId(Integer.valueOf(((String) val)));
								break;
							case 2 :
								a.setDuration(this.durationToLong((String) val));
								break;
							case 3 :
								a.setName((String) val);
								break;
						}
						break;
					case TableModelEvent.DELETE :
						// System.out.println("Row " + eRow + " deleted");
						rowToEntity.remove(eRow);
						contentTable.revalidate();
						break;
				}
			}

			private Long durationToLong(String dur) {
				String tmp[] = dur.split(":");
				Long ll = 0l;
				int multiplier = 60;
				for (int i = tmp.length - 1; i != -1; i--) {
					ll += Long.valueOf(tmp[i]) * multiplier;
					multiplier *= 60;
				}
				return ll;
			}
		};
	}

	@Override
	protected MouseListener initScrollMouseListener() {
		class ScrollerListener extends MouseAdapter implements MouseListener {
			@Override
			public void mouseClicked(MouseEvent e) {
				addRow(new AudioAlbumTrack(rowToEntity.size(), "", ""));
			}
		}
		return new ScrollerListener();
	}

	@Override
	protected void addRow(Object bt) {
		final AudioAlbumTrack a = (AudioAlbumTrack) bt;
		Object data[] = {null, a.getId(), a.getDuration(), a.getName()};
		ImageIcon tmp = new ImageIcon(GlobalPaths.MUSIC_ICON.toString());
		data[0] = new ImageIcon(tmp.getImage().getScaledInstance(10, 10,
				Image.SCALE_FAST));

		this.tableModel.addRow(data);
	}

	/**
	 * Setter, który pozwoli na ustawienie w {@link TrackListPanel} gotowej
	 * listy utworów
	 * 
	 * @param trackList
	 */
	public void setTracks(TreeSet<AudioAlbumTrack> trackList) {
		this.clear();
		for (AudioAlbumTrack a : trackList) {
			this.addRow(a);
		}
	}

	/**
	 * Getter pozwalający na pobranie kolekcji utworów
	 * 
	 * @return utwory muzyczne z tego panelu
	 */
	public Collection<AudioAlbumTrack> getTracks() {
		ArrayList<AudioAlbumTrack> tmp = new ArrayList<>();
		for (Object a : this.rowToEntity.values()) {
			tmp.add((AudioAlbumTrack) a);
		}
		return tmp;
	}

}
