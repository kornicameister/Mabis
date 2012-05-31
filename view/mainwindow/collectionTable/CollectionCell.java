package view.mainwindow.collectionTable;

import java.awt.Color;
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
import javax.swing.table.TableCellRenderer;

import model.BaseTable;
import model.entity.Movie;
import controller.HTMLDescriptor;

public class CollectionCell extends AbstractCellEditor implements TableCellRenderer {
	private static final long serialVersionUID = -8470153890083423012L;
	private JLabel text,image;
	private JPanel panel;
	private BaseTable table;

	private Color selectionColor, backgroundColor;
	
	public CollectionCell() {
		this.text = new JLabel();
		this.text.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
		
		this.image = new JLabel();
		this.panel = new JPanel(new GridLayout(1, 3));

		this.panel.add(this.text);
		this.panel.add(this.image);
		

		this.backgroundColor = this.panel.getBackground();
		this.selectionColor = this.backgroundColor.brighter();
		
	}

	private void updateData(BaseTable t, boolean isSelected, JTable table) {

		this.table = t;
		
		File imagePath = ((Movie)t).getCover().getImageFile();
		
		try {
			this.text.setText(HTMLDescriptor.toShortHTML(this.table));
			ImageIcon i = new ImageIcon(imagePath.getAbsolutePath());
			
			final double height = 180.0;
			double a = height / i.getIconHeight();
			double newWidth = a * i.getIconWidth();
			
			ImageIcon ii = new ImageIcon(i.getImage().getScaledInstance((int) newWidth, (int) height, Image.SCALE_FAST));
			
			this.image.setIcon(ii);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(isSelected){
			this.panel.setBackground(this.selectionColor);
		}else{
			this.panel.setBackground(this.backgroundColor);
		}
	}

	public Object getCellEditorValue() {
		return null;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		this.updateData(
				(BaseTable) value,
				isSelected, 
				table);
		return panel;
	}

}