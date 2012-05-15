package controller.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

public abstract class ApiAccess {
	protected ArrayList<URL> accessPoints;

	public ApiAccess() {
		try {
			this.accessPoints = this.setApiAccessPointList();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
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
	public abstract void query(String question, TreeMap<String, String> params)
			throws IOException;

	/**
	 * Metoda wywołana przez konsttruktor klasy bazowej {@link ApiAccess}.
	 * Definicja metody w klasach dziedziczących powinna utworzyć listę
	 * {@link URL} wskazujących na adresy publicznych API serwisów internetowych
	 * dostarczających informacje o multimediach, etc.
	 * 
	 * @return referencję do {@link ArrayList} linków do publicznych API
	 * @throws MalformedURLException
	 */
	protected abstract ArrayList<URL> setApiAccessPointList()
			throws MalformedURLException;
}
