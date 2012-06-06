/**
 * 
 */
package mvc.controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import mvc.controller.SQLFactory;
import mvc.controller.SQLStamentType;
import mvc.controller.exceptions.SQLEntityExistsException;
import mvc.model.BaseTable;
import mvc.model.entity.Author;
import mvc.model.entity.Picture;
import mvc.model.enums.TableType;
import utilities.Hasher;
import utilities.Utilities;

/**
 * Klasa opisuje operacje bazodanowe, ktore wykonywane sa na tabeli movie
 * zgodnie z danymi i parametrami przekazanymi przez {@link Author} lub metody
 * dostępowe tejze klasy
 * 
 * @author tomasz
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
			case UPDATE :
				st.setInt(1, author.getPrimaryKey());
				this.parseDeleteSet(st.executeUpdate());
				break;
			case INSERT :
				this.insertEntity(author, st);
				break;
			case SELECT :
				this.parseResultSet(st.executeQuery());
				break;
			case DELETE :
				this.deletePicture(author.getAvatar());
				this.parseDeleteSet(st.executeUpdate());
				break;
			default :
				break;
		}
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Author author = null;
		switch (this.type) {
			case UPDATE :
				break;
			case SELECT :
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
			default :
				break;
		}
		set.close();
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
		if (!entity.getTableType().equals(TableType.USER)) {
			try {
				BaseTable tmp = this.checkIfInserted();
				if (tmp != null) {
					this.lastAffactedId = tmp.getPrimaryKey();
					return;
				}
			} catch (SQLEntityExistsException e) {
				e.printStackTrace();
			}
			st.setString(1, Hasher.hashString(entity.getTitle()));
			st.setString(2, entity.getType().toString());
			Integer pictureID = this.insertAvatar(entity.getAvatar());
			if (pictureID != null) {
				st.setInt(3, pictureID);
			} else {
				st.setString(3, null);
			}
			st.setObject(4, entity);
		} else {
			st.setObject(2, entity);
			st.setInt(1, this.insertAvatar(entity.getAvatar()));
		}
		st.execute();
		st.clearParameters();
		this.lastAffactedId = Utilities.lastInsertedId(entity, st);
		entity.setPrimaryKey(this.lastAffactedId);
	}

	/**
	 * Metoda umieszcza w bazie danych informacje o avatarze danego autora
	 * 
	 * @param picture
	 * @return
	 * @throws SQLException
	 */
	private Integer insertAvatar(Picture picture) throws SQLException {
		if (picture != null) {
			PictureSQLFactory psf = new PictureSQLFactory(
					SQLStamentType.INSERT, picture);
			try {
				this.lastAffactedId = psf.executeSQL(false);
			} catch (SQLEntityExistsException e) {
				e.printStackTrace();
			}
			return lastAffactedId;
		}
		return null;
	}

	/**
	 * Zwraca pobranych autorów
	 * 
	 * @return {@link TreeSet} z pobranymi aktorami
	 */
	public TreeSet<Author> getAuthors() {
		return this.authors;
	}

	@Override
	public BaseTable checkIfInserted() throws SQLException,
			SQLEntityExistsException {
		AuthorSQLFactory asf = new AuthorSQLFactory(SQLStamentType.SELECT,
				this.table);
		asf.addWhereClause("hash", Hasher.hashString(this.table.getTitle()));
		asf.executeSQL(true);
		for (Author a : asf.getAuthors()) {
			return a;
		}
		return null;
	}
}
