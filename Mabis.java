import java.awt.Dimension;

import view.mainwindow.MainWindow;

public class Mabis {
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow mw = new MainWindow("MABIS", new Dimension(1000,
						1000));
				mw.setVisible(true);
			}
		});
	}

}
