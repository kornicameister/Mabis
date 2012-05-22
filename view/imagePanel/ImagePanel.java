package view.imagePanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.accessibility.Accessible;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
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
public class ImagePanel extends JPanel implements Accessible {
	private static final long serialVersionUID = 8841755218083931060L;
	protected static final Integer padding = 10;
	protected File imageFile;
	protected JLabel contentLabel;

	public ImagePanel(File f) {
		super(true);
		this.imageFile = f;
		this.addMouseMotionListener(new ImagePanelMouseMotionListener());

		this.contentLabel = new JLabel();
		this.contentLabel.setSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		this.contentLabel.setLayout(new FlowLayout());
		this.contentLabel.setIcon(new ImageIcon(f.getAbsolutePath()));
		this.add(contentLabel);
	}

	public ImagePanel() {
		super(true);
		this.addMouseMotionListener(new ImagePanelMouseMotionListener());

		this.contentLabel = new JLabel();
		this.contentLabel.setSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		this.contentLabel.setLayout(new FlowLayout());
		this.add(contentLabel);
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
		ImageIcon tmp = new ImageIcon(f.getAbsolutePath());
		if (tmp.getIconHeight() > this.getHeight()
				&& tmp.getIconWidth() > this.getWidth()) {
			tmp = new ImageIcon(tmp.getImage().getScaledInstance(
					this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
		}
		this.contentLabel.setIcon(tmp);
	}

	public ImageIcon getImage() {
		return new ImageIcon(this.imageFile.getName());
	}

	public File getImageFile() {
		return imageFile;
	}

	@Override
	public String toString() {
		return "ImagePanel [(" + this.getWidth() + "," + this.getHeight()
				+ ");(" + this.imageFile.getPath() + ")]";
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