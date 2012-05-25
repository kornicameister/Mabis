package view.items.itemsprieview;

import javax.swing.JPanel;

import model.BaseTable;
import model.entity.Movie;
import model.enums.TableType;

public class PreviewArbiter {

	public static JPanel determineTyp(BaseTable tmp) throws Exception {
		if(tmp == null){
			throw new Exception("Null table !!!");
		}
		boolean result = tmp.getTableType().equals(TableType.BOOK);
		if (!result) {
			result = tmp.getTableType().equals(TableType.MOVIE);
		}
		if (!result) {
			result = tmp.getTableType().equals(TableType.AUDIO_ALBUM);
		}
		if (result) {
			return new PreviewChunk((Movie) tmp);
		} else {
			throw new Exception("Invalid table type provided");
		}
	}

}
