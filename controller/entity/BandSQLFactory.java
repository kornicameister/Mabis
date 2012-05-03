package controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.Band;
import controller.SQLFactory;
import controller.SQLStamentType;

public class BandSQLFactory extends SQLFactory {
	private final TreeSet<Band> bands = new TreeSet<Band>();

	public BandSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		// TODO Auto-generated method stub

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
