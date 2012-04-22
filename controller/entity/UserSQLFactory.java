package controller.entity;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import logger.MabisLogger;
import model.entity.Picture;
import model.entity.User;
import model.enums.ImageType;
import model.utilities.ForeignKey;
import settings.GlobalPaths;
import utilities.Hasher;
import controller.SQLFactory;
import controller.SQLStamentType;

/**
 * This is the wrapper that allows to perform database specific operation to
 * User table content
 * 
 * @author kornicameister
 * 
 */
public class UserSQLFactory extends SQLFactory {
	private HashMap<Integer, User> users;
	private final String selectFromView = "SELECT * FROM mabis.UserListView";

	public UserSQLFactory(SQLStamentType type, User table) {
		super(type, table);
		users = new HashMap<Integer, User>();
	}

	@Override
	final protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		User u = (User) this.table;
		switch (this.type) {
		case UPDATE:
			break;
		case INSERT:
			short index = 1;
			st.setInt(index++, this.insertAvatar());
			st.setString(index++, u.getLastName());
			st.setString(index++, u.getFirstName());
			st.setString(index++, Hasher.hashPassword(u.getPassword()));
			st.setString(index++, u.getEmail());
			st.setString(index++, u.getLogin());
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
		case SELECT:
			this.parseResultSet(st.executeQuery(this.selectFromView));
			break;
		case DELETE:
			break;
		default:
			break;
		}
	}

	@Override
	final protected void parseResultSet(ResultSet set) throws SQLException {
		User u = null;
		switch (this.type) {
		case SELECT:
			while (set.next()) {
				u = new User();
				u.setFirstName(set.getString("firstName"));
				u.setLastName(set.getString("lastName"));
				u.setEmail(set.getString("email"));
				u.setLogin(set.getString("login"));
				u.setPassword(set.getString("password"));
				u.setPrimaryKey(set.getInt("idUser"));
				u.setPicture(this.selectAvatar(set.getString("image"),
						set.getString("hash")));
				u.addForeingKey(new ForeignKey("picture", "avatar", set
						.getInt("idPicture")));
				this.users.put(u.getPrimaryKey(), u);
			}
			break;
		case UPDATE:
			break;
		case DELETE:
			break;
		case INSERT:
			while (set.next()) {
				this.lastAffactedId = set.getInt(1);
			}
			break;
		default:
			break;
		}
	}

	private Picture selectAvatar(String path, String checkSum) {
		Picture p = null;
		try {
			p = new Picture(path, ImageType.AVATAR);
			if (p.getCheckSum().equals(checkSum)) {
				return p;
			} else {
				MabisLogger.getLogger().log(Level.WARNING,
						"Checksum of images does not match");
				p = new Picture(GlobalPaths.DEFAULT_COVER_PATH.toString(),
						ImageType.UNDEFINED);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return p;
	}

	private Integer insertAvatar() throws SQLException {
		PictureSQLFactory psf = new PictureSQLFactory(SQLStamentType.INSERT,
				((User) table).getPictureFile());
		this.lastAffactedId = psf.executeSQL(localDatabase);
		return lastAffactedId;
	}

	public HashMap<Integer, User> getUsers() {
		return this.users;
	}
}
