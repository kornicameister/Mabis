/**
 * package mabis.mvc.view.utilities in MABIS
 * by kornicameister
 */
package mvc.view.utilities;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

/**
 * Klasa bazuje na JLabel i symuluje dzialanie StatusBar
 * 
 * @author kornicameister
 * @see http 
 *      ://www.java-tips.org/java-se-tips/javax.swing/creating-a-status-bar.html
 */
public class StatusBar extends JLabel {
	private static final long serialVersionUID = 6160242403078374821L;

	/**
	 * Domyslny konstruktor, definiuje wszystkie najwazniejsze ustawienia tego
	 * komponentu, wliczajac w to
	 * <ul>
	 * <li>rozmiar</li>
	 * <li>wiadomosc poczatkowa</li>
	 * </ul>
	 * 
	 * @see JLabel#JLabel()
	 */
	public StatusBar() {
		super();
		setMinimumSize(new Dimension(500, 20));
		setPreferredSize(new Dimension(500, 20));
		setMaximumSize(new Dimension(500, 20));
		setMessage("StatusBar ready");
	}

	/**
	 * Ustawia wiadomosc, kolor dla wiadomosci -> czarny
	 * 
	 * @param string
	 *            wiadomosc
	 */
	public void setMessage(String string) {
		this.setForeground(Color.black);
		this.setText(" " + string);
	}

	/**
	 * Ustawia wiadomosc, kolor dla bledu -> czerwony
	 * 
	 * @param err
	 *            wiadomosc
	 */
	public void setError(String err) {
		this.setForeground(Color.red);
		this.setText(" " + err);
	}

}
