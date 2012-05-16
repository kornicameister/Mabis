package view.items.itemsprieview;

import java.awt.Dimension;
import java.util.TreeSet;

import javax.swing.ImageIcon;

import model.BaseTable;
import model.entity.Book;
import view.imagePanel.ImagePanel;

public class BooksPreview extends ItemsPreview {
	private static final long serialVersionUID = -1227730593758451025L;

	public BooksPreview(String title, TreeSet<BaseTable> elements) {
		super(title, elements);
	}

	@Override
	protected void initComponents() {
		super.initComponents();
		Book book = null;
		ImageIcon icon = null;
		ImagePanel panel = null;
		for(BaseTable bs : this.elements){
			book = (Book) bs;
			icon = new ImageIcon(book.getCover().getImagePath());
			panel = new ImagePanel(book.getCover().getImageFile());
			panel.setPreferredSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()+30));
			this.contentPanel.add(panel);
		}
	}
}
