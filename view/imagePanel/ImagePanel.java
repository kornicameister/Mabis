package view.imagePanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Class is to conveniently place an image inside of JPanel By Extending JPanel
 * user is given with ability of using standard JPanel functionality such as for
 * example creating borders
 * 
 * @author kornicameister
 * @see JPanel
 * @version 0.2
 */
public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 8841755218083931060L;
	protected static final Integer padding = 10;
	private File imageFile = null;

	public ImagePanel(File f) {
		super(true);
		this.imageFile = f;
	}

	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
	}

	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
	}

	public void setImg(File f) {
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

		return new ImageIcon(f.getImage().getScaledInstance(
				this.getWidth() - padding, this.getHeight() - padding, 0));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon i = rescaleImage();
		int topLeftX = getWidth() / 10;
		int topLeftY = getWidth() / 7;
		g.drawImage(i.getImage(), topLeftX, topLeftY, i.getIconWidth()
				- padding, i.getIconHeight() - padding, null);
	}

	@Override
	public String toString() {
		return "ImagePanel [(" + this.getWidth() + "," + this.getHeight()
				+ ")\n(" + this.imageFile.getPath() + ")\n]";
	}
}