package controller.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import model.BaseTable;
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
	private final TreeSet<BaseTable> result;
	private static final String API_ACCESS_KEY = "AIzaSyDZ7cVkpBjtmHtjKAhsvXOlWfetZQi4Ubo";

	public GoogleBookApi() {
		super();
		this.result = new TreeSet<BaseTable>();
	}

	@Override
	protected ArrayList<URL> setApiAccessPointList()
			throws MalformedURLException {
		ArrayList<URL> tmp = new ArrayList<URL>();
		tmp.add(new URL("https://www.googleapis.com/books/v1/volumes?q="));
		return tmp;
	}

	/**
	 * @return the result
	 */
	public TreeSet<BaseTable> getResult() {
		return result;
	}

	@Override
	public void query(String query, TreeMap<String, String> params)
			throws IOException {
		final Books books = Books
				.builder(new NetHttpTransport(), new JacksonFactory())
				.setApplicationName("Mabis")
				.setJsonHttpRequestInitializer(
						new GoogleKeyInitializer(API_ACCESS_KEY)).build();

		String fullQuery = new String();
		fullQuery += params;
		fullQuery += query;

		List volumesList = books.volumes().list(fullQuery);

		volumesList.setPrintType("books");
		volumesList.setPrettyPrint(true);
		volumesList.setProjection("full");

		// Execute the query.
		Volumes volumes = volumesList.execute();
		if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
			return;
		}

		ArrayList<Volume> foundBooks = (ArrayList<Volume>) volumes.getItems();
		Book book = null;

		for (Volume volume : foundBooks) {
			VolumeInfo vi = volume.getVolumeInfo();
			if(vi.getTitle() == null){ //no empty title
				break;
			}

			// setting book title
			book = new Book(vi.getTitle());
			if (vi.getSubtitle() != null) {
				book.setSubTitle(vi.getSubtitle());
			}

			// adding identifiers
			ArrayList<IndustryIdentifiers> ii = (ArrayList<IndustryIdentifiers>) vi
					.getIndustryIdentifiers();
			if (ii != null && !ii.isEmpty()) {
				for (IndustryIdentifiers identifier : ii) {
					book.addIdentifier(BookIndustryIdentifier.findType(identifier
							.getType()), identifier.getIdentifier());
				}
			}

			// authors
			java.util.List<String> authors = vi.getAuthors();
			if (authors != null && !authors.isEmpty()) {
				for (int i = 0; i < authors.size(); ++i) {
					String names[] = authors.get(i).split(" ");
					String firstName = new String();
					String lastName = new String();
					for (int j = 0; j < names.length; j++) {
						if (j < names.length - 1) {
							firstName += names[j] + " ";
						} else {
							lastName += names[j];
						}
					}
					book.addAuthor(new Author(firstName, lastName));
					// TODO add searching for author's face !
				}
			}else{
				book.addAuthor(new Author());
			}

			// Description (if any).
			if (vi.getDescription() != null && vi.getDescription().length() > 0) {
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
				if (il.getLarge() != null){
					image = il.getLarge();
				}
				if (il.getMedium() != null && image == null){
					image = il.getMedium();
				}
				if (il.getThumbnail() != null && image == null) {
					image = il.getThumbnail();
				}
				book.setCover(new Picture(new URL(image),ImageType.FRONT_COVER));
			}
			
			//genre
			if(vi.getCategories() != null && !vi.getCategories().isEmpty()){
				book.setGenre(new Genre(vi.getCategories().get(0)));
			}else{
				book.setGenre(new Genre("null"));
			}

			// pages
			if(vi.getPageCount() != null){
				book.setPages(vi.getPageCount());
			}else{
				book.setPages(0);
			}

			// saving found book
			this.result.add(book);
		}
	}

}
