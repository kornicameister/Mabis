package controller;

public enum SQLStamentType {
	INSERT, UPDATE, DELETE;

	/**
	 * @return lower cased {@link SQLStamentType#name()} of this enum constant
	 */
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
