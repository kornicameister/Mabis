package startPoint;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mvc.view.firstrun.FirstRunWizard;
import settings.GlobalPaths;
import settings.io.LastRunDescription;
import settings.io.SettingsException;
import settings.io.SettingsLoader;

public class Mabis {
	public static void main(String[] args) {

		// changing look
		try {
			UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// creating tmp dir
		File f = new File(GlobalPaths.TMP.toString());
		if (!f.exists()) {
			f.mkdir();
		}

		// loading last run
		try {
			LastRunDescription lrd = SettingsLoader.loadLastRun();
			if (lrd.getLastRunId() == 0) {
				FirstRunWizard frw = new FirstRunWizard();
				frw.addPropertyChangeListener(new GUILoader());
				frw.setVisible(true);
			} else {
				GUILoader loader = new GUILoader();
				loader.loadGUI();
			}
		} catch (SettingsException e1) {
			e1.printStackTrace();
			GUILoader loader = new GUILoader();
			loader.loadGUI();
		}

	}
}
