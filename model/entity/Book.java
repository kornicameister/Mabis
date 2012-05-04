/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;

import model.BaseTable;
import model.enums.TableType;
import model.utilities.ForeignKey;

/**
 * Klasa {@link Book} jest obiektową wersją tabeli bazy danych <b>mabis.book</b>
 * o następującej strukturze: </br> | idBook </br> | isbn </br> | titleOriginal
 * </br> | titleLocale </br> | genre </br> | pages </br> | cover </br> | writer
 * </br>
 * 
 * @author kornicameister
 * @version 0.2
 */
public class Book extends Movie implements Serializable {
	private static final long serialVersionUID = -3111018571540665182L;
	private Short pages = null;

	public Book() {
		super();
	}

	public Book(String originalTitle) {
		super(originalTitle);
	}

	public Book(int pk, ForeignKey... keys) {
		super(pk, keys);
	}

	public Book(int pk) {
		super(pk);
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idBook", "object", "genreFK", "coverFK", "writerFK" };
		return tmp;
	}

	@Override
	protected void initInternalFields() {
		this.setPages(new Short((short) 0));
		this.tableName = TableType.BOOK.toString();
	}

	public String getIsbn() {
		return this.titles[3];
	}

	public void setIsbn(String isbn) {
		this.titles[3] = isbn;
	}

	public Short getPages() {
		return pages;
	}

	public void setPages(Short pages) {
		this.pages = pages;
	}

	@Override
	public String toString() {
		String str = BaseTable.class.toString();
		str += "----------\n";
		str += "[TITLE: " + this.getOriginalTitle() + "]\n";
		str += "[ISBN: " + this.getIsbn() + "]\n";
		str += "[PAGES: " + this.getPages() + "]\n";
		str += "[GENRE: " + this.getGenre() + "]\n";
		str += "[AUTHOR:" + this.getAuthor() + "]\n";
		str += "[PICTURE: " + this.getCover() + "]\n";
		return str;
	}
}
