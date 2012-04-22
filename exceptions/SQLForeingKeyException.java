/**
 * package exceptions in MABIS
 * by kornicameister
 */
package exceptions;

import model.BaseTable;
import model.utilities.ForeignKeyPair;

/**
 * @author kornicameister
 * 
 */
// TODO comentary work
public class SQLForeingKeyException extends Exception {

	/**
	 * The value of this constant is {@value} and it was autogenerated
	 */
	private static final long serialVersionUID = 7727405758826418186L;
	private static final String message_2 = "Following keys ! are invalid in case of !! table";
	private static final String message_3 = "Required keys: !, Keys being inserted: !";

	public SQLForeingKeyException(ForeignKeyPair pair, BaseTable target) {
		super(message_2.replaceFirst("!", pair.toString()).replaceFirst("!!",
				target.getTableName()));
	}

	public SQLForeingKeyException(int count, int wanted) {
		super(message_3.replaceFirst("!", String.valueOf(wanted)).replaceFirst(
				"!", String.valueOf(count)));
	}
}
