package view.imagePanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.accessibility.Accessible;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import logger.MabisLogger;

/**
 * Class is to conveniently place an image inside of JPanel By Extending JPanel
 * user is given with ability of using standard JPanel functionality such as for
 * example creating borders
 * 
 * @author kornicameister
 * @see JPanel
 * @version 0.2
 */
public class ImagePanel extends JPanel implements Accessible {
	private static final long serialVersionUID = 8841755218083931060L;
	protected static final Integer padding = 10;
	private File imageFile = null;

	public ImagePanel(File f) {
		super(true);
		this.imageFile = f;
		this.addFocusListener(new ImagePanelFocusListener(this));
		this.addMouseMotionListener(new ImagePanelMouseMotionListener());
		try {
			this.setMinimumSize(new Dimension(new ImageIcon(f.getCanonicalPath()).getIconWidth(),
					new ImageIcon(f.getCanonicalPath()).getIconHeight()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
	}

	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
	}

	public void setImage(File f) {
		this.imageFile = f;
	}

	public ImageIcon getImage() {
		return new ImageIcon(this.imageFile.getName());
	}

	public File getImageFile() {
		return imageFile;
	}

	/**
	 * Method rescales internal image to adjust it to panel size <b>Notice</b>
	 * that image size in certain dimension will be always set to smaller in
	 * following comparision:
	 * <ul>
	 * <li>imageSize.width vs imagePanel.width -> smaller will be chosen</li>
	 * <li>imageSize.height vs imagePanel.height -> smaller will be chosen</li>
	 * </ul>
	 * 
	 * @param d
	 * @see ImagePanel#imageSize
	 */
	protected ImageIcon rescaleImage() {
		String filePath = this.imageFile.getAbsoluteFile().getAbsolutePath();
		ImageIcon f = new ImageIcon(filePath);

		int width = (this.getWidth() > f.getIconWidth() ? this.getWidth() : f.getIconWidth());
		int height = (this.getHeight() > f.getIconHeight() ? this.getHeight() : f.getIconHeight());
		
		return new ImageIcon(f.getImage().getScaledInstance(width, height, 0));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon i = rescaleImage();
		int topLeftX = padding / 2;
		int topLeftY = padding;
		g.drawImage(i.getImage(), topLeftX, topLeftY, i.getIconWidth()
				- padding, i.getIconHeight() - padding, null);
	}

	@Override
	public String toString() {
		return "ImagePanel [(" + this.getWidth() + "," + this.getHeight()
				+ ");(" + this.imageFile.getPath() + ")]";
	}

	class ImagePanelFocusListener implements FocusListener {
		private ImagePanel ref;

		public ImagePanelFocusListener(ImagePanel ref) {
			this.ref = ref;
		}

		@Override
		public void focusGained(FocusEvent e) {
			String path = null;
			try {
				path = ((ImagePanel) e.getComponent()).getImageFile()
						.getCanonicalPath();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			ref.firePropertyChange("focusable", false, true);
			MabisLogger.getLogger().log(Level.INFO,
					"{0} gained focus, tooltip visible", path);
			repaint();
		}

		@Override
		public void focusLost(FocusEvent e) {
			String path = null;
			try {
				path = ((ImagePanel) e.getComponent()).getImageFile()
						.getCanonicalPath();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			ref.firePropertyChange("focusable", true, false);
			MabisLogger.getLogger().log(Level.INFO, "{0} lost focus", path);
			repaint();
		}
	}

	public class ImagePanelMouseMotionListener extends MouseAdapter implements
			MouseMotionListener {

		@Override
		public void mouseMoved(MouseEvent e) {
			try {
				Thread.sleep(500);
				requestFocus(true);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

	}
}