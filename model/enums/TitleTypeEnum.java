/**
 * package database in MABIS
 * by kornicameister
 */
package model.enums;

import model.entity.Entity;


/**
 * Enum describes the title type from {@link Entity} class
 * 
 * @author kornicameister
 *
 */
public enum TitleTypeEnum {
	TITLE_ORIGINAL (0),
	TITLE_LOCALE (1),
	TITLE_INVALID (2);
	
	private int intValue;
	
	TitleTypeEnum(int value){
		this.intValue = value;
	}
	
	public int value() {
		return intValue;
	}
}
