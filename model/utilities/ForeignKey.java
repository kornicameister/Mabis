/**
 * package model.entity.utilities in MABIS
 * by kornicameister
 */
package model.utilities;

import java.io.Serializable;

import model.BaseTable;

/**
 * This class represent object-oriented implementation of sql foreign key Class
 * can be easily sorted using customized
 * {@link ForeignKey#compareTo(ForeignKey)} method
 * 
 * @author kornicameister
 * @see Comparable
 */
public class ForeignKey implements Comparable<ForeignKey>, Serializable{
	private static final long serialVersionUID = -839442478641269523L;
	private String originTable;
	private String name;
	private Integer value;

	/**
	 * Konstruktor {@link ForeignKey}
	 * 
	 * @param targetTable
	 *            tabela do której odwołuje się klucz obcy
	 * @param keyName
	 *            nazwa klucza obcego, najlepiej nazwa atrybutu
	 * @param value
	 *            wartość klucza obcego
	 */
	public ForeignKey(BaseTable tableTarget, String keyName, Integer value) {
		this.originTable = tableTarget.getTableName();
		this.name = keyName;
		this.value = value;
	}

	/**
	 * Konstruktor {@link ForeignKey}
	 * 
	 * @param targetTable
	 *            tabela do której odwołuje się klucz obcy
	 * @param keyName
	 *            nazwa klucza obcego, najlepiej nazwa atrybutu
	 * @param value
	 *            wartość klucza obcego
	 */
	public ForeignKey(String tableTarget, String keyName, Integer value) {
		this.originTable = tableTarget;
		this.name = keyName;
		this.value = value;
	}

	/**
	 * @return the table object at which foreign key is defined as primary
	 */
	public String getOriginTable() {
		return originTable;
	}

	/**
	 * @return symbolic foreign key name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value of the foreign key
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @param originTable
	 *            the originTable to set
	 */
	public void setOriginTable(String originTable) {
		this.originTable = originTable;
	}

	/**
	 * @param name
	 *            symbolic name to be set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param value
	 *            integer representation of this foreign key
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public int compareTo(ForeignKey o) {
		int result = this.originTable.compareTo(o.getOriginTable());
		if (result == 0) {
			result = this.value.compareTo(o.getValue());
		}
		if (result == 0) {
			result = this.name.compareTo(o.getName());
		}
		return result;
	}

	@Override
	public String toString() {
		String str = "Foreign key: " + this.name + "\n";
		str += "[Val: " + this.value + "]\n";
		str += "[From: " + this.originTable + "]\n";
		return str;
	}
}
