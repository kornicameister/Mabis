/**
 * package mabis.mvc.model.interfaces in MABIS
 * by kornicameister
 */
package mvc.model.interfaces;


/**
 * Interface that every class simulating table from sql database should
 * implement
 * 
 * @author kornicameister
 * @version 0.2
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
	 * method should return array of strings that stands for meta data of
	 * certain table
	 * 
	 * @return String[]
	 */
	String[] metaData();
}
