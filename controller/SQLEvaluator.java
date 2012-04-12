/**
 * package controller in MABIS
 * by kornicameister
 */
package controller;

import java.sql.SQLException;
import java.util.TreeMap;

import model.entity.BaseTable;
import database.MySQLAccess;

/**
 * @author kornicameister
 * 
 */
public class SQLEvaluator implements StatementFactory {
	private BaseTable target = null;
	private SQLStamentType type;
	private TreeMap<String, WhereClause> wheres = null;

	public SQLEvaluator() {
		this.target = null;
		this.type = null;
		this.wheres = new TreeMap<String, SQLEvaluator.WhereClause>();
	}
	@Override
	public void setStatementType(SQLStamentType type) {
		this.type = type;
	}

	@Override
	public void addWhereClause(String attribute, String value) {
		if (this.type == null || this.type.equals(SQLStamentType.INSERT)) {
			return;
		}
		this.wheres.put(attribute, new WhereClause(attribute, value));
	}

	@Override
	public String createSQL() {
		String sql = null;

		switch (type) {
			case UPDATE :
				sql = StatementFactory.updatePattern;
				// table name
				sql = sql.replaceFirst("!", this.target.getTableName());
				// value list
				sql = sql.replaceFirst("!", "");
				// where clause
				sql = sql.replaceFirst("!", this.buildWhereClause());
				break;
			case INSERT :
				sql = StatementFactory.insertPattern;
				// table name
				sql = sql.replaceFirst("!", this.target.getTableName());
				// field list
//				sql = sql.replaceFirst("!", data[0]);
//				// values
//				sql = sql.replaceFirst("!", data[1]);
				break;
			case DELETE :
				sql = StatementFactory.updatePattern;
				// table name
				sql = sql.replaceFirst("!", this.target.getTableName());
				// where clauses
				sql = sql.replaceFirst("!", this.buildWhereClause());
				break;
		default:
			break;
		}
		return sql;
	}
	@Override
	public void setTargetTable(BaseTable table) {
		this.target = table;
	}

	@Override
	public String buildWhereClause() {
		String a = new String();
		for (WhereClause where : this.wheres.values()) {
			a += where.attribute + "=" + where.value + ",";
		}
		return a.substring(0, a.length() - 1);
	}

	public boolean executeSQL() {
		String sql = this.createSQL();

		MySQLAccess db = new MySQLAccess();
		if (!db.connect()) {
			return false;
		}

		try {
			db.executeSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		db.disconnect();
		return false;
	}

	private class WhereClause {
		String attribute = null;
		String value = null;

		/**
		 * @param attribute
		 * @param value
		 */
		public WhereClause(String attribute, String value) {
			this.attribute = attribute;
			this.value = value;
		}
	}
}
