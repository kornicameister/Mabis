/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import model.enums.TableType;

/**
 * This claps maps itself to mabis.audioUser many-to-maby table
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
