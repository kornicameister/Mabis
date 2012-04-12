package view.imagePanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * Class is to conveniently place an image inside of JPanel By Extending JPanel
 * user is given with ability of using standard JPanel functionality such as for
 * example creating borders
 * 
 * @author kornicameister
 * @see JPanel
 */
public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 8841755218083931060L;
	private Image img = null;
	private String imgPath;

	public ImagePanel(String pathToImage) {
		this.imgPath = pathToImage;
		this.loadImage();
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	}

	public String getImagePath() {
		return this.imgPath;
	}

	public Image getImage() {
		return this.img;
	}

	/**
	 * Method sets new image path and reloads an image being displayed within
	 * JPanel by calling loadImage and repaint method
	 * 
	 * @param newImg
	 */
	public void swapImage(String newImg) {
		this.imgPath = newImg;
		this.loadImage();
	}

	private void loadImage() {
		try {
			img = ImageIO.read(new File(this.imgPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.gray);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}