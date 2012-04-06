/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.util.ArrayList;

/**
 * @author kornicameister
 * 
 */
public abstract class Entity {
	private Integer id = null;
	private String titles[] = null;
	private ArrayList<Cover> covers = null;
	private Integer author = null;

	/**
	 * Empty constructor, only initializes variables, used when new entity
	 * object was created and which is at the moment not in the database
	 */
	public Entity() {
		this.id = new Integer(-1);
		this.titles = new String[2];
		this.covers = new ArrayList<Cover>();
		this.author = new Integer(-1);
	}

	/**
	 * Constructs an Entity with and id
	 * 
	 * @param pk
	 *            if Entity is new object that is about to be inserted into DB,
	 *            pk (<b>primary key</b>) should be set to < 0 value otherwise
	 *            id represents actual identifier of row
	 */
	Entity(int pk) {
		this.id = new Integer(id);
		this.titles = new String[2];
		this.covers = new ArrayList<Cover>();
		this.author = new Integer(-1);
	}

	/**
	 * 
	 * @param pk
	 *            if Entity is new object that is about to be inserted into DB,
	 *            pk (<b>primary key</b>) should be set to < 0 value otherwise
	 *            id represents actual identifier of row
	 * @param authorFK
	 *            represents author's primary key in context of foreign key for
	 *            Entity.</br> This key context can be different depends on
	 *            which extending class object was created. In case of:
	 *            <ul>
	 *            <li> {@link AudioAlbum} - this will refer to band table primary
	 *            key</li>
	 *            <li> {@link Book} - this will refer to writer table primary key
	 *            </li>
	 *            <li> {@link Movie} - this will refer to director table primary
	 *            key</li>
	 *            </ul>
	 */
	Entity(int pk, int authorFK) {
		this.id = new Integer(id);
		this.author = new Integer(authorFK);
		this.covers = new ArrayList<Cover>();
	}

}
