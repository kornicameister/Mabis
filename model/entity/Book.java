/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import model.enums.TableType;
import model.utilities.ForeignKey;

/**
 * This class map itself to mabis.book Table structure: </br> | idBook </br> |
 * isbn </br> | titleOriginal </br> | titleLocale </br> | genre </br> | pages
 * </br> | cover </br> | writer </br>
 * 
 * @author kornicameister
 * @version 0.2
 */
public class Book extends BaseTable {
	private Short pages = null;
	private Author writer = null; // writer is a foreign key here
	private Cover cover = null; // cover is also a foreign key here
	private Genre genre = null; // genre is also a foreign key here

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
		String tmp[] = { "idBook", "isbn", "titleOriginal", "titleLocale",
				"genre", "pages", "cover", "writer" };
		return tmp;
	}

	@Override
	protected void initInternalFields() {
		this.setPages(new Short((short) 0));
		this.tableName = TableType.BOOK.toString();

		this.constraints.add(TableType.AUTHOR);
		this.constraints.add(TableType.COVER);
		this.constraints.add(TableType.GENRE);
	}

	public String getIsbn() {
		return this.titles[2];
	}

	public void setIsbn(String isbn) {
		this.titles[2] = isbn;
	}

	public Short getPages() {
		return pages;
	}

	public void setPages(Short pages) {
		this.pages = pages;
	}

	public String getDescription() {
		return this.titles[3];
	}

	public void setDescription(String description) {
		this.titles[3] = description;
	}

	public Author getWriter() {
		return writer;
	}

	public void setWriter(Author writer) {
		this.writer = writer;
	}

	public Cover getCover() {
		return cover;
	}

	public void setCover(Cover cover) {
		this.cover = cover;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}
}
