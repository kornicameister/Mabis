/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;

import model.BaseTable;
import model.enums.TableType;
import model.utilities.BookIndustryIdentifier;
import model.utilities.ForeignKey;

/**
 * Klasa {@link Book} jest obiektową wersją tabeli bazy danych <b>mabis.book</b>
 * o następującej strukturze: </br> | idBook </br> | isbn </br> | titleOriginal
 * </br> | titleLocale </br> | genres </br> | pages </br> | cover </br> | writer
 * </br>
 * 
 * @author kornicameister
 * @version 0.2
 */
public class Book extends Movie implements Serializable {
	private static final long serialVersionUID = -3111018571540665182L;
	private Integer pages;
	private TreeSet<BookIndustryIdentifier> identifiers;

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
		String tmp[] = { 	"idBook", 
							"object",
							"title",
							"writerFK", 
							"coverFK",
							"genreFK"};
		return tmp;
	}

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
		this.setPages(0);
		this.tableType = TableType.BOOK;
		this.identifiers = new TreeSet<>();
	}

	public void addIdentifier(BookIndustryIdentifier t) {
		this.identifiers.add(t);
	}

	public void setIdentifiers(Collection<BookIndustryIdentifier> ii) {
		this.identifiers.addAll(ii);
	}
	
	public TreeSet<BookIndustryIdentifier> getIdentifiers() {
		return this.identifiers;
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
		str += "[TITLE: " + this.getTitle() + "]\n";
		str += "[IDENTIFIERS:]\n";
		for (BookIndustryIdentifier bii : this.getIdentifiers()) {
			str += bii.toString();
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

	@Override
	public int compareTo(BaseTable o) {
		int result = super.compareTo(o);
		Book other = (Book) o;
		if (result == 0) {
			result = this.getTitle().compareTo(other.getTitle());
		}
		if (result == 0 && (this.getDescription() != null)) {
			result = this.getDescription().compareTo(other.getDescription());
		}
		return result;
	}
	
}
