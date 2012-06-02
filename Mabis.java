import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import settings.GlobalPaths;
import settings.io.SettingsException;
import settings.io.SettingsLoader;
import settings.io.SettingsSaver;
import view.mainwindow.MainWindow;

public class Mabis {
	public static void main(String[] args) {
		
		//changing look
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		//creating tmp dir
		File f = new File(GlobalPaths.TMP.toString());
		if (!f.exists()) {
			f.mkdir();
		}

		//opening main window
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				final MainWindow mw = new MainWindow("Mabis");
				try {
					SettingsLoader.loadFrame(mw);
				} catch (SettingsException e) {
					e.printStackTrace();
				}
				mw.setVisible(true);
				
				class ApplicationExiting extends WindowAdapter implements WindowListener{
					@Override
					public void windowClosed(WindowEvent e) {
						SettingsSaver ss = new SettingsSaver();
						ss.execute();
					}
				}
				
				mw.addWindowListener(new ApplicationExiting());
			}
		});
	}

}
