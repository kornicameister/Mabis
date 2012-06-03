package mvc.view.newUser;

import java.sql.SQLException;

import mvc.controller.SQLStamentType;
import mvc.controller.entity.UserSQLFactory;
import mvc.controller.exceptions.SQLEntityExistsException;
import mvc.model.entity.User;

public class NewUserRegisterer {

	public static void saveToLocalDatabase(User user) throws SQLException, SQLEntityExistsException {
		UserSQLFactory factory = new UserSQLFactory(SQLStamentType.INSERT, user);
		factory.executeSQL(true);
	}

}
