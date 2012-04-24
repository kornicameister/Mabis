/**
 * 
 */
package controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.AudioAlbum;
import controller.SQLFactory;
import controller.SQLStamentType;

/**
 * @author kornicameister
 * 
 */
public class AudioAlbumSQLFactory extends SQLFactory {
	TreeSet<AudioAlbum> audioAlbums = new TreeSet<AudioAlbum>();

	public AudioAlbumSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		this.handleGenres();
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		// TODO Auto-generated method stub
	}

	/**
	 * Metoda łączy się z tabelą mabis.genre i sprawdza czy gatunki
	 * odpowiadające klasie AudioAlbum są już przechowywane w bazie danych.
	 * Jeśli dany gatunek jest już w bazie pobierany jest jedynie jego
	 * identyfikator, w innym wypadku gatunek jest umieszczany w bazie danych, a
	 * następnie pobierany jest jego identyfikator
	 */
	private final void handleGenres() {
		return;
	}

	public TreeSet<AudioAlbum> getValues() {
		return this.audioAlbums;
	}

}
