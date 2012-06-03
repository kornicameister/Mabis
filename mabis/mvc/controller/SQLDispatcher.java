package mvc.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import mvc.controller.exceptions.SQLEntityExistsException;


public class SQLDispatcher implements Runnable {

	private final Stack<SQLFactory> factories;
	private final List<SQLFactory> synchronizedFactories;

	public SQLDispatcher() {
		this.factories = new Stack<>();
		this.synchronizedFactories = Collections
				.synchronizedList(this.factories);
	}

	public void addFactory(final SQLFactory sf) {
		this.synchronizedFactories.add(sf);
	}

	@Override
	public void run() {
		for (SQLFactory sf : this.synchronizedFactories) {
			try {
				sf.executeSQL(true);
			} catch (SQLException | SQLEntityExistsException e) {
				e.printStackTrace();
			}
		}
	}
}
