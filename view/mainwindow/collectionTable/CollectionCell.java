package view.mainwindow.collectionTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import model.BaseTable;
import controller.HTMLDescriptor;

public class CollectionCell extends AbstractCellEditor implements TableCellRenderer {
	private static final long serialVersionUID = -8470153890083423012L;
	private JLabel text;
	private JPanel panel = new JPanel();
	private BaseTable table;

	private Color selectionColor, backgroundColor;
	
	public CollectionCell() {
		this.text = new JLabel();
		this.text.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		
		this.panel.add(this.text);

		this.backgroundColor = this.panel.getBackground();
		this.selectionColor = this.backgroundColor.brighter();
		
	}

	private void updateData(BaseTable t, boolean isSelected, JTable table) {
		this.table = t;
		
		try {
			this.text.setText(HTMLDescriptor.toShortHTML(this.table));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(isSelected){
			this.panel.setBackground(this.selectionColor);
		}else{
			this.panel.setBackground(this.backgroundColor);
		}
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		this.updateData(
				(BaseTable) value,
				isSelected, 
				table);
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

}