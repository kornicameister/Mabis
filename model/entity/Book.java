/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import model.enums.TableType;
import model.utilities.ForeignKey;

/**
 * @author kornicameister
 * 
 */
public class Book extends BaseTable {
	private String isbn = null;
	private Short pages = null;
	private String description = null;

	/**
	 * @param originalTitle
	 */
	public Book(String originalTitle) {
		super(originalTitle);
	}

	/**
	 * @param pk
	 * @param keys
	 */
	public Book(int pk, ForeignKey... keys) {
		super(pk, keys);
	}

	/**
	 * @param pk
	 */
	public Book(int pk) {
		super(pk);
	}

	@Override
	protected void initInternalFields() {
		this.setIsbn(new String());
		this.setPages(new Short((short) 0));
		this.setDescription(new String());
		this.constraints.add(TableType.AUTHOR);
		this.constraints.add(TableType.COVER);
		this.constraints.add(TableType.GENRE);
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Short getPages() {
		return pages;
	}

	public void setPages(Short pages) {
		this.pages = pages;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
