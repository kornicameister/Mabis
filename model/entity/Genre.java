/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;
import java.util.ArrayList;

import model.BaseTable;
import model.enums.TableType;

/**
 * Class maps itself to mabis.genre table Table structure: </br> | idGenre </br>
 * | genres </br>
 * 
 * @author kornicameister
 * 
 */
public class Genre extends BaseTable implements Serializable {
	private static final long serialVersionUID = 3862214016426775417L;

	/**
	 * Construct new Genre with default empty constructor
	 */
	public Genre() {
		super();
	}

	/**
	 * Constructs new Genre with primary key set up
	 * 
	 * @param pk
	 */
	public Genre(int pk) {
		super(pk);
	}

	/**
	 * Construct new Genre with genres value
	 * 
	 * @param genres
	 */
	public Genre(String genre) {
		super(genre);
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idGenre", "object" };
		return tmp;
	}

	/**
	 * wraps {@link BaseTable#setTitle(String)}
	 * 
	 * @param genres
	 */
	public void setGenre(String genre) {
		this.titles[0] = genre;
	}

	/**
	 * wraps {@link BaseTable#getTitle()}
	 * 
	 * @return nazwę gatunku
	 */
	public String getGenre() {
		return this.titles[0];
	}

	@Override
	protected void initInternalFields() {
		this.tableType = TableType.GENRE;
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += "----------\n";
		str += "[GENRE: " + this.getGenre() + "]\n";
		return str;
	}

	@Override
	public Object[] toColumnIdentifiers() {
		ArrayList<Object> data = new ArrayList<Object>();
		for(Object d : super.toColumnIdentifiers()){
			data.add(d);
		}
		data.add("Genre");
		return data.toArray();
	}

	@Override
	public Object[] toRowData() {
		ArrayList<Object> data = new ArrayList<Object>();
		for(Object d : super.toColumnIdentifiers()){
			data.add(d);
		}
		data.add(this.getGenre());
		return data.toArray();
	}

}
