package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import settings.io.SettingsSaver;

public class WindowClosedListener extends WindowAdapter implements WindowListener{
	@Override
	public void windowClosed(WindowEvent e) {
		super.windowClosed(e);
		SettingsSaver.addFrame((JFrame) e.getSource());
	}
}