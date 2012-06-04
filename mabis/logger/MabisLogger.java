/**
 * 
 */
package logger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

/**
 * Mabis wrapper for Logger functionality.</b> This particular wrapper uses
 * remote HDD located xml file to save Mabis logs
 * 
 * @author kornicameister
 */
public class MabisLogger {
	private static Logger logger = initLogger();

	private static Logger initLogger() {
		logger = Logger.getLogger("Mabis");
		FileHandler h1;
		try {
			h1 = new FileHandler("./mabisLog.xml", true);
			h1.setLevel(Level.ALL);
			h1.setFormatter(new XMLFormatter());
			logger.addHandler(h1);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		logger.log(Level.WARNING, "Log started at {0}",
				dateFormat.format(new Date()));
		return logger;
	}

	/**
	 * @return the mabis.logger
	 */
	public static Logger getLogger() {
		return logger;
	}

}
