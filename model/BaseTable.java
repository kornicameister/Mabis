/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.TreeSet;

import model.entity.AudioAlbum;
import model.entity.Book;
import model.entity.Movie;
import model.enums.TableType;
import model.interfaces.Table;
import model.utilities.ForeignKey;
import exceptions.SQLForeingKeyException;
import exceptions.SQLForeingKeyNotFound;

/**
 * Klasa {@link BaseTable} będąc klasą abstrakcyjną dostarcza podstawową
 * funkcjonalność jaką powinna reprezentować tabela relacyjnej bazy danych.
 * Implementując interfejs {@link Serializable} daje możliwość zapisu obiektów
 * do bazy danych bez konieczności rozbijania operacji na operacje elementarne
 * na poszczególnych atrybutach danej tabeli.
 * 
 * @author kornicameister
 * @version 0.3
 */
public abstract class BaseTable implements Table, Comparable<BaseTable>,
		Serializable {
	private static final long serialVersionUID = 8934017748567797527L;
	private Integer primaryKey = null;
	protected String[] titles = null;
	protected TreeSet<TableType> constraints = null;
	protected TreeMap<String, ForeignKey> foreignKeys;
	protected String tableName = "empty";

	/**
	 * Empty constructor, only initializes variables, used when new entity
	 * object was created and which is at the moment not in the database
	 */
	public BaseTable() {
		this.initFields();
		this.initInternalFields();
	}

	/**
	 * Constructs BaseTable with title in it's original language
	 * 
	 * @param originalTitle
	 */
	public BaseTable(String originalTitle) {
		this.initFields();
		this.initInternalFields();
		this.titles[0] = originalTitle;
	}

	/**
	 * Constructs an Table with and primaryKey
	 * 
	 * @param pk
	 *            if Table is new object that is about to be inserted into DB,
	 *            pk (<b>primary key</b>) should be set to < 0 value otherwise
	 *            primaryKey represents actual identifier of row
	 */
	public BaseTable(int pk) {
		this.initFields();
		this.initInternalFields();
		this.primaryKey = pk;
	}

	/**
	 * 
	 * @param pk
	 *            if Table is new object that is about to be inserted into DB,
	 *            pk (<b>primary key</b>) should be set to < 0 value otherwise
	 *            primaryKey represents actual identifier of row
	 * @param key
	 *            represents author's primary key in context of foreign key for
	 *            Table.</br> This key context can be different depends on which
	 *            extending class object was created. In case of:
	 *            <ul>
	 *            <li> {@link AudioAlbum} - this will refer to band table primary
	 *            key</li>
	 *            <li> {@link Book} - this will refer to writer table primary key
	 *            </li>
	 *            <li> {@link Movie} - this will refer to director table primary
	 *            key</li>
	 *            </ul>
	 * @see ForeignKey
	 */
	public BaseTable(int pk, ForeignKey... keys) {
		this.initFields();
		this.initInternalFields();
	}

	/**
	 * intializes all fields of this class
	 */
	private void initFields() {
		this.primaryKey = new Integer(-1);
		this.titles = new String[4];
		this.constraints = new TreeSet<TableType>();
		this.foreignKeys = new TreeMap<String, ForeignKey>();
	}

	/**
	 * overridden by extending class, called always as triggering call is
	 * located in {@link BaseTable} constructor takes care of initializing all
	 * internal fields that belongs to extending classes
	 */
	protected abstract void initInternalFields();

	/**
	 * @return title of a table
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @return an array of titles with indexed identified further by
	 *         {@link TitleType}
	 */
	public String[] getTitles() {
		String t[] = new String[2];
		t[0] = this.titles[0];
		t[1] = this.titles[1];
		return t;
	}

	/**
	 * set original title
	 * 
	 * @param t
	 */
	public void setOriginalTitle(String t) {
		this.titles[0] = t;
	}

	/**
	 * set localized title
	 * 
	 * @param t
	 */
	public void setLocalizedTitle(String t) {
		this.titles[1] = t;
	}

	/**
	 * @return title in it's original language
	 */
	public String getOriginalTitle() {
		return this.titles[0];
	}

	/**
	 * @return title being localized (for example in user_language)
	 */
	public String getLocalizedTitle() {
		return this.titles[1];
	}

	@Override
	public Integer getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Integer id) {
		this.primaryKey = id;
	}

	@Override
	public ForeignKey getForeingKey(String name) throws SQLForeingKeyNotFound {
		if (!this.foreignKeys.containsKey(name)) {
			throw new SQLForeingKeyNotFound(name, this);
		}
		return this.foreignKeys.get(name);
	}

	@Override
	public TreeMap<String, ForeignKey> getForeingKeys() {
		return this.foreignKeys;
	}

	@Override
	public void addForeingKey(ForeignKey key) {
		this.foreignKeys.put(key.getName(), key);
	}

	@Override
	public void checkConstraints(ForeignKey... keys)
			throws SQLForeingKeyNotFound, SQLForeingKeyException {
		if (keys.length > this.constraints.size()) {
			throw new SQLForeingKeyException(keys.length, 3);
		}
		for (ForeignKey foreignKey : keys) {
			if (constraints.contains(foreignKey)) {
				this.addForeingKey(foreignKey);
			} else {
				throw new SQLForeingKeyNotFound(foreignKey.getName(), this);
			}
		}
	}

	@Override
	public String toString() {
		String str = "Table: " + tableName + "\n";
		str += "[PK: " + this.primaryKey + "]\n";
		str += "[FKS]\n";
		for (ForeignKey fk : this.foreignKeys.values()) {
			str += fk.toString();
		}
		return str;
	}

	/**
	 * Domyślny komparator, który porównuje po nazwie tabeli. Jeśli tabele są
	 * identyczne, sprawdza klucze główne.
	 */
	@Override
	public int compareTo(BaseTable o) {
		int compareValue = 0;
		compareValue = this.getTableName().compareTo(o.getTableName());
		if (compareValue == 0) {
			// the same table, lets check primary keys
			compareValue = this.getPrimaryKey().compareTo(o.getPrimaryKey());
		}
		return compareValue;
	}
}
