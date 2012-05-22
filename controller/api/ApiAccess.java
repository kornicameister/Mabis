package controller.api;

import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;

import model.BaseTable;

public abstract class ApiAccess {
	protected TreeSet<BaseTable> result;

	public ApiAccess() {
		this.result = new TreeSet<BaseTable>();
	}

	public TreeSet<BaseTable> getResult() {
		return result;
	}

	/**
	 * Metoda abstrakcyjna. Powinna określać, definiować szereg czynności
	 * związanych z wykonaniem zapytania przez API celem stworzenia pełnego
	 * obiektu kolekcji.
	 * 
	 * @param question
	 *            zapytanie do bazy
	 * @param params
	 *            mapa parametrów w formacie string:string
	 * @throws IOException
	 */
	public abstract void query(TreeMap<String, String> query)
			throws IOException;
}
