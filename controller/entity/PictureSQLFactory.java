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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
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
	private final TreeSet<Picture> pictures = new TreeSet<>();

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
			

			pp.setImageFile(newPicture.getCanonicalPath());
			
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
			p.setPrimaryKey(Utilities.lastInsertedId(p, st)+1);
			st.setObject(1, p);
			st.execute();
			this.lastAffactedId = p.getPrimaryKey();
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
		Picture problematic = (Picture) this.table;
		// 1. fetch all pictures
		PictureSQLFactory psf = new PictureSQLFactory(SQLStamentType.SELECT, problematic);
		psf.executeSQL(true);
		
		ArrayList<Picture> tmp = new ArrayList<>(psf.getCovers());
		
		int index = Collections.binarySearch(tmp, problematic,new Comparator<Picture>() {

			@Override
			public int compare(Picture o1, Picture o2) {
				return o1.getCheckSum().compareTo(o2.getCheckSum());
			}
			
		});
		
		if(index > 0){
			this.lastAffactedId = tmp.get(index).getPrimaryKey();
			return;
		}
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
						picture.setPrimaryKey(set.getInt("idPicture"));
						this.pictures.add(picture);
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

	public TreeSet<Picture> getCovers() {
		return pictures;
	}

	@Override
	public Boolean checkIfInserted() throws SQLException {
		return true;
	}

}
