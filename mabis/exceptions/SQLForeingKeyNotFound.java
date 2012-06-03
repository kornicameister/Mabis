/**
 * package mabis.exceptions in MABIS
 * by kornicameister
 */
package exceptions;

import mvc.model.BaseTable;

/**
 * @author kornicameister
 * 
 */
// TODO comments to be added
public class SQLForeingKeyNotFound extends Exception {

	private static final long serialVersionUID = -3723790779908957017L;
	private static final String message = "Following key: ! was not found in table: !";
	public SQLForeingKeyNotFound(String wantedKey, BaseTable table) {
		super(message.replaceFirst("!", wantedKey).replaceFirst("!",
				table.getTableType().toString()));
	}
}
