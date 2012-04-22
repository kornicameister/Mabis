package controller;

import model.BaseTable;

/**
 * This exception is used to protect against using certain xSQLFactory with the type
 * that inherits from x but due to sql nature can not be used in certain factory 
 * @author kornicameister
 *
 */
public class InvalidBaseClass extends Exception {
	private static final long serialVersionUID = 8100976203569162316L;

	public InvalidBaseClass() {
	}

	public InvalidBaseClass(String wanted,BaseTable invalid) {
		super("Table: " + invalid.getTableName() + " can not be used in " + wanted + "SQLFactory");
	}

}
