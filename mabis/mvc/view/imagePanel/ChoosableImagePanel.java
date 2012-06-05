/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.view.imagePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
 * This is the {@link ImagePanel} panel extended with the following feature:
 * </br> Every time user clicks on {@link ChoosableImagePanel}, than image panel
 * is redrawn and besides the image user can see filled rectangle
 * 
 * @author kornicameister
 */
public class ChoosableImagePanel extends ImagePanel {
	private static final long serialVersionUID = 5784477220725904046L;
	private Boolean marked;
	private final MouseListener listener;
	private Color defaultColor;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public ChoosableImagePanel(File icon, Dimension d) {
		super();
		this.marked = false;
		this.defaultColor = this.getForeground();
		this.listener = new ChoosableImagePanelMouseListener();
		this.addMouseListener(this.listener);
		ImageIcon tmp = new ImageIcon(icon.getAbsolutePath());
		ImageIcon tmp2 = new ImageIcon(tmp.getImage().getScaledInstance(
				(int) d.getWidth(), (int) d.getHeight()-30, Image.SCALE_FAST));
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
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener l) {
		if(this.pcs == null){
			pcs = new PropertyChangeSupport(this);
		}
		this.pcs.addPropertyChangeListener(l);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener l) {
		this.pcs.removePropertyChangeListener(l);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (this.marked) {
			g.setColor(Color.gray.brighter().brighter());
			g.fill3DRect(0, 0, this.getWidth(), this.getHeight() + padding,
					false);
		} else {
			g.setColor(this.defaultColor);
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
		}
	}

	/**
	 * Works pretty much the same as
	 * {@link ChoosableImagePanel#setMarked(false)} but without triggering
	 * property change event, additionaly it fires repainting of panel
	 */
	public void demark() {
		this.marked = false;
		this.repaint();
	}

	/**
	 * Works like {@link ChoosableImagePanel#setMarked(true)}, but does not
	 * trigger fire property change
	 */
	public void mark() {
		this.marked = true;
		this.repaint();
	}

	class ChoosableImagePanelMouseListener extends MouseAdapter implements
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
}