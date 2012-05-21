import java.awt.Dimension;
import java.io.File;

import settings.GlobalPaths;
import view.mainwindow.MainWindow;

public class Mabis {
	public static void main(String[] args) {
		File f = new File(GlobalPaths.TMP.toString());
		if(!f.exists()){
			f.mkdir();
		}
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
