package mvc.model.utilities;

import java.io.Serializable;

import mvc.model.interfaces.MMTable;

/**
 * Klasa zamyka w sobie funkcjonalnosc symulujaca pare kluczy obcych
 * 
 * @author kornicameister
 * @see MMTable
 * @see ForeignKey
 */
public class ForeignKeyPair implements Comparable<ForeignKeyPair>, Serializable {
	private static final long serialVersionUID = -6158282006256210527L;
	private final ForeignKey f1, f2;

	public ForeignKeyPair(ForeignKey f1, ForeignKey f2) {
		this.f1 = f1;
		this.f2 = f2;
	}

	public ForeignKey getKey(String name) {
		if (name.equals(f1.getName())) {
			return f1;
		} else if (name.equals(f2.getName())) {
			return f2;
		}
		return null;
	}

	public ForeignKey getFirstKey() {
		return f1;
	}

	public ForeignKey getSecondKey() {
		return f2;
	}

	@Override
	public int compareTo(ForeignKeyPair o) {
		Integer firstKeyCompare = o.getFirstKey().compareTo(this.getFirstKey());
		Integer secondKeyCompare = o.getSecondKey().compareTo(
				this.getSecondKey());
		return firstKeyCompare.compareTo(secondKeyCompare);
	}
}
