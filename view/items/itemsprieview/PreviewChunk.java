package view.items.itemsprieview;

import java.awt.GridLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.entity.AudioAlbum;
import model.entity.Book;
import model.entity.Movie;
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
	public PreviewChunk(Movie entity) {
		super(true);
		this.previedItem = entity;
		// tworzymy panel ze zdjęciem
		ImagePanel panel = new ImagePanel(entity.getCover().getImageFile());

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
		this.setLayout(new GridLayout(1, 2));
		this.add(panel);
		if (description != null) {
			JScrollPane pane = new JScrollPane(description);
			this.add(pane);
		}
	}
}