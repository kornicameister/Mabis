/**
 * package database in MABIS
 * by kornicameister
 */
package controller.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
 * @see mysql java driver
 * @see ConnectionData#setPassword(String)
 * @version 0.2
 */
public class MySQLAccess {

	/** The Constant databaseName. */
	private final static String databaseName = "mabis";

	/** The Constant userName. */
	private final static String userName = "mabisUser";

	/** The Constant userPass. */
	private final static String userPass = "mabisPass";

	/** The Constant defaultPort. */
	private final static Short defaultPort = 3306;

	/** The Constant defaultHost */
	private final static String host = "localhost";

	/** The connection. */
	private static Connection connection = MySQLAccess.connectLocally();

	/**
	 * Instantiates a new my sql access.
	 */
	public MySQLAccess() {
		if (MySQLAccess.connection == null) {
			MySQLAccess.connectLocally();
		}
	}

	/**
	 * <b>Connect</b> method loads the <b>MySQL</b> driver and than tries to
	 * establish new connection using
	 * <ul>
	 * <li>{@link MySQLAccess#databaseName}</li>
	 * <li>{@link MySQLAccess#userName}</li>
	 * <li>{@link MySQLAccess#userPass}</li>
	 * <li>{@link MySQLAccess#host}</li>
	 * <li>{@link MySQLAccess#defaultPort}</li>
	 * </ul>
	 * <b>Notice that is method is both private and static, that means that it
	 * is called even earlier that right creation of the {@link MySQLAccess},
	 * that is dictated by need of having {@link MySQLAccess#connection}
	 * available even without right {@link MySQLAccess} object</b>
	 * 
	 * @return {@link Connection} object if succeeded, otherwise returns null
	 * @since 0.2 method does not check for user presence in user table, this
	 *        job was moved to main window
	 */
	private static Connection connectLocally() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://!:!/!";
			url = url.replaceFirst("!", MySQLAccess.host);
			url = url.replaceFirst("!", MySQLAccess.defaultPort.toString());
			url = url.replaceFirst("!", MySQLAccess.databaseName);
			return connection = DriverManager.getConnection(url,
					MySQLAccess.userName, MySQLAccess.userPass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <b>Disconnect</b> safely closses up the existing connection to database
	 * identified by {@link MySQLAccess#databaseName}
	 */
	public static void disconnect() {
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

	/**
	 * Static getter for the Connection object !
	 * 
	 * @return referencję do obiektu tego połączenia
	 */
	public static Connection getConnection() {
		return connection;
	}

	// property methods
	/**
	 * 
	 * @return host for localhost connections
	 */
	public static String getLocalhost() {
		return host;
	}

	/**
	 * 
	 * @return username of the local mabis database
	 */
	public static String getLocalUser() {
		return userName;
	}

	/**
	 * 
	 * @return database name
	 */
	public static String getDatabaseName() {
		return databaseName;
	}

	/**
	 * 
	 * @return port via mysql connections is established
	 */
	public static Short getPort() {
		return defaultPort;
	}

	// property methods

	public boolean isConnected() throws SQLException {
		if (MySQLAccess.connection != null) {
			return !MySQLAccess.connection.isClosed();
		}
		return false;
	}

	/**
	 * Method is overriden to ensure that connections to databases will be
	 * closed
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		MySQLAccess.disconnect();
	}

}
