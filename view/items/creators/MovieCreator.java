package view.items.creators;

import java.awt.HeadlessException;

import model.BaseTable;
import view.items.CreatorContentNullPointerException;
import view.items.ItemCreator;

/**
 * 
 * @author kornicameister
 *
 */
public class MovieCreator extends ItemCreator {
	private static final long serialVersionUID = -8029181092669910824L;
	
	public MovieCreator(String title) throws HeadlessException,
			CreatorContentNullPointerException {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void clearContentFields() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Boolean createItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void fetchFromAPI(String query, String criteria) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void cancelAPISearch() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void fillWithResult(BaseTable table) {
		// TODO Auto-generated method stub

	}

}
