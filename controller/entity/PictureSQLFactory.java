/**
 * 
 */
package controller.entity;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

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