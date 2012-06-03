package mvc.model.interfaces;

import mvc.model.utilities.ForeignKey;
import mvc.model.utilities.ForeignKeyPair;

/**
 * Interface for Many to many tables
 * <b>Notice</b> that classes using it should deprecate using methods supporting single foreing keys;
 * @author kornicameister
 *
 */
public interface MMTable extends Table {

	void addMultiForeignKey(Integer id, ForeignKey f1, ForeignKey f2);
	ForeignKeyPair getMultiForeing(Integer id);
	Integer primaryKeyFor(ForeignKeyPair pair);
}
