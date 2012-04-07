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

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
		this.reloadMetaData();
	}

	@Override
	public void reloadMetaData() {
		this.metaData.clear();
		this.metaData.put("idAudioUser", this.getPrimaryKey().toString());
		this.metaData.put("idUser",
				this.getMultiForeing(this.getPrimaryKey()).f1.getValue()
						.toString());
		this.metaData.put("idAudio",
				this.getMultiForeing(this.getPrimaryKey()).f2.getValue()
						.toString());
	}
}
