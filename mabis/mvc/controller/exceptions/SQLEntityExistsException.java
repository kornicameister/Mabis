package mvc.controller.exceptions;

import mvc.model.BaseTable;

/**
 * Wyjatek rzucany wtedy, kiedy okaze sie, ze obiekt, ktory uzytkownik sprobuje
 * umiescic w bazie danych, jest juz tam obecny
 * 
 * @author tomasz
 * 
 */
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
