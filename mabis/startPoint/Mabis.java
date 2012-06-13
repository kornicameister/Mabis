package startPoint;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mvc.view.mainwindow.MainWindow;
import settings.GlobalPaths;
import settings.io.SettingsException;
import settings.io.SettingsLoader;
import settings.io.SettingsSaver;

/**
 * Glowna klasa programu, gdzie ustawiany jest styl okien oraz ładowane jest
 * glowne okienko aplikacji. Dodatkowym elementem jest zapis ustawien aplikacji,
 * w momencie kiedy zamkniete zostanie glowne okno
 * 
 * @author tomasz
 * 
 */
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

		final MainWindow mw = new MainWindow("Mabis");

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SettingsLoader.load(mw);
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
					
					@Override
					public void windowClosing(WindowEvent e) {
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
