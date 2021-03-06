package mvc.controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import mvc.controller.SQLStamentType;
import mvc.model.BaseTable;
import mvc.model.entity.Band;

/**
 * Klasa opisuje operacje bazodanowe, ktore wykonywane sa na tabeli movie
 * zgodnie z danymi i parametrami przekazanymi przez {@link Band} lub metody
 * dostępowe tejze klasy
 * 
 * @author tomasz
 * 
 */
public class BandSQLFactory extends AuthorSQLFactory {
	private final TreeSet<Band> bands = new TreeSet<Band>();

	public BandSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Band band = null;
		switch (this.type) {
			case SELECT :
				while (set.next()) {
					byte[] buf = set.getBytes("object");
					if (buf != null) {
						try {
							ObjectInputStream objectIn = new ObjectInputStream(
									new ByteArrayInputStream(buf));
							band = (Band) objectIn.readObject();
							band.setPrimaryKey(set.getInt("idAuthor"));
							this.bands.add(band);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
					band = null;
				}
				break;
			default :
				break;
		}
		set.close();
	}

	/**
	 * Zwraca pobrane zespoły
	 * 
	 * @return the bands
	 */
	public TreeSet<Band> getBands() {
		return bands;
	}

}
