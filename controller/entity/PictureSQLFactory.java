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

import settings.GlobalPaths;

import logger.MabisLogger;
import model.entity.Picture;
import model.enums.ImageType;
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
		this.movePictureToCache();
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

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		Picture p = (Picture) this.table;
		switch (this.type) {
		case INSERT:
			short index = 1;
			st.setString(index++, p.getCheckSum());
			st.setString(index++, p.getImagePath());
			st.execute();

			// getting last inserted id
			st.clearParameters();
			String query = "SELECT id! from mabis.! order by id! desc limit 1";
			String tableName = String.valueOf(
					this.table.getTableName().charAt(0)).toUpperCase()
					+ this.table.getTableName().substring(1);

			query = query.replaceFirst("!", tableName);
			query = query.replaceFirst("!", this.table.getTableName());
			query = query.replaceFirst("!", tableName);

			this.parseResultSet(st.executeQuery(query));

			break;
		case DELETE:
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
		case INSERT:
			// getting last inserted id !!!
			while (set.next()) {
				this.lastAffactedId = set.getInt(1);
			}
			break;
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
