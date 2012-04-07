/**
 * package view in MABIS
 * by kornicameister
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author kornicameister
 * 
 */
public class MainWindowListener implements ActionListener {
	private MainWindow mw = null;

	/**
	 * Constructs MainWindowListener with mainWindow arg
	 * 
	 * @param mainWindow
	 *            reference to MainWindow. It is used to control boolean flags
	 *            located in MainWindo
	 */
	public MainWindowListener(MainWindow mainWindow) {
		this.mw = mainWindow;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object s = arg0.getSource();
		if (s.equals(mw.bRef.aboutMe)) {
			System.out.println("About me action");
		} else if (s.equals(mw.bRef.editButton)) {
			System.out.println("Edit item action");
		} else if (s.equals(mw.bRef.exitButton)) {
			System.out.println("Exit action");
		} else if (s.equals(mw.bRef.newItem)) {
			System.out.println("New item action");
		} else if (s.equals(mw.bRef.publishButton)) {
			System.out.println("Publish action");
		} else if (s.equals(mw.bRef.toogleConnection)) {
			System.out.println("Toogle action");
		}
	}
}
