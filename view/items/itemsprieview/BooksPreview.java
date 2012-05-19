package view.items.itemsprieview;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URL;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
		JPanel p = null;
		for (BaseTable bs : this.elements) {
			p = new BookPreviewChunk((Book) bs, this.getSize());
			p.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createEtchedBorder(), bs.getTitle()));
			this.tabbedPanel.addTab(((Book) bs).getTitle(), p);
		}
	}

	class BookPreviewChunk extends JPanel {
		private static final long serialVersionUID = 1L;

		public BookPreviewChunk(Book book, Dimension dimension) {
			super(true);
			// tworzymy panel ze zdjęciem
			ImagePanel panel = new ImagePanel(book.getCover().getImageFile());

			JEditorPane description = null;
			try {
				URL url = book.toDescription();
				if (url != null) {
					description = new JEditorPane(url);
				} else {
					description = new JEditorPane();
					description.setText("Description unavailable");
				}
				description.setEditable(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.setLayout(new GridLayout(1, 2));
			this.add(panel);
			if (description != null) {
				JScrollPane pane = new JScrollPane(description);
				this.add(pane);
			}
		}
	}
}
