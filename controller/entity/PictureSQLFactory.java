/**
 * 
 */
package controller.entity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import logger.MabisLogger;
import model.entity.Picture;
import settings.GlobalPaths;
import utilities.Utilities;
import controller.SQLFactory;
import controller.SQLStamentType;
import controller.exceptions.PictureCacheException;

/**
 * @author kornicameister
 * 
 */
public class PictureSQLFactory extends SQLFactory {
	private final HashMap<Integer, Picture> pictures = new HashMap<Integer, Picture>();

	public PictureSQLFactory(SQLStamentType type, Picture table) {
		super(type, table);
	}

	private Boolean movePictureToCache() {
		Picture pp = ((Picture) this.table);
		File oldPicture = pp.getImageFile();

		File newPicture = null;
		switch (pp.getType()) {
		case AVATAR:
			newPicture = new File(GlobalPaths.AVATAR_CACHE_PATH
					+ pp.getCheckSum());
			break;
		case AUTHOR:
			newPicture = new File(GlobalPaths.AUTHOR_CACHE_PATH
					+ pp.getCheckSum());
			break;
		case BAND:
			newPicture = new File(GlobalPaths.BAND_CACHE_PATH
					+ pp.getCheckSum());
			break;
		default:
			newPicture = new File(GlobalPaths.MEDIA_CACHE_PATH
					+ pp.getCheckSum());
			break;
		}

		if (newPicture.exists()) {
			return false; // that means that cover was found in cache !!!
		}

		InputStream in;
		try {
			in = new FileInputStream(oldPicture);
			OutputStream out = new FileOutputStream(newPicture);

			byte[] buf = new byte[4096];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();

			pp.setImageFile(newPicture.getCanonicalPath(), pp.getCheckSum());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void deletePictureFromCache() throws PictureCacheException {
		Picture pp = ((Picture) this.table);
		File oldPicture = pp.getImageFile();
		if (!oldPicture.delete()) {
			MabisLogger.getLogger().log(Level.WARNING,
					"Couldn't delete {0} cached picture", pp.getImagePath());
			throw new PictureCacheException(pp, "couldn't be deleted");
		}
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		Picture p = (Picture) this.table;
		switch (this.type) {
		case INSERT:
			if (!this.movePictureToCache()) {
				// picture found in cache, internally changing the plan
				this.syncCacheToDB();
				return;
			}
			st.setObject(1, p);
			st.execute();
			// getting last inserted it
			st.clearParameters();
			this.lastAffactedId = Utilities.lastInsertedId(p, st);
			break;
		case DELETE:
			try {
				this.deletePictureFromCache();
			} catch (PictureCacheException e) {
				e.printStackTrace();
			}
			st.setInt(1, p.getPrimaryKey());
			this.parseDeleteSet(st.executeUpdate());
			break;
		case UPDATE:
		case SELECT:
			this.parseResultSet(st.executeQuery());
			break;
		default:
			break;
		}
	}

	/**
	 * Metoda pobiera wszystkie grafiki z bazy danych i następnie porównuje ich
	 * sumy kontrolne z sumą kontrolną problematycznej grafiki. W wypadku
	 * trafienia, zapisywany jest klucz główny znalezionej grafiki do grafiki
	 * problematycznej i działanie algorytmu jest zakończone.
	 * 
	 * @throws SQLException
	 */
	private void syncCacheToDB() throws SQLException {
		this.reset();
		Picture problematic = (Picture) this.table;
		// 1. fetch all pictures
		this.type = SQLStamentType.SELECT;
		this.executeSQL(true);
		for (Picture p : this.pictures.values()) {
			if (problematic.getCheckSum().equals(p.getCheckSum())) {
				// located picture
				problematic.setPrimaryKey(p.getPrimaryKey());
			} else {
				p = null;
			}
		}
		this.reset();
		this.lastAffactedId = problematic.getPrimaryKey();
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Picture picture = null;
		switch (this.type) {
		case UPDATE:
			break;
		case SELECT:
			while (set.next()) {
				byte[] buf = set.getBytes("object");
				if (buf != null) {
					try {
						ObjectInputStream objectIn = new ObjectInputStream(
								new ByteArrayInputStream(buf));
						picture = (Picture) objectIn.readObject();
						this.pictures.put(picture.getPrimaryKey(), picture);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				picture = null;
			}
			break;
		default:
			break;
		}
		set.close();
	}

	@Override
	public void reset() {
		super.reset();
		this.pictures.clear();
	}

	public HashMap<Integer, Picture> getCovers() {
		return pictures;
	}

	@Override
	public Boolean checkIfInserted() throws SQLException {
		return true;
	}

}
