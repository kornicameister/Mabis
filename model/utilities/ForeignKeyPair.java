package model.utilities;

import model.interfaces.MMTable;

/**
 * Small addition, class encapsulates two foreign keys into pair of it Strongly
 * used in Many-to-many tables
 * 
 * @author kornicameister
 * @see MMTable
 */
public class ForeignKeyPair {
	public ForeignKey f1, f2;

	public ForeignKeyPair(ForeignKey f1, ForeignKey f2) {
		this.f1 = f1;
		this.f2 = f2;
	}
}
