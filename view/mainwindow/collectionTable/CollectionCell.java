package view.mainwindow.collectionTable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import model.BaseTable;
import model.entity.Movie;
import controller.HTMLDescriptor;

public class CollectionCell extends AbstractCellEditor implements
		TableCellEditor, TableCellRenderer {
	private static final long serialVersionUID = -8470153890083423012L;
	private JLabel text,image;
	private JPanel panel;
	private BaseTable table;

	public CollectionCell() {
		this.text = new JLabel();
		this.text.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
		
		this.image = new JLabel();
		this.panel = new JPanel(new GridLayout(1, 3));

		this.panel.add(this.text);
		this.panel.add(this.image);
	}

	private void updateData(BaseTable t, boolean isSelected, JTable table) {
		if(t != null){
			this.table = t;
		}
		
		File imagePath = ((Movie)t).getCover().getImageFile();
		
		try {
			this.text.setText(HTMLDescriptor.toShortHTML(this.table));
			ImageIcon ii = new ImageIcon(new ImageIcon(imagePath.getAbsolutePath()).getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH));
			this.image.setIcon(ii);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		BaseTable tt = (BaseTable) value;
		this.updateData(tt, true, table);
		return panel;
	}

	public Object getCellEditorValue() {
		return null;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		BaseTable feed = (BaseTable) value;
		this.updateData(feed, isSelected, table);
		return panel;
	}

}