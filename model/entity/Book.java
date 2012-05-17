/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import model.BaseTable;
import model.enums.BookIndustryIdentifier;
import model.enums.TableType;
import model.utilities.ForeignKey;
import settings.GlobalPaths;

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

	private TreeMap<BookIndustryIdentifier, String> getIdentifiers() {
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

	@Override
	public int compareTo(BaseTable o) {
		int result = super.compareTo(o);
		Book other = (Book) o;
		if (result == 0) {
			result = this.getOriginalTitle().compareTo(other.getOriginalTitle());
		}
		if (result == 0) {
			result = this.getDescription().compareTo(other.getDescription());
		}
		return result;
	}

	@Override
	public Object[] toColumnIdentifiers() {
		ArrayList<Object> data = new ArrayList<Object>();
		for (Object d : super.toColumnIdentifiers()) {
			data.add(d);
		}
		data.set(4, "Pages");
		data.set(5, "Writer");
		data.add(5, "Identifier number");
		return data.toArray();
	}

	@Override
	public Object[] toRowData() {
		ArrayList<Object> data = new ArrayList<Object>();
		for (Object d : super.toRowData()) {
			data.add(d);
		}
		data.set(4, this.getPages());
		data.add(5, this.getIdentifiers());
		return data.toArray();
	}

	public URL toDescription() {
		String str = new String();
		str += "<html>";
		str += "<p><b>ID:</b>" + this.getPrimaryKey() + "</p>";
		str += "<p><b>Title:</b>" + this.getOriginalTitle() + "</p>";
		if(this.getLocalizedTitle() != null && !this.getLocalizedTitle().isEmpty()){
			str += "<b><i>Subtitle:</i></b>" + this.getLocalizedTitle() + "</p>";
		}
		str += "<p><b>Pages:</b>" + this.getPages() + "</p>";
		if(this.getAuthors() != null && !this.getAuthors().isEmpty()){
			str += "<b>Authors:</b>";
			str += "<ul>";
			for (Author author : this.getAuthors()) {
				str += "<li>" + author.getFirstName() + " " + author.getLastName() + "</li>";
			}
			str += "</ul>";
		}
		str += "</html>";
		
		DataOutputStream dos = null;
		String path = GlobalPaths.TMP + String.valueOf(Math.random());
		try {
			dos = new DataOutputStream(new FileOutputStream(new File(path)));
			dos.writeBytes(str);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(dos != null){
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			URL ulr = new URL("file:///" + path);
			return ulr;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
