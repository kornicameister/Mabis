/**
 * package database in MABIS
 * by kornicameister
 */
package database;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <b>The Class Utilities</b> contains only static methods that allows to
 * perform database itself independent operations
 * 
 * @author kornicameister
 * @version 0.1
 */
public abstract class Utilities {

	/**
	 * hashes the password using sha algorithm
	 */
	static public String hashPassword(String password) {
		String hashword = password;
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA");
			md5.update(password.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = hash.toString(16).substring(0, 36);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		return hashword;
	}

	/**
	 * Md5sum. generates checksum from content of fis InputStream
	 * 
	 * @param input
	 *            String for which md5sum will be calculated
	 * @return md5sum of input
	 * @see http://m2tec.be/blog/2010/02/03/java-md5-hex-0093
	 */
	static public String crc32(InputStream fis) {
		String hashword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA");
			
			byte raw[] = new byte[2048];
			int readData = 0;
			do{
				readData = fis.read(raw);
				md5.update(raw);
			}while(readData != -1);
			
			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = hash.toString(16).substring(0,36);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashword;
	}

	/**
	 * <b>Query size</b> methods grabs {@link ResultSet} object and tries to
	 * resolve rowCount without violating rs integrity
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
