/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import model.BaseTable;
import model.enums.BookIndustryIdentifier;
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
	private Integer pages;
	private TreeMap<BookIndustryIdentifier, String> identifiers;

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
		super.initInternalFields();
		this.setPages(0);
		this.tableName = TableType.BOOK.toString();
		this.identifiers = new TreeMap<BookIndustryIdentifier, String>();
	}

	public String getIdentifier(BookIndustryIdentifier bii) {
		return this.identifiers.get(bii);
	}

	public void addIdentifier(BookIndustryIdentifier bii, String val) {
		this.identifiers.put(bii, val);
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer integer) {
		this.pages = integer;
	}

	@Override
	public String toString() {
		String str = BaseTable.class.toString();
		str += "----------\n";
		str += "[TITLE: " + this.getOriginalTitle() + "]\n";
		str += "[IDENTIFIERS:]\n";
		Iterator<Map.Entry<BookIndustryIdentifier, String>> it = this.identifiers
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<BookIndustryIdentifier, String> entry = it.next();
			str += "\t" + entry.getKey() + " -> " + entry.getValue() + "\t";
		}
		str += "[PAGES: " + this.getPages() + "]\n";
		str += "[GENRE: " + this.getGenre() + "]\n";
		str += "[RATING: " + this.getRating() + "]\n";
		for (Author a : this.directors) {
			str += "\t" + a.toString() + "\n";
		}
		str += "[PICTURE: " + this.getCover() + "]\n";
		return str;
	}
}
