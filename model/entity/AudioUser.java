/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import model.enums.TableType;

/**
 * @author kornicameister
 * 
 */
public class AudioUser extends ManyToManyTable {

	public AudioUser() {
		super();
		this.tableName = TableType.AUDIO_USER.toString();
	}
}
