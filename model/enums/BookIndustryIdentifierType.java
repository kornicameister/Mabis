package model.enums;

/**
 * Enum opisuje typ numeru identyfikacyjnego jaki może być do książki
 */
public enum BookIndustryIdentifierType {
	ISBN_10, ISBN_13, OCLC, UOM, OTHER;

	public static BookIndustryIdentifierType findType(String str) {
		if (str.equals(ISBN_10.toString())
				|| str.equals(ISBN_10.toString().toLowerCase())) {
			return ISBN_10;
		}
		if (str.equals(ISBN_13.toString())
				|| str.equals(ISBN_13.toString().toLowerCase())) {
			return ISBN_13;
		}
		if (str.equals(OCLC.toString())
				|| str.equals(OCLC.toString().toLowerCase())) {
			return OCLC;
		}
		if (str.equals(UOM.toString())
				|| str.equals(UOM.toString().toLowerCase())) {
			return UOM;
		}
		return OTHER;
	}
}
