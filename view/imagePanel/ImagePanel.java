package view.imagePanel;

import java.awt.Dimension;
import java.awt.Graphics;

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
	private static final Integer padding = 10;
	private ImageIcon img = null, scaledImage = null;
	private String imgPath;

	public ImagePanel(ImageIcon img, String path) {
		super(true);
		this.img = img;
		this.imgPath = (path == null ? "" : path);
	}

	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
	}

	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
	}

	public void setImg(ImageIcon img, String path) {
		this.img = null;
		this.img = img;
		this.imgPath = (path == null ? "" : path);
	}

	public ImageIcon getImage() {
		return img;
	}

	public String getImagePath() {
		return imgPath;
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
	private void rescaleImage() {
		this.scaledImage = new ImageIcon(this.img.getImage().getScaledInstance(
				this.getWidth() - padding, this.getHeight() - padding,
				0));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		rescaleImage();
		int topLeftX = getWidth() / 10;
		int topLeftY = getWidth() / 7;
		g.drawImage(scaledImage.getImage(), topLeftX, topLeftY, scaledImage.getIconWidth()-padding,
				scaledImage.getIconHeight()-padding, null);
	}

	@Override
	public String toString() {
		return "ImagePanel [(" + this.getWidth() + "," + this.getHeight()
				+ ")\n(" + this.imgPath + ")\n(" + this.img.getIconWidth()
				+ "," + this.img.getIconHeight() + ")]";
	}
}