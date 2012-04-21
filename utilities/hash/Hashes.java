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
 * Klasa <b>Hashes</b> jest klas¹ abstrakcyjn¹.
 * Jest to celowe dzia³anie, poniewa¿ klasa ta zawiera jedynie statyczne metody, 
 * które nie wymagaj¹ do wykonania istniej¹cego obiektu klasy</br></br>
 * <b>Hashes</b> zawiera funkcje, których funkcjonalnoœæ mo¿na opisaæ najogólniej
 * jako metody tworz¹ sumy kontrolne. 
 * 
 * @author kornicameister
 * @version 0.1
 */
public final abstract class Hashes {

	/**
	 * Metoda tworzy hash has³a u¿ytkownika korzystaj¹c z algorytmu SHA
	 * @param passowrd has³o przes³ane czystym tekstem
	 * @return has³o zakodowanego algorytmem SHA
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
	 * Metoda oblicza sumê SHA dla podanego w parametrze strumienia wejœciowego.
	 * Mo¿e to byæ plik lub inny dowolny strumieñ który dziedziczy z InputStream.
	 * @param fis strumieñ wejœcia
	 * @return String suma kontrolna (liczba z podstaw¹ szesnastkow¹) jako String
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
