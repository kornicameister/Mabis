import java.io.File;

import settings.GlobalPaths;
import view.items.BookCreator;

public class Mabis {
	public static void main(String[] args) {

		// deleting cache, right before application starts
		File tmp = new File(GlobalPaths.TMP.toString());
		File[] files = tmp.listFiles();
		for (File f : files) {
			f.delete();
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// MainWindow mw = new MainWindow("MABIS",
				// new Dimension(1000, 1000));
				// mw.setVisible(true);
				BookCreator mw = new BookCreator("New book");
				mw.setVisible(true);
			}
		});
	}

}
