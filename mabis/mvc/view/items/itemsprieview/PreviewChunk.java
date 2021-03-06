package mvc.view.items.itemsprieview;

import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import mvc.controller.HTMLDescriptor;
import mvc.model.entity.AudioAlbum;
import mvc.model.entity.Book;
import mvc.model.entity.Movie;

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
	 * ponieważ w module <b>mabis.mvc.model</b> {@link Movie} jest samodzielną klasą i
	 * jednocześnie bazą dla {@link AudioAlbum} oraz {@link Book}
	 * 
	 * @param entity
	 */
	public PreviewChunk(final Movie entity) {
		super(true);
		this.previedItem = entity;

		JEditorPane description = null;
		try {
			URL url = HTMLDescriptor.toHTML(entity);
			if (url != null) {
				description = new JEditorPane(url);
			} else {
				description = new JEditorPane();
				description.setText("Description unavailable");
			}
			description.setEditable(false);
			this.add(description);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}