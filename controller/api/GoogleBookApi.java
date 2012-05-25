package controller.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;

import logger.MabisLogger;
import model.entity.Author;
import model.entity.Book;
import model.entity.Genre;
import model.entity.Picture;
import model.enums.BookIndustryIdentifier;
import model.enums.ImageType;

import com.google.api.client.googleapis.services.GoogleKeyInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.Books.Volumes.List;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volume.VolumeInfo;
import com.google.api.services.books.model.Volume.VolumeInfo.ImageLinks;
import com.google.api.services.books.model.Volume.VolumeInfo.IndustryIdentifiers;
import com.google.api.services.books.model.Volumes;

public class GoogleBookApi extends ApiAccess {
	private static final String API_ACCESS_KEY = "AIzaSyDZ7cVkpBjtmHtjKAhsvXOlWfetZQi4Ubo";

	public GoogleBookApi() {
		super();
	}

	@Override
	public void query(TreeMap<String, String> query) throws IOException {
		final Books books = Books
				.builder(new NetHttpTransport(), new JacksonFactory())
				.setApplicationName("Mabis")
				.setJsonHttpRequestInitializer(
						new GoogleKeyInitializer(API_ACCESS_KEY)).build();

		String fullQuery = new String();
		fullQuery += query;

		List volumesList = books.volumes().list(fullQuery);

		// volumesList.setPrintType("books");
		// volumesList.setPrettyPrint(true);
		// volumesList.setProjection("full");

		// Execute the query.
		Volumes volumes = volumesList.execute();
		if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
			return;
		}

		
		ArrayList<Volume> foundBooks = (ArrayList<Volume>) volumes.getItems();

		this.pcs.firePropertyChange("taskStarted",
									0,
									foundBooks.size());
		
		for (Volume volume : foundBooks) {
			VolumeInfo vi = volume.getVolumeInfo();
			if (vi.getTitle() != null && !vi.getTitle().isEmpty()) {
				this.result.add(parseVolume(vi));
				this.pcs.firePropertyChange("taskStep",this.result.size()-1,this.result.size());
			}
			MabisLogger.getLogger().log(Level.FINE,
					"Loaded {0} books from GoogleBook API", this.result.size());
		}
	}

	/**
	 * @param vi
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private Book parseVolume(VolumeInfo vi) throws MalformedURLException {
		Book book;
		book = new Book(vi.getTitle());
		if (vi.getSubtitle() != null) {
			book.setSubTitle(vi.getSubtitle());
		}

		// adding identifiers
		ArrayList<IndustryIdentifiers> ii = (ArrayList<IndustryIdentifiers>) vi.getIndustryIdentifiers();
		if (ii != null && !ii.isEmpty()) {
			for (IndustryIdentifiers identifier : ii) {
				book.addIdentifier(BookIndustryIdentifier
						.findType(identifier.getType()), identifier
						.getIdentifier());
			}
		}

		// authors
		java.util.List<String> authors = vi.getAuthors();
		if (authors != null && !authors.isEmpty()) {
			for (int i = 0; i < authors.size(); ++i) {
				String names[] = authors.get(i).split(" ");
				String firstName = names[0];
				String lastName = new String();
				for (int j = 1; j < names.length; j++) {
					lastName += names[j];
					if (j < names.length - 1) {
						lastName += " ";
					}
				}
				try {
					Author tmp = new Author(firstName, lastName);
					tmp.setPicture(new Picture(GoogleImageSearch.queryForImage(authors.get(i)),ImageType.AUTHOR));
					book.addAuthor(tmp);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			book.addAuthor(new Author());
		}

		// Description (if any).
		if (vi.getDescription() != null
				&& vi.getDescription().length() > 0) {
			book.setDescription(vi.getDescription());
		}

		// Ratings (if any).
		if (vi.getAverageRating() != null) {
			book.setRating(vi.getAverageRating());
		}

		// cover
		ImageLinks il = vi.getImageLinks();
		if (il != null) {
			String image = null;
			if (il.getLarge() != null) {
				image = il.getLarge();
			}
			if (il.getMedium() != null && image == null) {
				image = il.getMedium();
			}
			if (il.getThumbnail() != null && image == null) {
				image = il.getThumbnail();
			}
			try {
				book.setCover(new Picture(new URL(image), ImageType.FRONT_COVER));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// genres
		if (vi.getCategories() != null && !vi.getCategories().isEmpty()) {
			for (String genre : vi.getCategories()) {
				book.addGenre(new Genre(genre));
			}
		}

		// pages
		if (vi.getPageCount() != null) {
			book.setPages(vi.getPageCount());
		} else {
			book.setPages(0);
		}
		return book;
	}

}
