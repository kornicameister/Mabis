package mvc.controller.api;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;

import mvc.model.BaseTable;

/**
 * Abstrakcyjna klasa, ktora stanowi baze dla wszystkich klas dostepowych do
 * API. Definiuje ona wymagane zmienne oraz metody
 * 
 * @author tomasz
 * 
 */
public abstract class ApiAccess {
	protected TreeSet<BaseTable> result;
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public ApiAccess() {
		this.result = new TreeSet<BaseTable>();
	}

	public TreeSet<BaseTable> getResult() {
		return result;
	}

	/**
	 * Metoda abstrakcyjna. Powinna okreslac szereg czynnosci, jakie musza
	 * zostac wykonane aby pobrac dane o obiekcie kolekcji zgodnym z typem klasy
	 * API dla ktorego ta metoda zostala wywolana. I tak wlasciwe bedzie
	 * stwierdzenie, ze
	 * <p>
	 * metoda pobierze informacje o pisarzu, okladce oraz ilosci stron, jesli
	 * zostanie wywolana na rzecz obiektu {@link GoogleBookApi}
	 * </p>
	 * 
	 * @param question
	 *            zapytanie do bazy
	 * @param params
	 *            mapa parametrów w formacie string:string
	 * @throws IOException
	 */
	public abstract void query(TreeMap<String, String> query)
			throws IOException;

	/**
	 * Metoda ktorej redefinicja w klasie dziedzacej powinna zaimplementowac
	 * operacje takie, aby moc podlaczyc sie i pobrac informacje o danym
	 * obiekcie kolekcji.
	 * 
	 * @param params
	 * @return
	 * @throws IOException
	 */
	protected abstract StringBuilder accessAPI(TreeMap<String, String> params)
			throws IOException;
	protected abstract StringBuilder accessAPI(String crit) throws IOException;

	public PropertyChangeSupport getPcs() {
		return pcs;
	}
}
