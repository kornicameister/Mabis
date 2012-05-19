package view.items.itemsprieview;

import javax.swing.JPanel;

import model.BaseTable;
import model.entity.Movie;
import model.enums.TableType;

public class PreviewArbiter {

	public static JPanel determineTyp(BaseTable tmp) throws Exception {
		boolean result = tmp.getTableType().equals(TableType.BOOK.toString());
		if (!result) {
			result = tmp.getTableType().equals(TableType.MOVIE.toString());
		}
		if (!result) {
			result = tmp.getTableType().equals(TableType.AUDIO_ALBUM.toString());
		}
		if (result) {
			return new PreviewChunk((Movie) tmp);
		} else {
			throw new Exception("Invalid table type provided");
			// TODO add custom exception
		}
	}

}
