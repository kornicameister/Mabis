/**
 * package mabis.mvc.model.entity in MABIS
 * by kornicameister
 */
package mvc.model.entity;

import java.io.Serializable;

import mvc.model.ManyToManyTable;
import mvc.model.enums.TableType;

/**
 * This class maps itself to mabis.movieUser many-to-many table
 * 
 * @author kornicameister
 * 
 */
public class MovieUser extends ManyToManyTable implements Serializable {
	private static final long serialVersionUID = 8097744132978112122L;

	public MovieUser() {
		super();
	}

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
		this.tableType = TableType.MOVIE_USER;
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idMovieUser", "idMovie", "idUser" };
		return tmp;
	}
}
