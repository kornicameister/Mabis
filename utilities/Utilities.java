/**
 * package database in MABIS
 * by kornicameister
 */
package utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.BaseTable;

/**
 * <b>Utilities</b> jest klasą enkapsulującą metody zorientowane na wykonywanie
 * prostych operacji na bazie danych.
 * 
 * @author kornicameister
 * @version 0.1
 */
public abstract class Utilities {
	private final static String LAST_ID_PATTERN = "SELECT id! from mabis.! order by id! desc limit 1";

	/**
	 * <b>Query size</b> korzystając z otrzymanego {@link ResultSet} stara się
	 * określić wielkość kwerendy (tj. ilość zwróconych w wyniku zapytania
	 * wierszy).
	 * 
	 * @param rs
	 *            zbiór wyników zawierający informację o ilości wierszy w
	 *            zapytaniu
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

	/**
	 * Metoda pozwala na pobranie z bazy danych numerzu porządkowej <b>id</id>
	 * ostatnio umieszczonej krotki w tabeli <i>table</i>
	 * 
	 * @param table
	 * @param st
	 * @return numer porządkowy ostatnio wrzuconej krotki w <i>table</i> tabeli
	 * @throws SQLException
	 */
	static public Integer lastInsertedId(BaseTable table, PreparedStatement st)
			throws SQLException {
		long res = 0;
		String query = Utilities.LAST_ID_PATTERN;
		String tableName = String.valueOf(table.getTableType().toString().charAt(0))
				.toUpperCase() + table.getTableType().toString().substring(1);

		query = query.replaceFirst("!", tableName);
		query = query.replaceFirst("!", table.getTableType().toString());
		query = query.replaceFirst("!", tableName);

		ResultSet set = st.executeQuery(query);
		while (set.next()) {
			res = set.getInt(1);
		}
		set.close();
		return (int) res;
	}

}
