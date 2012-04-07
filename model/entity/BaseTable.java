/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import model.enums.TableType;
import model.interfaces.Table;
import model.utilities.ForeignKey;
import exceptions.SQLForeingKeyException;
import exceptions.SQLForeingKeyNotFound;

/**
 * @author kornicameister
 * 
 */
public abstract class BaseTable implements Table {
	// TODO: comment this bullshit
	private Integer primaryKey = null;
	private String titleOriginal = null;
	private String titleLocale = null;
	private ArrayList<Cover> covers = null;
	private TreeMap<String, ForeignKey> foreignKeys = null;
	protected TreeSet<TableType> constraints = null;
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
		this.titleOriginal = originalTitle;
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
		try {
			this.checkConstraints(keys);
		} catch (SQLForeingKeyNotFound e) {
			e.printStackTrace();
		} catch (SQLForeingKeyException e) {
			e.printStackTrace();
		} finally {
			this.constraints.clear();
		}
	}

	/**
	 * intializes all fields of this class
	 */
	private void initFields() {
		this.primaryKey = new Integer(-1);
		this.titleLocale = new String("");
		this.titleOriginal = new String("");
		this.covers = new ArrayList<Cover>();
		this.foreignKeys = new TreeMap<String, ForeignKey>();
		this.constraints = new TreeSet<TableType>();
	}

	/**
	 * overridden by extending class, called always as triggering call is
	 * located in {@link BaseTable} constructor takes care of initializing all
	 * internal fields that belongs to extending classes
	 */
	protected abstract void initInternalFields();

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
		t[0] = this.titleOriginal;
		t[1] = this.titleLocale;
		return t;
	}

	/**
	 * set original title
	 * 
	 * @param t
	 */
	public void setOriginalTitle(String t) {
		this.titleOriginal = t;
	}

	/**
	 * set localized title
	 * 
	 * @param t
	 */
	public void setLocalizedTitle(String t) {
		this.titleLocale = t;
	}

	/**
	 * @return title in it's original language
	 */
	public String getOriginalTitle() {
		return this.titleOriginal;
	}

	/**
	 * @return title being localized (for example in user_language)
	 */
	public String getLocalizedTitle() {
		return this.titleLocale;
	}

	/**
	 * @return the covers
	 * @see Cover
	 */
	public ArrayList<Cover> getCovers() {
		return covers;
	}

	/**
	 * adds a new cover
	 * 
	 * @param cover
	 */
	public void addCover(Cover cover) {
		this.covers.add(cover);
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
		if (this.foreignKeys.containsKey(name)) {
			return this.foreignKeys.get(name);
		} else {
			throw new SQLForeingKeyNotFound(name, this);
		}
	}

	@Override
	public void addForeingKey(ForeignKey key) {
		this.foreignKeys.put(key.getName(), key);
	}

	@Override
	public TreeMap<String, ForeignKey> getForeingKeys() {
		return this.foreignKeys;
	}

	@Override
	public String toString() {
		String str = "Table: " + tableName + "\n";
		str += "[PK: " + this.primaryKey + "]\n]";
		str += "[FKS]\n";
		for (ForeignKey fk : this.foreignKeys.values()) {
			str += fk.toString();
		}
		str += "[TITLES]\n";
		if (!this.titleOriginal.isEmpty())
			str += "\t[ORIGINAL:" + this.titleOriginal + "]\n";
		if (!this.titleLocale.isEmpty())
			str += "\t[LOCALE:" + this.titleLocale + "]\n";
		str += "[COVERS]\n";
		for (Cover c : this.covers) {
			str += c.toString();
		}
		return str;
	}
}
