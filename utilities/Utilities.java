/**
 * package database in MABIS
 * by kornicameister
 */
package utilities;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <b>Utilities</b> jest klas� enkapsuluj�c� metody
 * zorientowane na wykonywanie prostych operacji na bazie danych.
 * 
 * @author kornicameister
 * @version 0.1
 */
public abstract class Utilities {

	/**
	 * <b>Query size</b> korzystając z otrzymanego {@link ResultSet} 
	 * stara się określić wielkość kwerendy (tj. ilość zwróconych w wyniku zapytania
	 * wierszy). 
	 * 
	 * @param rs
	 *            zbiór wyników zawierający informację o ilości wierszy w zapytaniu
	 * @return ilość wierszy zapytania
	 */
	static public int querySize(ResultSet rs) {
		try {
			int currentRow = rs.getRow();
			rs.last();
			int result = rs.getRow();
			while (rs.getRow() != currentRow) {
				rs.previous();
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

}
