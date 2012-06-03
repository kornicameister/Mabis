package view.mainwindow.collectionTable;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.BaseTable;

public class CollectionTableModel extends DefaultTableModel implements
		TableModel {
	private static final long serialVersionUID = -7192867910474719900L;

	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex == 1){
			return BaseTable.class;
		}
		return ImageIcon.class;
	}

	public int getColumnCount() {
		return 2;
	}

	public String getColumnName(int columnIndex) {
		if(columnIndex == 1){
			return "Item";
		}
		return "Picture";
	}
	
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}
}