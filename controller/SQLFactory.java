/**
 * package controller in MABIS
 * by kornicameister
 */
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TreeSet;

import javax.swing.ImageIcon;

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
	private TreeSet<WhereClause> wheres = null;
	protected String questionMarkFieldList;

	/**
	 * Constructs a SQL factory
	 */
	public SQLFactory() {
		this.type = null;
		this.wheres = new TreeSet<SQLFactory.WhereClause>();
	}

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
		this.wheres.add(new WhereClause(attribute, value));
	}

	public String createSQL(BaseTable table) {
		String rawQueryCopy = this.rawQuery.replaceFirst("!",
				table.getTableName());
		String fieldList = this.buildFieldList(table.metaData());
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
		}

		return rawQueryCopy;
	}

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

	public void setImageAsBlob(PreparedStatement st, File imageFile, short index) {
		try {
			FileInputStream fis = new FileInputStream(imageFile);
			st.setBinaryStream(index, (InputStream) fis, (int) imageFile.length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ImageIcon createImageFromBlob(Blob blob) {
		try {
			ImageIcon i = new ImageIcon(blob.getBytes(1, (int) blob.length()));
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
