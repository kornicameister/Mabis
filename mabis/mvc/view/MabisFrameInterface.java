package mvc.view;

/**
 * Interfejs dla okienek uzywanych w aplikacji MABIS
 * 
 * @author tomasz
 * 
 */
public interface MabisFrameInterface {
	/**
	 * Implementacja tej metody powinna zawsze inicjalizowac wiekszosc, a
	 * najlepiej wszystkie, komponenty jakich dane okienko uzywa
	 */
	abstract void initComponents();
	/**
	 * Implementacja tej metody powinna, w sposob wlasciwy danemu okienkowi,
	 * inicjalizowac layout tegoz okienka
	 */
	abstract void layoutComponents();
}
