/**
 * package controller in MABIS
 * by kornicameister
 */
package controller;

import java.util.TreeMap;

import model.entity.BaseTable;

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
	protected String rawQuery = null;
	private TreeMap<String, WhereClause> wheres = null;
	protected String questionMarkFieldList;

	public SQLFactory() {
		this.type = null;
		this.wheres = new TreeMap<String, SQLFactory.WhereClause>();
	}

	/**
	 * This method is to set valid instance of one of the class that inherits
	 * from BaseTable
	 * 
	 * @param table
	 * @throws InvalidBaseClass
	 */
	public abstract void setTable(BaseTable table) throws InvalidBaseClass;

	@Override
	public void setStatementType(SQLStamentType type) {
		this.type = type;
		switch (this.type) {
		case INSERT:
			this.rawQuery = StatementFactory.insertPattern;
			break;
		case DELETE:
			this.rawQuery = StatementFactory.updatePattern;
			break;
		case UPDATE:
			this.rawQuery = StatementFactory.updatePattern;
			break;
		case SELECT:
			this.rawQuery = StatementFactory.selectPattern;
			break;
		}
	}

	@Override
	public void addWhereClause(String attribute, String value) {
		if (this.type == null || this.type.equals(SQLStamentType.INSERT)) {
			return;
		}
		this.wheres.put(attribute, new WhereClause(attribute, value));
	}

	public String createSQL(BaseTable table){
		String rawQueryCopy = this.rawQuery.replaceFirst("!",
				table.getTableName());
		
		if(this.type == SQLStamentType.SELECT){
			String where = this.buildWhereChunk();
			if(where.equals("")){
				rawQueryCopy = rawQueryCopy.substring(0,rawQueryCopy.lastIndexOf("where")-1);
			}else{
				rawQueryCopy = rawQueryCopy.replaceAll("!", where);
			}
			return rawQueryCopy;
		}
		
		String fieldList = this.buildFieldList(table);

		// setting field list
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
		}
		switch (this.type) {
		case UPDATE:
		case DELETE:
			rawQueryCopy = rawQueryCopy.replaceFirst("!",
					this.buildWhereChunk());
			break;
		}

		return rawQueryCopy;
	}

	@Override
	public String buildWhereChunk() {
		String a = new String();
		for (WhereClause where : this.wheres.values()) {
			a += where.attribute + "=" + where.value + ",";
		}
		if(a.length() == 0){
			return "";
		}
		return a.substring(0, a.length() - 1);
	}
	
	@Override
	public String buildFieldList(BaseTable table) {
		String[] fieldList = table.metaData();
		questionMarkFieldList = "";

		String fieldList2 = new String();
		for (short i = (short) (fieldList.length - 1); i != 0; i--) {
			fieldList2 += fieldList[i] + ",";
			questionMarkFieldList += "?" + ",";
		}
		questionMarkFieldList = questionMarkFieldList.substring(0,questionMarkFieldList.lastIndexOf(","));
		return fieldList2.substring(0, fieldList2.lastIndexOf(","));
	}

	public abstract boolean executeSQL();

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
