package controller;

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
	void setStatementType(SQLStamentType type);

	/**
	 * 
	 * @param attribute
	 * @param value
	 */
	void addWhereClause(String attribute, String value);

	/**
	 * 
	 * @return where clause containing string in following format
	 *         <b>(?=![,?=!,?=!,...,?=!])</b> where ? stands for
	 *         <em>attribute name</em> and ! stands for <em>attribute value</em>
	 */
	String buildWhereChunk();

	/**
	 * Implementation should return valid string representation of the sql
	 * statement If sql could not have been created null string should be
	 * returned
	 * 
	 * @return string containing full valid sql statement
	 * @param table
	 *            the source of meta data
	 */
	String createSQL(BaseTable table);

	/**
	 * This must be implemented in sql factory, as some sql statement requires
	 * field list
	 * 
	 * @param table
	 * @return
	 */
	String buildFieldList(BaseTable table);
}
