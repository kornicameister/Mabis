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

import exceptions.ConnectionDataException;

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

	/**
	 * Wrapper allowing to set connection related data
	 * 
	 * @see ConnectionData
	 * */
	private final ConnectionData connectionData;

	/** The connection. */
	private Connection connection = null;

	/**
	 * Instantiates a new my sql access.
	 */
	public MySQLAccess() {
		this.connectionData = new ConnectionData();
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
	 * @return boolean </br><b>TRUE</b> if connection had been established and
	 *         {@link ConnectionData#login} is valid user </br> <b>FALSE</b> -
	 *         connection was established but database is closed
	 */
	public boolean connect() {
		try {

			if (!this.connectionData.isReady()) {
				throw new ConnectionDataException("Connection message invalid",
						this.connectionData);
			}

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = new String("jdbc:mysql://!:!/!");
			url = url.replaceFirst("!", this.connectionData.getHost());
			url = url.replaceFirst("!", this.connectionData.getPort()
					.toString());
			url = url.replaceFirst("!", MySQLAccess.databaseName);
			this.connection = DriverManager.getConnection(url,
					MySQLAccess.userName, MySQLAccess.userPass);
			if (!this.connection.isClosed()) {
				System.out.println("Connection established");
				return checkForUser();
			} else {
				return false;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ConnectionDataException e) {
			e.printDataError();
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
	public boolean checkForUser() {
		try {
			PreparedStatement check = connection
					.prepareStatement("select count(*) from user where login=?");
			check.setString(1, this.connectionData.getLogin());
			return Utilities.querySize(check.executeQuery()) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * <b>Disconnect</b> safely closses up the existing connection to database
	 * identified by {@link MySQLAccess#databaseName}
	 */
	public void disconnect() {
		if (this.connection != null) {
			try {
				this.connection.close();
				if (this.connection.isClosed()) {
					System.out.println("Connection terminated");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method returns reference to connectionData object, that itself allows to
	 * set up future connection parameters
	 * 
	 * @return the connectionData
	 * @see ConnectionData
	 */
	public ConnectionData getConnectionData() {
		return connectionData;
	}

	/**
	 * The Class ConnectionData.
	 */
	public class ConnectionData {

		/** The host. */
		private String login, password, host;

		/** The port, by default it is equal to {@link MySQLAccess#defaultPort} */
		private Short port;

		/**
		 * Instantiates a new connection data.
		 */
		public ConnectionData() {
			this.login = new String();
			this.password = new String();
			this.host = new String();
			this.port = new Short(MySQLAccess.defaultPort);
		}

		/**
		 * Checks if is ready.
		 * 
		 * @return the boolean
		 */
		public Boolean isReady() {
			boolean ready = false;
			ready = !this.login.isEmpty();
			ready = !this.password.isEmpty();
			ready = !this.host.isEmpty();
			return ready;
		}

		/**
		 * @return the login
		 */
		public String getLogin() {
			return login;
		}

		/**
		 * @param login
		 *            the login to set
		 */
		public void setLogin(String login) {
			this.login = login;
		}

		/**
		 * @return the host
		 */
		public String getHost() {
			return host;
		}

		/**
		 * @param host
		 *            the host to set
		 */
		public void setHost(String host) {
			this.host = host;
		}

		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * @param password
		 *            the password to set
		 */
		public void setPassword(String password) {
			this.password = Utilities.md5sum(password);
		}

		/**
		 * @return the port
		 */
		public Short getPort() {
			return port;
		}

		/**
		 * @param port
		 *            the port to set
		 */
		public void setPort(Short port) {
			this.port = port;
		}
	}

	public ResultSet executeSQL(String sql) throws SQLException {
		return connection.prepareStatement(sql).executeQuery();
	}
}
