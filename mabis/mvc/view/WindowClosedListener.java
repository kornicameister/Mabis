package mvc.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import settings.io.SettingsSaver;

/**
 * Prosty {@link WindowAdapter} który odwołuje się do statycznej metody
 * {@link SettingsSaver} zapisująć weń framkę. Jest to jednoznaczne z faktrem,
 * że konfiguracja okienka zostanie zapisana do pliku XML.
 * 
 * @author tomasz
 * @see WindowListener
 */
public class WindowClosedListener extends WindowAdapter implements
		WindowListener {
	@Override
	public void windowClosed(WindowEvent e) {
		super.windowClosed(e);
		SettingsSaver.addFrame((JFrame) e.getSource());
	}
}