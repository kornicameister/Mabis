package view.mainwindow.collectionTable;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.BaseTable;

public class CollectionTableModel extends DefaultTableModel implements
		TableModel {
	private static final long serialVersionUID = -7192867910474719900L;

	public Class<?> getColumnClass(int columnIndex) {
		return BaseTable.class;
	}

	public int getColumnCount() {
		return 1;
	}

	public String getColumnName(int columnIndex) {
		return "Feed";
	}
	
	public boolean isCellEditable(int arg0, int arg1) {
		return true;
	}
}