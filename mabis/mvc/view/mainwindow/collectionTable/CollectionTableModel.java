package mvc.view.mainwindow.collectionTable;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import mvc.model.BaseTable;

/**
 * Model dla tabeli, ktora potrafi wyswietlac w kolejnych wierszach w:
 * <ol>
 * <li>kolumnie -> obiekt klasy ImageIcon</li>
 * <li>kolumnie -> obiekt klasy BaseTable</li>
 * </ol>
 * 
 * @author tomasz
 * @see BaseTable
 */
public class CollectionTableModel extends DefaultTableModel
		implements
			TableModel {
	private static final long serialVersionUID = -7192867910474719900L;

	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 1) {
			return BaseTable.class;
		}
		return ImageIcon.class;
	}

	public int getColumnCount() {
		return 2;
	}

	public String getColumnName(int columnIndex) {
		if (columnIndex == 1) {
			return "Item";
		}
		return "Picture";
	}

	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}
}