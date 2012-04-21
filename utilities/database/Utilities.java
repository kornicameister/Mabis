/**
 * package database in MABIS
 * by kornicameister
 */
package utilities.database;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	 * <b>Query size</b> korzystaj�c z otrzymanego {@link ResultSet} 
	 * stara si� okre�li� wielko�� kwerendy (tj. ilo�� zwr�conych w wyniku zapytania
	 * wierszy). 
	 * 
	 * @param rs
	 *            zbi�r wynik�w zawieraj�cy wa�n� tutaj informacj� o wielko�ci kwerendy
	 * @return ilo�� wierszy kwerendy
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
