/**
 * 
 */
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import logger.MabisLogger;

import settings.GlobalPaths;
import database.Utilities;

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
	static public void putBlobImageToStatement(PreparedStatement dbStatement,
			File imageFile, short index) {
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
	// TODO add creating a File file
	static public File createImageFromBlob(Blob blob) {
		File blobFile = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = blob.getBinaryStream();
			String fileName = GlobalPaths.AVATAR_CACHE_PATH.toString()
					+ Utilities.crc32(is).substring(0, 35);
			blobFile = new File(fileName);

			if (blobFile.exists()) {
				is.close();
				return blobFile;
			}

			fos = new FileOutputStream(blobFile);

			fos.write(blob.getBytes(1, (int) blob.length()));

			is.close();
			fos.close();

			MabisLogger.getLogger().log(Level.INFO,
					"Created new cached file at {0}", fileName);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			MabisLogger.getLogger().log(Level.SEVERE,
					"Could not open/create cached image file");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return blobFile;
	}
}
