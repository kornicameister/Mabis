package controller;

import java.sql.SQLException;

import model.entity.BaseTable;

public interface StatementFactory {
	/**
	 * ! marks, in following order, stands for:
	 * <ol>
	 * <li>table name</li>
	 * <li>field list</li>
	 * <li>value list</li>
	 * </ol>
	 * </b> </pre>
	 */
	static final String insertPattern = "insert into ! (!) values (!)";

	/**
	 * ! marks, in following order, stands for:
	 * <ol>
	 * <li>table name</li>
	 * <li>attribute name = new value</li>
	 * <li>condition list</li>
	 * </ol>
	 * <b>Notice</b> that <b>attribute name = new value</b> has constant format
	 * of
	 * 
	 * <pre>
	 * (?=![,?=!,?=!,...,?=!])
	 * </pre>
	 * 
	 * Where question mark is abbreviation for attribute name that value is
	 * being updated and exclamation mark is abbreviation for value of attribute
	 * Exemplary sql statement as a result of {@link StatementFactory} can look
	 * like this:
	 * 
	 * <pre>
	 * <b>
	 * update foo set atr_1=val_1, atr_2=val_2 where atr_pk = 2
	 * </b>
	 * </pre>
	 */
	static final String updatePattern = "update ! set (!) where !";

	/**
	 * ! marks, in following order, stands for:
	 * <ol>
	 * <li>table name</li>
	 * <li>condition list</li>
	 * </ol>
	 * <b>Notice</b> that condition list has constant format of
	 * 
	 * <pre>
	 * (?=![,?,?,...,?])
	 * </pre>
	 * 
	 * Where question mark is abbreviation for attribute name that value is
	 * being updated and exclamation mark is abbreviation for value of attribute
	 * Exemplary sql statement as a result of {@link StatementFactory} can look
	 * like this:
	 * 
	 * <pre>
	 * <b>
	 * delete from foo where atr_name = atr_value
	 * </b>
	 * </pre>
	 */
	static final String deletePattern = "delete from ! where !";

	/**
	 * ! marks, in following order, stands for:
	 * <ol>
	 * <li>table name</li>
	 * <li>condition list</li>
	 * </ol>
	 * <b>Notice</b> that condition list has constant format of
	 * 
	 * <pre>
	 * (?=![,?,?,...,?])
	 * </pre>
	 * 
	 * Where question mark is abbreviation for attribute name that value is
	 * being updated and exclamation mark is abbreviation for value of attribute
	 * Exemplary sql statement as a result of {@link StatementFactory} can look
	 * like this:
	 * 
	 * <pre>
	 * <b>
	 * select * from table_name where atr = atr_value;
	 * </b>
	 * </pre>
	 */
	static final String selectPattern = "select * from ! where !";

	/**
	 * This field indicates the type of sql statement being created
	 * 
	 * @param type
	 */
	abstract void setStatementType(SQLStamentType type);

	/**
	 * This method is undeniably required in sql factory Via this method
	 * end-user can provide where chunks to sql factory that in further future
	 * will be concatenated into one hue sql where clause
	 * 
	 * Due to convenience reason, all values to where chunk are provided as
	 * strings, this forces not to use data inapplicable to where clause such as
	 * blobs.
	 * 
	 * @param attributeName
	 * @param value
	 */
	abstract void addWhereClause(String attributeName, String value);

	/**
	 * 
	 * @return where clause containing string in following format
	 *         <b>(?=![,?=!,?=!,...,?=!])</b> where ? stands for
	 *         <em>attribute name</em> and ! stands for <em>attribute value</em>
	 */
	abstract String buildWhereChunk();

	/**
	 * This must be implemented in sql factory, as some sql statement requires
	 * field list. This method should be provided with attribute list of the
	 * table that sql factory comma concerns about.
	 * 
	 * @param fieldList
	 *            , an array of fields name
	 * @return field list as single string, consecutive fields are separated
	 *         with
	 */
	abstract String buildFieldList(String[] fieldList);

	/**
	 * Method executes sql statement If something goes wrong,
	 * {@link SQLException} will be thrown </br> <b>Notice</b> that
	 * implementation can use whatever algorithm user prefers to, nevertheless
	 * it should depends on what {@link StatementFactory#createSQL(BaseTable)}
	 * returns
	 * 
	 * @param local
	 *            if set to true than statements will be executed upon local
	 *            database, otherwise upon online database
	 * @return 
	 * @throws SQLException
	 */
	abstract Integer executeSQL(boolean local) throws SQLException;
}
