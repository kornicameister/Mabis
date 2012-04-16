/**
 * 
 */
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ImageIcon;

/**
 * Blobber class is a simple utility that sets into stream an image or returns
 * it after successful extraction from database
 * 
 * @author kornicameister
 */
public class Blobber {

	/**
	 * Methods perform blob insertion into {@link PreparedStatement} object.
	 * Blob image is created from provided imageFile
	 * 
	 * @param dbStatement
	 * @param imageFile
	 * @param index
	 */
	static public void putBlobImageToStatement(PreparedStatement dbStatement, File imageFile,
			short index) {
		try {
			FileInputStream fis = new FileInputStream(imageFile);
			dbStatement.setBinaryStream(index, (InputStream) fis,
					(int) imageFile.length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates ImageIcon icon from blob 
	 * 
	 * @param blob
	 * @return
	 */
	//TODO add creating a File file
	static public ImageIcon createImageFromBlob(Blob blob) {
		try {
			ImageIcon i = new ImageIcon(blob.getBytes(1, (int) blob.length()));
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
