package mvc.controller.exceptions;

import mvc.model.BaseTable;

/**
 * Wyjatek rzucany zawsze wtedy, gdy okazuje sie, ze w pewnym miejscu aplikacji
 * pojawila sie nie taka tabela jak potrzeba
 * 
 * @author kornicameister
 * 
 */
public class InvalidBaseClass extends Exception {
	private static final long serialVersionUID = 8100976203569162316L;

	public InvalidBaseClass() {
	}

	public InvalidBaseClass(String wanted, BaseTable invalid) {
		super("Table: " + invalid.getTableType() + " can not be used in "
				+ wanted + "SQLFactory");
	}

}
