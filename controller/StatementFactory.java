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
	static final String updatePattern = "update ! set (!) where !=!";

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
	 * Sets target table for all types of queries ( {@link SQLStamentType} )
	 * This table name will be used in every kind of statement. Additionaly
	 * <b>insert</b> statement should use BaseTable fields to create valid sql
	 * statement
	 * 
	 * @param table
	 */
	void setTargetTable(BaseTable table);

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
	String buildWhereClause();

	/**
	 * Implementation should return valid string representation of the sql
	 * statement If sql could not have been created null string should be
	 * returned
	 * 
	 * @return string containing full valid sql statement
	 */
	String createSQL();
}
