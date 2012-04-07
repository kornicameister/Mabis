/**
 * package model.interfaces in MABIS
 * by kornicameister
 */
package model.interfaces;

import java.util.TreeMap;

import model.entity.BaseTable;
import model.utilities.ForeignKey;
import exceptions.SQLForeingKeyException;
import exceptions.SQLForeingKeyNotFound;

/**
 * @author kornicameister
 * 
 */
public interface Table {

	/**
	 * @return the primaryKey
	 */
	Integer getPrimaryKey();

	/**
	 * @param primaryKey
	 *            the primaryKey to set
	 */
	void setPrimaryKey(Integer id);

	/**
	 * method look up in foreing keys and tries to locate integer value of it,
	 * otherwise throws an InvalidForeingKey exception
	 * 
	 * @param name
	 * @return foreing key associated with the name
	 * @throws SQLForeingKeyNotFound
	 */
	ForeignKey getForeingKey(String name) throws SQLForeingKeyNotFound;

	/**
	 * @return sorted map of all foreing keys
	 * @see TreeMap
	 * @see ForeignKey
	 */
	TreeMap<String, ForeignKey> getForeingKeys();

	/**
	 * add new foreing key
	 * 
	 * @param name
	 * @param value
	 */
	void addForeingKey(ForeignKey key);

	/**
	 * Called only if following constructor is called <b>
	 * {@link BaseTable#BaseTable(int, ForeignKey...)} </b> <br>
	 * Implementation tries to find out if provided {@link ForeignKey} keys are
	 * valid in context of extending class {@link BaseTable#constraints}</br>
	 * 
	 * @param keys
	 * @throws SQLForeingKeyNotFound
	 * @throws SQLForeingKeyException
	 */
	void checkConstraints(ForeignKey... keys) throws SQLForeingKeyNotFound,
			SQLForeingKeyException;

	/**
	 * 
	 * @return comma separated names of column of the table
	 */
	String metaData();

	/**
	 * <b>Notice</b> this method is to work in cooperation with
	 * {@link Table#metaData()} as first index contains metaData returned string
	 * 
	 * @return comma separated names of column of the table in first index and
	 *         corresponding values in the second index
	 */
	String[] rawData();
}
