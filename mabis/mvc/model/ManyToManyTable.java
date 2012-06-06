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
 * {@link ManyToManyTable} rozszerza {@link BaseTable} poprzez definicje
 * zachowania wlasciwego dla tabel <strong>many-to-many</strong>
 * 
 * @author kornicameister
 */
public abstract class ManyToManyTable extends BaseTable
		implements
			MMTable,
			Serializable {
	private static final long serialVersionUID = -3084535642990380335L;
	private TreeMap<Integer, ForeignKeyPair> mapOfKeys;

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

	/**
	 * Metoda dodaje nowy klucz multi
	 * 
	 * @param id
	 *            numer porzadkowy klucza wiele-do-wielu
	 * @param f1
	 *            pierwsza czesc klucza
	 * @param f2
	 *            druga czesc klucza
	 */
	public void addMultiForeignKey(Integer id, ForeignKey f1, ForeignKey f2) {
		this.mapOfKeys.put(id, new ForeignKeyPair(f1, f2));
	}

	/**
	 * Zwraca {@link ForeignKeyPair}
	 */
	public ForeignKeyPair getMultiForeing(Integer id) {
		return this.mapOfKeys.get(id);
	}

	@Override
	protected void initInternalFields() {
		this.mapOfKeys = new TreeMap<Integer, ForeignKeyPair>();
	}

	/**
	 * @deprecated z uwagi na nature tej klasy, metoda jest oznaczona jako
	 *             niezalecane do uzycia. Zamiast niej nalezy dodawac klucze
	 *             jako pare poprzez
	 *             {@link ManyToManyTable#addMultiForeignKey(Integer, ForeignKey, ForeignKey)}
	 */
	@Deprecated
	public void addForeingKey(ForeignKey key) {
		return;
	}

	/**
	 * @deprecated metoda niedostepna, zawsze zwraca null, poniewaz tabela
	 *             <strong>many-to-many</strong> zawiera klucze obce parami
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
