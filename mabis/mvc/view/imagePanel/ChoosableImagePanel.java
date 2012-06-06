/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.view.imagePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import logger.MabisLogger;

/**
 * 
 * Klasa rozszerza mozliwosci {@link ImagePanel} o mozliwosci sygnalizowania, ze
 * dany {@link ImagePanel} zostal zaznaczony przez uzytkownika, badz odznaczony
 * 
 * @author kornicameister
 */
public class ChoosableImagePanel extends ImagePanel {
	private static final long serialVersionUID = 5784477220725904046L;
	private Boolean marked;
	private final MouseListener listener;
	private Color defaultColor;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * Konstruuje nowy {@link ChoosableImagePanel} z ikoną (<b>icon</b>) oraz
	 * rozmiarem (<b>d</b>).
	 * 
	 * @param icon
	 *            ikona do ustawienia wewnątrz {@link ChoosableImagePanel}
	 * @param d
	 *            rozmiar ikony
	 */
	public ChoosableImagePanel(File icon, Dimension d) {
		super();
		this.marked = false;
		this.defaultColor = this.getForeground();
		this.listener = new ChoosableImagePanelMouseListener();
		this.addMouseListener(this.listener);
		ImageIcon tmp = new ImageIcon(icon.getAbsolutePath());
		ImageIcon tmp2 = new ImageIcon(tmp.getImage().getScaledInstance(
				(int) d.getWidth(), (int) d.getHeight() - 30, Image.SCALE_FAST));
		this.contentLabel.setIcon(tmp2);
		this.imageFile = icon;
	}

	/**
	 * The status of imagePanel, if panel detected clicked within it's area than
	 * {@link ChoosableImagePanel#marked} value is true, otherwise is set to
	 * false
	 * 
	 * @return the marked
	 */
	public Boolean getMarked() {
		return marked;
	}

	/**
	 * Calling this method causes PropertyChangeSupport to fire property change
	 * event that can be caught in adjusted class
	 * 
	 * @param marked
	 *            the marked to set
	 */
	public void setMarked(Boolean marked) {
		Boolean oldMarked = this.marked;
		this.marked = marked;
		this.pcs.firePropertyChange("panelMarked", oldMarked, this.marked);
		if (this.marked) {
			this.setBackground(Color.gray.brighter());
		} else {
			this.setBackground(this.defaultColor);
		}
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener l) {
		if (this.pcs == null) {
			pcs = new PropertyChangeSupport(this);
		}
		this.pcs.addPropertyChangeListener(l);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener l) {
		this.pcs.removePropertyChangeListener(l);
	}

	class ChoosableImagePanelMouseListener extends MouseAdapter
			implements
				MouseListener {

		@Override
		public void mouseClicked(MouseEvent o) {
			Point p = o.getLocationOnScreen();
			Point pp = getLocationOnScreen();
			boolean inArea = p.getX() > pp.getX()
					& p.getX() < (pp.getX() + getWidth());
			inArea = inArea
					& (p.getY() > pp.getY() & p.getY() < (pp.getY() + getHeight()));
			if (inArea) {
				setMarked(!marked);
				MabisLogger.getLogger().log(Level.INFO,
						"ChoosableImagePanel click detected, status: {0}",
						(marked ? "marked" : "unmarked"));
			}
			repaint();
		}
	}

	/**
	 * Metoda odznacza ten panel, ale nie generuje przy tym zdarzenia firePropertyChange
	 */
	public void demark() {
		this.marked = false;
		this.setBackground(this.defaultColor);
		this.revalidate();
	}

	/**
	 * Metoda zaznacza ten panel, ale nie generuje przy tym zdarzenia firePropertyChange
	 */
	public void mark() {
		this.marked = true;
		this.setBackground(Color.gray.brighter());
		this.revalidate();
	}
}
