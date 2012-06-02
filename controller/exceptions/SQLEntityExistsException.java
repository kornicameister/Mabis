package controller.exceptions;

import model.BaseTable;

public class SQLEntityExistsException extends Exception {
	final BaseTable table;
	private static final long serialVersionUID = -1492603401443874303L;

	public SQLEntityExistsException(BaseTable bt, String messString) {
		super(messString);
		this.table = bt;
	}

	public BaseTable getTable() {
		return table;
	}
}
