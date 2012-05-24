package view.items.itemsprieview;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import model.entity.AudioAlbum;
import model.entity.Author;
import model.entity.Book;
import model.entity.Movie;
import settings.GlobalPaths;
import view.imagePanel.ImagePanel;

/**
 * Prosta klasa która wyświetlana jest w {@link ItemsPreview}. Pozwala ona,
 * niezależnie od typu na przygotowanie panelu z okładką danego obiektu kolekcji
 * oraz jego krótkim opisem;
 * 
 * @author kornicameister
 * 
 */
public class PreviewChunk extends JPanel {
	private static final long serialVersionUID = 1L;
	final Movie previedItem;

	/**
	 * PreviewChunk jako parametr przyjmuje refrencje do obiektu typu Movie
	 * ponieważ w module <b>model</b> {@link Movie} jest samodzielną klasą i
	 * jednocześnie bazą dla {@link AudioAlbum} oraz {@link Book}
	 * 
	 * @param entity
	 */
	public PreviewChunk(final Movie entity) {
		super(true);
		this.previedItem = entity;
		final ImagePanel panel = new ImagePanel();

		JEditorPane description = null;
		try {
			URL url = entity.toDescription();
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
		this.setLayout(new GridLayout(1, 3));
		this.add(panel);
		
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (entity.getCover() == null) {
					panel.setImage(new File(GlobalPaths.DEFAULT_COVER_PATH
							.toString()));
				} else {
					panel.setImage(entity.getCover().getImageFile());
				}
			}
		});

		if (description != null) {
			JScrollPane pane = new JScrollPane(description);
			this.add(pane);
		}
		JTabbedPane authors = new JTabbedPane(JTabbedPane.BOTTOM);
		authors.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		for (Author a : entity.getAuthors()) {
			ImagePanel p = new ImagePanel(a.getPictureFile().getImageFile());
			authors.addTab(a.getLastName(), p);
		}
		this.add(authors);
	}
}