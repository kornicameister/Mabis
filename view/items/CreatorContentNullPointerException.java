/**
 * 
 */
package view.items;

/**
 * Klasa wyjątku dla {@link ItemCreator}. Wyjątek rzucany jest wtedy, kiedy nie
 * został zdefiniowany content dla ItemCreatora, który jest integralną i
 * niezbędną częścią każdego ItemCreatora.
 * 
 * @author kornicameister
 * 
 */
public class CreatorContentNullPointerException extends NullPointerException {
	private static final long serialVersionUID = 2714963506513787769L;

	/**
	 * Domyślny konstruktor wyjątku {@link CreatorContentNullPointerException}
	 */
	public CreatorContentNullPointerException() {
		super();
	}

	/**
	 * @param s
	 *            wiadomość do wyświetlenia
	 */
	public CreatorContentNullPointerException(String s) {
		super(s);
	}

	/**
	 * Konstruktor przyjmujący dwa parametry. Jeden z nich jest obiektem klasy
	 * {@link ItemCreator}, z którego pobierana jest nazwa klasy, gdzie pojawił
	 * się wyjątek, drugi jest wiadomością który ma być dołączona do treści
	 * wyjątku.
	 * 
	 * @param ic
	 * @param s
	 */
	public CreatorContentNullPointerException(ItemCreator ic, String s) {
		super(ic.getClass().getSimpleName() + " :: " + s);
	}

}
