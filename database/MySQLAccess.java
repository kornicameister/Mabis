/**
 * package database in MABIS
 * by kornicameister
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.entity.User;

import controller.InvalidBaseClass;
import controller.SQLStamentType;
import controller.entity.UserSQLFactory;

/**
 * This class wraps for MySQL establish connection process.</br> It allows to
 * set connection information such as
 * <ul>
 * <li>login</li>
 * <li>password (note that password is echoed throug md5sum)</li>
 * <li>host</li>
 * </ul>
 * 
 * @author kornicameister
 * @see <b>mysql java driver</b>
 * @see ConnectionData#setPassword(String)
 * @version 0.2
 */
public class MySQLAccess {

	/** The Constant databaseName. */
	protected final static String databaseName = "mabis";

	/** The Constant userName. */
	protected final static String userName = "mabisUser";

	/** The Constant userPass. */
	protected final static String userPass = "f72158bc8d";

	/** The Constant defaultPort. */
	protected final static Short defaultPort = 3306;

	/** The Constant defaultHost */
	protected final static String host = "localhost";

	/** The connection. */
	private static Connection connection = null;

	/**
	 * Instantiates a new my sql access.
	 */
	public MySQLAccess() {

	}

	/**
	 * <b>Connect</b> method loads the <b>MySQL</b> driver and than tries to
	 * establish new connection using
	 * <ul>
	 * <li>{@link MySQLAccess#databaseName}</li>
	 * <li>{@link MySQLAccess#userName}</li>
	 * <li>{@link MySQLAccess#userPass}</li>
	 * </ul>
	 * and data passed to {@link ConnectionData} object
	 * <ul>
	 * <li> {@link ConnectionData#port}</li>
	 * </ul>
	 * 
	 * @return boolean </br><b>TRUE</b> if connection had been established,
	 *         otherwise return false
	 * @since 0.2 method does not check for user presence in user table, this
	 *        job was moved to main window
	 */
	public boolean connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = new String("jdbc:mysql://!:!/!");
			url = url.replaceFirst("!", MySQLAccess.host);
			url = url.replaceFirst("!", MySQLAccess.defaultPort.toString());
			url = url.replaceFirst("!", MySQLAccess.databaseName);
			MySQLAccess.connection = DriverManager.getConnection(url,
					MySQLAccess.userName, MySQLAccess.userPass);
			if (!MySQLAccess.connection.isClosed()) {
				System.out.println("Connection established");
				return true;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * <b>Check for user</b> method execute simple sql query that check if user
	 * identified by {@link ConnectionData#login} is a valid application user
	 * 
	 * @return boolean </br><b>TRUE</b> user was found</br><b>FALSE</b>
	 *         otherwise
	 */
	public boolean doWeHaveUser(String user) {
		UserSQLFactory f = new UserSQLFactory();
		try {
			f.setTable(new User());
			f.setStatementType(SQLStamentType.SELECT);
			f.executeSQL();
		} catch (InvalidBaseClass e) {
			e.printStackTrace();
		}
		return !f.getUsers().isEmpty();
	}

	/**
	 * <b>Disconnect</b> safely closses up the existing connection to database
	 * identified by {@link MySQLAccess#databaseName}
	 */
	public void disconnect() {
		if (MySQLAccess.connection != null) {
			try {
				MySQLAccess.connection.close();
				if (MySQLAccess.connection.isClosed()) {
					System.out.println("Connection terminated");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ResultSet executeSQL(String sql) throws SQLException {
		return connection.prepareStatement(sql).executeQuery();
	}

	public ResultSet executeSQL(PreparedStatement st) throws SQLException {
		return st.executeQuery();
	}

	public static Connection getConnection() {
		return connection;
	}
}
