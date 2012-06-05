package startPoint;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import mvc.view.mainwindow.MainWindow;
import settings.io.SettingsException;
import settings.io.SettingsLoader;
import settings.io.SettingsSaver;

public class GUILoader implements PropertyChangeListener {
	MainWindow mw = null;

	GUILoader() {
		mw = new MainWindow("Mabis");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("status")) {
			boolean approval = (boolean) evt.getNewValue();
			if (approval) {
				this.loadGUI();
			}
		}
	}

	void loadGUI() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SettingsLoader.loadFrame(mw);
				} catch (SettingsException e) {
					e.printStackTrace();
				}
				mw.setVisible(true);

				class ApplicationExiting extends WindowAdapter
						implements
							WindowListener {
					@Override
					public void windowClosed(WindowEvent e) {
						SettingsSaver.incrementRunCount();
						SettingsSaver.setRunStatus(true);
						SettingsSaver ss = new SettingsSaver();
						ss.execute();
					}
				}

				mw.addWindowListener(new ApplicationExiting());
			}
		});
	}

}
