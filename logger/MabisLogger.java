/**
 * 
 */
package logger;

import java.io.IOException;
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
	private static final Logger logger = Logger.getLogger("Mabis");

	public MabisLogger() {
		// TODO support for saving logs into xml file, not supported so far
		FileHandler h1;
		try {
			h1 = new FileHandler("mabisLog.xml");
			h1.setLevel(Level.ALL);
			h1.setFormatter(new XMLFormatter());
			logger.addHandler(h1);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

}
