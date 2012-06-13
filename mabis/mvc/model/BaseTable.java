/**
 * package mabis.mvc.model.entity in MABIS
 * by kornicameister
 */
package mvc.model;

import java.io.Serializable;

import mvc.model.entity.AudioAlbum;
import mvc.model.entity.Book;
import mvc.model.entity.Movie;
import mvc.model.enums.TableType;
import mvc.model.interfaces.Table;
import mvc.model.utilities.ForeignKey;

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
public abstract class BaseTable
		implements
			Table,
			Comparable<BaseTable>,
			Serializable {
	private static final long serialVersionUID = 8934017748567797527L;
	private Integer primaryKey;
	protected String[] titles;
	protected TableType tableType;

	/**
	 * Tworzy obiekt {@link BaseTable}. Konstruktor jest pusty i jedynie pozwala
	 * na inicjalizacje pol klasy
	 */
	public BaseTable() {
		this.initFields();
		this.initInternalFields();
	}

	/**
	 * Konstruktor tworzacy obiekt {@link BaseTable} ze stringiem, ktory
	 * umieszczany jest na pierwszym miejscu tabeli stringow
	 * 
	 * @param str_1 string do umieszczenia wewnatrz {@link BaseTable}
	 */
	public BaseTable(String str_1) {
		this.initFields();
		this.initInternalFields();
		this.titles[0] = str_1;
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
	 * @param keys
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
		for (int i = 0; i < this.titles.length; i++) {
			this.titles[i] = new String();
		}
		this.tableType = TableType.NULL;
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
	public TableType getTableType() {
		return tableType;
	}

	/**
	 * @return an array of titles, first title is originalTitle, the second one
	 *         referes to translated title
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
	public void setTitle(String t) {
		this.titles[0] = t;
	}

	/**
	 * set localized title
	 * 
	 * @param t
	 */
	public void setSubTitle(String t) {
		this.titles[1] = t;
	}

	/**
	 * @return title in it's original language
	 */
	public String getTitle() {
		return this.titles[0];
	}

	/**
	 * @return subtitle of this collection's item
	 */
	public String getSubtitle() {
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
	public String toString() {
		String str = "Table: " + tableType + "\n";
		str += "[ID: " + this.primaryKey + "]\n";
		return str;
	}

	/**
	 * Domyślny komparator, który porównuje po nazwie tabeli. Jeśli tabele są
	 * identyczne, sprawdza klucze główne.
	 */
	@Override
	public int compareTo(BaseTable o) {
		int compareValue = 0;
		if (o == null) {
			return 0;
		}
		compareValue = this.getTableType().compareTo(o.getTableType());
		if (compareValue == 0) {
			compareValue = this.getPrimaryKey().compareTo(o.getPrimaryKey());
		}
		return compareValue;
	}

	@Override
	public boolean equals(Object obj) {
		return this.compareTo((BaseTable) obj) == 0;
	}

}
