import java.awt.Dimension;

import view.mainwindow.MainWindow;

public class Mabis {
	public static void main(String[] args) {
		// database.MySQLAccess d = new MySQLAccess();
		// d.getConnectionData().setHost("localhost");
		// d.getConnectionData().setLogin("kornicameister");
		// d.getConnectionData().setPassword("tabaka");
		// if (d.connect()) {
		// System.out.println("Good, i am in");
		// }
		// d.disconnect();
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow mw = new MainWindow("MABIS",
						new Dimension(1000, 1000));
				mw.setVisible(true);
			}
		});
	}

}
