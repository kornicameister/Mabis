/**
 * package view in MABIS
 * by kornicameister
 */
package view;

import javax.swing.JFrame;

/**
 * This is an about window where all informations describing application purpose
 * and functionality are located at
 * 
 * @author kornicameister
 * @version 0.1
 * @see java.swing.JFrame
 */
public class AboutWindow extends JFrame {

	/**
	 * The value of this constant is {@value} and it was autogenerated
	 */
	private static final long serialVersionUID = 6992601172376070322L;
	
	public AboutWindow() {
		super("About");
		setSize(300, 200);
	}
}
