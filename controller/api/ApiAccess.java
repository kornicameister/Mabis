package controller.api;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

public abstract class ApiAccess {
	protected ArrayList<URL> accessPoints;

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
