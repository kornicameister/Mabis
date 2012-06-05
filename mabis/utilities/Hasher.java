/**
 * package database in MABIS
 * by kornicameister
 */
package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.hash.Hashing;

/**
 * Klasa <b>Hasher</b> jest klasą abstrakcyjną. Jest to celowe działanie,
 * ponieważ klasa ta zawiera jedynie statyczne metody, które nie wymagają do
 * wykonania istniejącego obiektu klasy</br
 * 
 * @author kornicameister
 * @version 0.1
 */
public abstract class Hasher {

	/**
	 * Metoda działa jako wrapper dla metody md5() z pakietu
	 * com.google.common.hash.
	 * 
	 * @see Hashing
	 * @return hash hasła jako String
	 */
	final static public String hashString(String password) {
		return Hashing.md5().hashString(password).toString();
	}

	/**
	 * Metoda hashuje podany jako parametr strumień wejściowy.
	 * 
	 * @param fis
	 * @return hash zawartości strumienia
	 */
	static public String hashStream(InputStream fis) {
		String hashword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA");
			byte raw[] = new byte[2048];
			int readData = 0;
			do {
				readData = fis.read(raw);
				md5.update(raw);
			} while (readData != -1);

			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = hash.toString(16).substring(0, 36);
			fis.close();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hashword;
	}

}
