/**
 * 
 */
package mvc.view.mainwindow;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import logger.MabisLogger;
import mvc.model.entity.User;
import mvc.view.items.creators.AudioAlbumCreator;
import mvc.view.items.creators.BookCreator;
import mvc.view.items.creators.MovieCreator;

/**
 * Klase {@link MWItemButtons} dziedziczy z JPanel i jest agregatem gdzie
 * zlokalizowane są przyciski do interakcji z kolekcją
 * 
 * @author kornicameister
 * 
 */
public class MWItemButtons extends JPanel implements PropertyChangeListener, ActionListener {
	private static final long serialVersionUID = -4998076655395481258L;
	private JButton newBookButton;
	private JButton newMovieButton;
	private JButton newAudioAlbumButton;
	private User connectedUser;

	public MWItemButtons() {
		super(true);
		this.setLayout(new GridLayout(0, 1, 10, 10));
		this.initComponents();
	}
	
	public void setConnectedUser(User u){
		this.connectedUser = u;
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Item fun ;-)"));
		this.newBookButton = new JButton("New book");
		this.newBookButton.addActionListener(this);
		this.newMovieButton = new JButton("New movie");
		this.newMovieButton.addActionListener(this);
		this.newAudioAlbumButton = new JButton("New audio");
		this.newAudioAlbumButton.addActionListener(this);

		// layout
		this.add(this.newAudioAlbumButton);
		this.add(this.newBookButton);
		this.add(this.newMovieButton);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("itemAffected")){
			this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source.equals(newBookButton)) {
			BookCreator bc = new BookCreator(connectedUser ,"New book");
			bc.addPropertyChangeListener(this);
			bc.setVisible(true);
		} else if (source.equals(newAudioAlbumButton)) {
			AudioAlbumCreator aac = new AudioAlbumCreator(connectedUser,"New audio album");
			aac.addPropertyChangeListener(this);
			aac.setVisible(true);
		} else if (source.equals(newMovieButton)) {
			MovieCreator mc = new MovieCreator(connectedUser,"New movie");
			mc.addPropertyChangeListener(this);
			mc.setVisible(true);
		}
		MabisLogger.getLogger().log(Level.INFO, "Action called ::  {0}", source.getActionCommand());
	}
	
}
