/**
 * 
 */
package controller.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import logger.MabisLogger;
import model.entity.Picture;
import model.enums.ImageType;
import settings.GlobalPaths;
import utilities.Utilities;
import controller.PictureCacheException;
import controller.SQLFactory;
import controller.SQLStamentType;

/**
 * @author kornicameister
 * 
 */
public class PictureSQLFactory extends SQLFactory {
	private HashMap<Integer, Picture> covers;

	public PictureSQLFactory(SQLStamentType type, Picture table) {
		super(type, table);
		covers = new HashMap<Integer, Picture>();
	}

	private void movePictureToCache() {
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
			return;
		}

		InputStream in;
		try {
			in = new FileInputStream(oldPicture);
			OutputStream out = new FileOutputStream(newPicture);

			byte[] buf = new byte[1024];
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
			this.movePictureToCache();
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

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Picture p = null;
		switch (this.type) {
		case DELETE:
			break;
		case UPDATE:
			break;
		case SELECT:
			while (set.next()) {
				p = new Picture(set.getInt("idPicture"), ImageType.UNDEFINED);
				try {
					p.setImageFile(set.getString("image"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!set.getString("hash").equals(p.getCheckSum())) {
					MabisLogger.getLogger().log(Level.WARNING,
							"Loaded image checksum != calculated checksum");
				}
				this.covers.put(p.getPrimaryKey(), p);
			}
			break;
		default:
			break;
		}
	}

	public HashMap<Integer, Picture> getCovers() {
		return covers;
	}

}
