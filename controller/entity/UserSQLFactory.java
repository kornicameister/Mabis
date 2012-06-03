package controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import model.entity.User;
import controller.SQLStamentType;

/**
 * Klasa jest wrapperem, który zawiera metody pozwalające na wykonywanie
 * operacji (insert,delete,update,select) z tabeli User znajdującej się w bazie
 * danych mabis. Klasa dziedziczy z {@link UserSQLFactory} ponieważ operacja
 * typu INSERT jest identyczna dla obu z tych klas. Ponadto klasa User
 * dziedziczy z klasy Update, więc dziedziczenie wrapperów jest tym bardziej
 * uzasadnione.
 * 
 * @author kornicameister
 * 
 */
public class UserSQLFactory extends AuthorSQLFactory {
	private final HashMap<Integer, User> users = new HashMap<Integer, User>();

	public UserSQLFactory(SQLStamentType type, User table) {
		super(type, table);
	}

	@Override
	final protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		User user = (User) this.table;
		switch (this.type) {
		case UPDATE:
			break;
		case INSERT:
			this.insertEntity(user, st);
			break;
		case SELECT:
			this.parseResultSet(st.executeQuery());
			break;
		case DELETE:
			this.deletePicture(user.getAvatar());
			this.parseDeleteSet(st.executeUpdate());
			break;
		default:
			break;
		}
	}

	@Override
	final protected void parseResultSet(ResultSet set) throws SQLException {
		User user = null;
		switch (this.type) {
		case SELECT:
			while (set.next()) {
				byte[] buf = set.getBytes("object");
				if (buf != null) {
					try {
						ObjectInputStream objectIn = new ObjectInputStream(
								new ByteArrayInputStream(buf));
						user = (User) objectIn.readObject();
						user.setPrimaryKey(set.getInt("idUser"));
						this.users.put(user.getPrimaryKey(), user);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				user = null;
			}
			break;
		case UPDATE:
			break;
		default:
			break;
		}
		set.close();
	}

	public HashMap<Integer, User> getUsers() {
		return this.users;
	}
}
