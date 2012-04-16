package controller;

/**
 * Enum to standardize type of the query {@link SQLFactory} extending class may
 * want to perform
 * 
 * @author kornicameister
 * 
 */
public enum SQLStamentType {
	INSERT, UPDATE, DELETE, SELECT;

	/**
	 * @return lower cased {@link SQLStamentType#name()} of this enum constant
	 */
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
