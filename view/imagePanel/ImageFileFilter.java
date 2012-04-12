/**
 * 
 */
package view.imagePanel;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

import view.enums.ImageFormats;

/**
 * This class provides customized file filter that allows to choose only image
 * based file
 * 
 * @author kornicameister
 * @see ImageFormats
 */
public class ImageFileFilter extends FileFilter {
	public ImageFileFilter() {
		super();
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		ArrayList<String> availableExt = ImageFormats.getFormats();
		for (String e : availableExt) {
			if (e.equals(ext)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getDescription() {
		ArrayList<String> availableExt = ImageFormats.getFormats();
		String str = new String();
		for (String a : availableExt) {
			str += a + ",";
		}
		str = str.substring(0, str.lastIndexOf(","));
		return str;
	}

}
