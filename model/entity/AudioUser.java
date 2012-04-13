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
	}

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
		this.tableName = TableType.AUDIO_USER.toString();
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idAudioUser", "idUser", "idAudio" };
		return tmp;
	}
}
