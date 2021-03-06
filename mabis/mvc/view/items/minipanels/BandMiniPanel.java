package mvc.view.items.minipanels;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import logger.MabisLogger;
import mvc.model.entity.Author;
import mvc.model.entity.Band;
import mvc.model.enums.AuthorType;
import settings.GlobalPaths;

/**
 * {@link MiniPanel} który rozszerzając {@link AuthorMiniPanel} stanowi
 * wyspecjalizowaną jego wersję przystosowaną do działania z {@link Band} -
 * tworcami albumow muzycznych
 * 
 * @author tomasz
 * @see Band
 */
public class BandMiniPanel extends AuthorMiniPanel {
	private static final long serialVersionUID = -7769648241373563025L;
	private ArrayList<Band> bands = new ArrayList<>();

	public BandMiniPanel(TreeSet<Band> bands, AuthorType type) {
		super(new TreeSet<Author>(bands), type);
		this.bands = new ArrayList<>(bands);
	}

	@Override
	public void addRow(Author a) {
		Band b = (Band) a;
		Object data[] = {null, b.getName()};
		if (b.getPrimaryKey() < 0) {
			ImageIcon tmp = new ImageIcon(GlobalPaths.CROSS_SIGN.toString());
			data[0] = new ImageIcon(tmp.getImage().getScaledInstance(10, 10,
					Image.SCALE_FAST));
		} else {
			ImageIcon tmp = new ImageIcon(GlobalPaths.OK_SIGN.toString());
			data[0] = new ImageIcon(tmp.getImage().getScaledInstance(10, 10,
					Image.SCALE_FAST));
		}
		this.tableModel.addRow(data);
		this.rowToAuthor.put(this.rowToAuthor.size(), a);
	}

	@Override
	protected void initTable() {
		String columnNames[] = {"LP", "Name"};
		this.tableModel = new DefaultTableModel(columnNames, 0);
		this.table = new JTable(tableModel) {
			private static final long serialVersionUID = -4341941906407330416L;
			@Override
			public Class<?> getColumnClass(int column) {
				if (column == 0) {
					return ImageIcon.class;
				}
				return Object.class;
			}
		};
	}

	@Override
	protected void initComponents() {
		super.initComponents();
		this.selectAuthorButton.setToolTipText("Select band");
		this.selectAuthorButton.setName("Select one of the following band");
		this.newAuthorButton.setToolTipText("Create new band");
		this.newAuthorButton.setName("Create new band");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		Comparator<Band> comparator = new Comparator<Band>() {
			@Override
			public int compare(Band o1, Band o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};
		if (source.equals(newAuthorButton)) {
			String returned = JOptionPane.showInputDialog(null, "Input:",
					source.getName(), JOptionPane.PLAIN_MESSAGE);
			if (returned != null) {
				Band tmp = new Band(returned, this.type);
				if (Collections.binarySearch(this.bands, tmp, comparator) < 0) {
					this.addRow(tmp);
				}
			}
		} else if (source.equals(selectAuthorButton)) {
			if (bands.size() == 0) {
				return;
			}
			Object arr[] = bands.toArray();
			Object returned = JOptionPane.showInputDialog(source,
					"Select one from following box", source.getName(),
					JOptionPane.QUESTION_MESSAGE, null, arr, arr[0]);
			if (returned != null) {
				Band tmp = (Band) returned;
				this.addRow(tmp);
			}
		}
		MabisLogger.getLogger().log(Level.INFO,
				"Action called by clicking at {0}", source.getName());
	}
}
