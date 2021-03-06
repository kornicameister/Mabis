package mvc.model.utilities;

import java.io.Serializable;

import mvc.model.enums.BookIndustryIdentifierType;

/**
 * Prosta klasa opisująca identyfikator producenta nadany książce.
 * 
 * @author kornicameister
 * 
 */
public class BookIndustryIdentifier
		implements
			Serializable,
			Comparable<BookIndustryIdentifier> {
	private static final long serialVersionUID = -6019553424259345124L;
	private BookIndustryIdentifierType type;
	private String value;

	/**
	 * Tworzy nowy identifykator producenta dla ksiazki, ktory opisywany jest
	 * przez {@link BookIndustryIdentifierType} typ oraz jego wartosc
	 * 
	 * @param type
	 *            typ identifikatora
	 * @param value
	 *            wartosc identyfikatora
	 */
	public BookIndustryIdentifier(BookIndustryIdentifierType type, String value) {
		super();
		this.type = type;
		this.value = value;
	}

	public BookIndustryIdentifierType getType() {
		return type;
	}

	public void setType(BookIndustryIdentifierType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(BookIndustryIdentifier o) {
		return this.value.compareTo(o.value);
	}

	@Override
	public String toString() {
		return this.type.toString() + " :: " + this.value + "\n";
	}
}
