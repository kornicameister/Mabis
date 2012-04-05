/**
 * package database in MABIS
 * by kornicameister
 */
package database;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <b>The Class Utilities</b> contains only static methods
 * that allows to perform database itself independent operations
 * 
 * @author kornicameister
 * @version 0.1
 */
public abstract class Utilities {

	/**
	 * Md5sum.
	 * 
	 * @param input
	 *            String for which md5sum will be calculated
	 * @return md5sum of input
	 * @see http://m2tec.be/blog/2010/02/03/java-md5-hex-0093
	 */ 
	static public String md5sum(String input) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(input.getBytes(Charset.forName("UTF8")));
			
			byte array[] = m.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; i++) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <b>Query size</b> methods grabs {@link ResultSet} object
	 * and tries to resolve rowCount without violating rs integrity
	 * 
	 * @param rs
	 *            result set of which size it is all about
	 * @return rowCount of the rs
	 */
	static public int querySize(ResultSet rs) {
		try {
			int currentRow = rs.getRow();
			rs.last();
			int result = rs.getRow();
			while(rs.getRow() != currentRow){
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
