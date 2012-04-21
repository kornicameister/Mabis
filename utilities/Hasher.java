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

/**
 * Klasa <b>Hasher</b> jest klasą abstrakcyjną.
 * Jest to celowe działanie, ponieważ klasa ta zawiera jedynie statyczne metody, 
 * kt�re nie wymagaj� do wykonania istniej�cego obiektu klasy</br></br>
 * <b>Hasher</b> zawiera funkcje, kt�rych funkcjonalno�� mo�na opisa� najog�lniej
 * jako metody tworz� sumy kontrolne. 
 * 
 * @author kornicameister
 * @version 0.1
 */
public abstract class Hasher {

	/**
	 * Metoda tworzy hash has�a u�ytkownika korzystaj�c z algorytmu SHA
	 * @param passowrd has�o przes�ane czystym tekstem
	 * @return has�o zakodowanego algorytmem SHA
	 * @see java.security.MessageDigest
	 */
	final static public String hashPassword(String password) {
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
	 * Metoda oblicza sum� SHA dla podanego w parametrze strumienia wej�ciowego.
	 * Mo�e to by� plik lub inny dowolny strumie� kt�ry dziedziczy z InputStream.
	 * @param fis strumie� wej�cia
	 * @return String suma kontrolna (liczba z podstaw� szesnastkow�) jako String
	 */
	static public String hashStream(InputStream fis) {
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
			e.printStackTrace();
		}
		return hashword;
	}

}
