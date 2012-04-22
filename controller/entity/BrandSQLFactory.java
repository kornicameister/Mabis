package controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.entity.BaseTable;
import controller.SQLFactory;
import controller.SQLStamentType;

public class BrandSQLFactory extends SQLFactory {

	public BrandSQLFactory(SQLStamentType type, BaseTable table) {
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

}
