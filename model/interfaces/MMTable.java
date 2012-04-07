package model.interfaces;

import model.utilities.ForeignKey;
import model.utilities.ForeignKeyPair;

public interface MMTable extends Table {

	void addMultiForeignKey(Integer id, ForeignKey f1, ForeignKey f2);
	ForeignKeyPair getMultiForeing(Integer id);
	Integer primaryKeyFor(ForeignKeyPair pair);
}
