/**
 * package controller in MABIS
 * by kornicameister
 */
package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.logging.Level;

import logger.MabisLogger;
import model.entity.BaseTable;
import database.MySQLAccess;

/**
 * Abstract class defining only common methods that allows to build valid SQL
 * statement. Nevertheless, <b>notice</b> that this is the job of inheriting
 * Classes to build the query which will be valid in the context of certain
 * table
 * 
 * @author kornicameister
 * 
 */
public abstract class SQLFactory implements StatementFactory {
	protected SQLStamentType type;
	protected String sqlQuery = null;
	private TreeSet<WhereClause> wheres = null;
	protected String questionMarkFieldList = null;
	protected BaseTable table = null;

	/**
	 * Constructs a SQL factory
	 * 
	 * @param type
	 */
	public SQLFactory(SQLStamentType type, BaseTable table) {
		this.table = table;
		this.wheres = new TreeSet<SQLFactory.WhereClause>();
		this.setStatementType(type);
	}

	@Override
	public void setStatementType(SQLStamentType type) {
		this.type = type;
		this.sqlQuery = this.createSQL();
	}

	@Override
	public void addWhereClause(String attribute, String value) {
		if (this.type == null || this.type.equals(SQLStamentType.INSERT)) {
			return;
		}
		this.wheres.add(new WhereClause(attribute, value));
	}

	private String createSQL() {
		String rawQueryCopy = null;
		switch (this.type) {
		case INSERT:
			rawQueryCopy = StatementFactory.insertPattern;
			break;
		case DELETE:
			rawQueryCopy= StatementFactory.updatePattern;
			break;
		case UPDATE:
			rawQueryCopy = StatementFactory.updatePattern;
			break;
		case SELECT:
			rawQueryCopy = StatementFactory.selectPattern;
			break;
		default:
			break;
		}
		rawQueryCopy = rawQueryCopy.replaceFirst("!",
				this.table.getTableName());
		String fieldList = this.buildFieldList(this.table.metaData());
		// first pass
		switch (this.type) {
		case INSERT:
			rawQueryCopy = rawQueryCopy.replaceFirst("!", fieldList);
			rawQueryCopy = rawQueryCopy.replaceFirst("!",
					this.questionMarkFieldList);
			break;
		case UPDATE:
			rawQueryCopy = rawQueryCopy.replaceFirst("!", fieldList);
			rawQueryCopy = rawQueryCopy.replaceAll(",", " = ?, ");
			break;
		default:
			break;
		}
		// second pass
		switch (this.type) {
		case UPDATE:
		case DELETE:
			rawQueryCopy = rawQueryCopy.replaceFirst("!",
					this.buildWhereChunk());
			break;
		case SELECT:
			String where = this.buildWhereChunk();
			if (where.equals("")) {
				rawQueryCopy = rawQueryCopy.substring(0,
						rawQueryCopy.lastIndexOf("where") - 1);
			} else {
				rawQueryCopy = rawQueryCopy.replaceAll("!", where);
			}
			break;
		default:
			break;
		}

		return rawQueryCopy;
	}

	@Override
	public void executeSQL(boolean online) throws SQLException {
		try {
			if (!MySQLAccess.getConnection().isValid(1000)) {
				MabisLogger.getLogger().log(Level.SEVERE,
						"Database connection lost");
				return;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		PreparedStatement st = null;

		if (online) {
			st = MySQLAccess.getOnlineConnection().prepareStatement(
					this.sqlQuery, Statement.RETURN_GENERATED_KEYS);
		} else {
			st = MySQLAccess.getConnection().prepareStatement(this.sqlQuery,
					Statement.RETURN_GENERATED_KEYS);
		}

		this.executeByTableAndType(st);

		st.close();
		st = null;
		System.gc();
	}

	/**
	 * This method is always called by extended class. This is caused by further
	 * inconsistency in executing sql queries, every class maps itself to
	 * another tables that consist different attributes, therefore
	 * {@link SQLFactory#executeByTableAndType(PreparedStatement)} method is
	 * declared abstract to allow other factories define it's own table depended
	 * statements processing
	 * 
	 * @param st
	 *            {@link PreparedStatement} object that internal sql query is
	 *            already defined and awaits to have it's values added and/or
	 *            being executed
	 * @throws SQLException
	 *             if anything goes wrong, exception is thrown and caught in
	 *             propagation way by upper calling method
	 *             {@link SQLFactory#executeSQL(boolean)}
	 */
	protected abstract void executeByTableAndType(PreparedStatement st)
			throws SQLException;

	@Override
	public String buildWhereChunk() {
		String a = new String();
		for (WhereClause where : this.wheres) {
			a += where.attribute + "=" + where.value + ",";
		}
		if (a.length() == 0) {
			return "";
		}
		return a.substring(0, a.length() - 1);
	}

	@Override
	public String buildFieldList(String[] fieldList) {
		questionMarkFieldList = "";

		String fieldList2 = new String();
		for (short i = (short) (fieldList.length - 1); i != 0; i--) {
			fieldList2 += fieldList[i] + ",";
			questionMarkFieldList += "?" + ",";
		}
		questionMarkFieldList = questionMarkFieldList.substring(0,
				questionMarkFieldList.lastIndexOf(","));
		return fieldList2.substring(0, fieldList2.lastIndexOf(","));
	}

	/**
	 * Private nested class of {@link SQLFactory} encapsulating
	 * WhereClause.</br> Class consists two publicly visible fields. Using
	 * getter schema in private nested class is negligble.
	 * 
	 * @author kornicameister
	 * 
	 */
	private class WhereClause {
		String attribute = null;
		String value = null;

		/**
		 * Constructs where clause with given value of attribute
		 * 
		 * @param attribute
		 * @param value
		 */
		public WhereClause(String attribute, String value) {
			this.attribute = attribute;
			this.value = value;
		}
	}
}
