/**
 * package mabis.mvc.model.entity in MABIS
 * by kornicameister
 */
package mvc.model;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.TreeMap;

import mvc.model.interfaces.MMTable;
import mvc.model.utilities.ForeignKey;
import mvc.model.utilities.ForeignKeyPair;

/**
 * {@link ManyToManyTable} extends {@link BaseTable} class by defining
 * background for all tables that job is to connect two ordinary tables
 * 
 * @author kornicameister
 */
public abstract class ManyToManyTable extends BaseTable implements MMTable,
		Serializable {
	private static final long serialVersionUID = -3084535642990380335L;
	private TreeMap<Integer, ForeignKeyPair> mapOfKeys;

	/**
	 * Construct new MM table using tableType as it's name
	 * 
	 * @param tableType
	 */
	public ManyToManyTable() {
		super();
	}

	@Override
	public String toString() {
		String str = "Table: " + tableType + "\n";
		str += "[PK: " + this.getPrimaryKey() + "]\n";
		str += "[FKS]\n";
		for (Entry<Integer, ForeignKeyPair> fff : this.mapOfKeys.entrySet()) {
			str += "[Entry_" + fff.getKey() + ": "
					+ fff.getValue().getFirstKey().toString() + ";"
					+ fff.getValue().getSecondKey().toString() + "]\n";
		}
		return str;
	}

	public void addMultiForeignKey(Integer id, ForeignKey f1, ForeignKey f2) {
		this.mapOfKeys.put(id, new ForeignKeyPair(f1, f2));
	}

	public ForeignKeyPair getMultiForeing(Integer id) {
		return this.mapOfKeys.get(id);
	}

	@Override
	protected void initInternalFields() {
		this.mapOfKeys = new TreeMap<Integer, ForeignKeyPair>();
	}

	/**
	 * @deprecated due to ManyToMany table nature this method is deprecated,
	 *             instead use addMultiForeingKey
	 */
	@Deprecated
	public void addForeingKey(ForeignKey key) {
		return;
	}

	/**
	 * @deprecated due to ManyToMany table nature this method is deprecated,
	 *             instead use getMultiForeingKey THIS METHOD RETURNS NULL BY
	 *             DEFAULT
	 */
	@Deprecated
	public TreeMap<String, ForeignKey> getForeingKeys() {
		return null;
	}

	@Override
	// TODO - NOT YET IMPLEMENTED
	public Integer primaryKeyFor(ForeignKeyPair pair) {
		if (this.mapOfKeys.containsValue(pair)) {
		}
		return -1;
	}
}
