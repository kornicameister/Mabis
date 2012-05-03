/**
 * 
 */
package controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.Author;
import model.entity.User;
import utilities.Utilities;
import controller.SQLFactory;
import controller.SQLStamentType;

/**
 * @author kornicameister
 * 
 */
public class AuthorSQLFactory extends SQLFactory {
	private final TreeSet<Author> authors = new TreeSet<Author>();

	public AuthorSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		Author author = (Author) this.table;
		switch (this.type) {
		case UPDATE:
			st.setInt(1, author.getPrimaryKey());
			this.parseDeleteSet(st.executeUpdate());
			break;
		case INSERT:
			this.insertEntity(author, st);
			break;
		case SELECT:
			this.parseResultSet(st.executeQuery());
			break;
		case DELETE:
			st.setInt(1, author.getPrimaryKey());
			this.parseDeleteSet(st.executeUpdate());
			break;
		default:
			break;
		}
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Author author = null;
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
						author = (Author) objectIn.readObject();
						author.setPrimaryKey(set.getInt("idAuthor"));
						this.authors.add(author);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				author = null;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Metoda umieszcza w {@link PreparedStatement} zserializowany obiekt klasy
	 * {@link Author}. Dodatkowo zajmuje się umieszczeniem w bazie danych
	 * informacji o avatarze danego autora.
	 * 
	 * @param entity
	 *            obiekt klasy {@link Author}, który ma być umieszczony w bazie
	 *            danych
	 * @param st
	 *            obiekt klasy {@link PreparedStatement}
	 * @throws SQLException
	 */
	protected void insertEntity(Author entity, PreparedStatement st)
			throws SQLException {
		int picturePK = this.insertAvatar();
		entity.getPictureFile().setPrimaryKey(picturePK);
		st.setInt(1, picturePK);
		st.setObject(2, entity);
		st.execute();
		st.clearParameters();
		this.lastAffactedId = Utilities.lastInsertedId(entity, st);
	}

	/**
	 * Metoda umieszcza w bazie danych informacje o avatarze danego autora
	 * 
	 * @return
	 * @throws SQLException
	 */
	private Integer insertAvatar() throws SQLException {
		PictureSQLFactory psf = new PictureSQLFactory(SQLStamentType.INSERT,
				((User) table).getPictureFile());
		this.lastAffactedId = psf.executeSQL(localDatabase);
		return lastAffactedId;
	}
}
