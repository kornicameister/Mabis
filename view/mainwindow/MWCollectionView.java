/**
 * 
 */
package view.mainwindow;

import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;

/**
 * @author kornicameister
 *
 */
public class MWCollectionView extends JPanel {
	private static final long serialVersionUID = 4037649477948033295L;
	private JPopupMenu collectionMenu;

	/**
	 * 
	 */
	public MWCollectionView() {
		super();
		this.initComponents();
	}

	/**
	 * @param layout
	 */
	public MWCollectionView(LayoutManager layout) {
		super(layout);
		this.initComponents();
	}

	/**
	 * @param isDoubleBuffered
	 */
	public MWCollectionView(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		this.initComponents();
	}

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public MWCollectionView(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.initComponents();
	}

	private void initComponents(){
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Collection"));
		initPopupMenu();
	}
	
	private void initPopupMenu(){
		// TODO add listener
		this.collectionMenu = new JPopupMenu("Collection popup menu");
		collectionMenu.add(new JMenuItem("Edit"));
		collectionMenu.add(new JMenuItem("Remove"));
		collectionMenu.add(new JMenuItem("Publish/Unpublish"));
		collectionMenu.setSelected(this);
	}
}
