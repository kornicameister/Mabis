package controller;

import model.entity.Picture;

/**
 * Wyjątek do obsługi błędów które mogę się pojawić podczas interakcji z cache
 * grafik
 * 
 * @author kornicameister
 * 
 */
public class PictureCacheException extends Exception {
	private static final long serialVersionUID = -5700553885599312048L;
	private Picture picture;

	public PictureCacheException(Picture p, String message) {
		super(message);
		this.picture = p;
	}

	@Override
	public String getMessage() {
		return this.picture.getImagePath() + "::" + super.getMessage();
	}

}
