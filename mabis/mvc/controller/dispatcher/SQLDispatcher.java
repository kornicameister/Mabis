package mvc.controller.dispatcher;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import logger.MabisLogger;
import mvc.controller.database.MySQLAccess;
import mvc.controller.exceptions.SQLEntityExistsException;

/**
 * Klasa, ktorej zadaniem jest wykonanie zespolu kwerend sql w oddzielnym watku
 * oraz zapewnienie transakcji
 * 
 * @author tomasz
 * @see Runnable
 */
public class SQLDispatcher implements Runnable {
	private SQLFactory factory;

	public SQLDispatcher(SQLFactory sf) {
		this.factory = sf;
	}

	@Override
	public void run() {
		try {
			if (!MySQLAccess.getConnection().isValid(1000)) {
				MabisLogger.getLogger().log(Level.SEVERE, "Database connection lost");
				this.factory.lastAffactedId = -1;
				return;
			}
			
			PreparedStatement st = MySQLAccess.getConnection().prepareStatement(factory.createSQL(), Statement.RETURN_GENERATED_KEYS);
			factory.executeByTableAndType(st);
			st.close();
			
			if (factory.entityAlreadyInserted) {
				throw new SQLEntityExistsException(factory.table,
						factory.table.getTitle() + " already exists");
			}

			System.gc();
		} catch (SQLException | SQLEntityExistsException e) {
			e.printStackTrace();
		}
	}

	public Integer getID() {
		return this.factory.lastAffactedId;
	}

}
