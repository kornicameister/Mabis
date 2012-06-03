package mvc.view.items.itemsprieview;

import javax.swing.JPanel;

import mvc.model.BaseTable;
import mvc.model.entity.Movie;
import mvc.model.enums.TableType;

public class PreviewDispatcher {

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
