package view.newUser;

import java.sql.SQLException;

import model.entity.User;
import controller.SQLStamentType;
import controller.entity.UserSQLFactory;

public class NewUserRegisterer {

	public static void saveToLocalDatabase(User user) throws SQLException {
		UserSQLFactory factory = new UserSQLFactory(SQLStamentType.INSERT, user);
		factory.executeSQL(true);
	}

	public static void saveToOnlineDatabase(User user) throws SQLException {
//		UserSQLFactory factory = new UserSQLFactory(SQLStamentType.INSERT, user);
//		factory.executeSQL(false);
	}

}
