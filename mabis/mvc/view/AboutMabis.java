/**
 * package mabis.mvc.view in MABIS
 * by kornicameister
 */
package mvc.view;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Klasa pozwalaja na podglad informacji o aplikacji.
 * 
 * @author kornicameister
 * @version 0.1
 * @see java.swing.JFrame
 */
public class AboutMabis extends JFrame implements MabisFrameInterface {
	private static final long serialVersionUID = 6992601172376070322L;
	private JTextArea description;

	public AboutMabis() {
		super("About");
		setSize(300, 200);
	}

	@Override
	public void initComponents() {
		this.description = new JTextArea();
		this.add(new JScrollPane(this.description));
		
//		this.description.setTe
	}

	@Override
	public void layoutComponents() {

	}
}
