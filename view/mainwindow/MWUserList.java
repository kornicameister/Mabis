/**
 * 
 */
package view.mainwindow;

import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

/**
 * @author kornicameister
 * 
 */
public class MWUserList extends JPanel {
	private static final long serialVersionUID = -4998076655395481258L;
	private JScrollPane userList;

	/**
	 * 
	 */
	public MWUserList() {
		super();
		this.initComponents();
	}

	/**
	 * @param layout
	 */
	public MWUserList(LayoutManager layout) {
		super(layout);
		this.initComponents();
	}

	/**
	 * @param isDoubleBuffered
	 */
	public MWUserList(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		this.initComponents();
	}

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public MWUserList(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.initComponents();
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Users"));
		
		this.userList = new JScrollPane(this);
		this.userList.setWheelScrollingEnabled(true);

		// TODO add action to view menu
		JPopupMenu userMenu = new JPopupMenu("Friend popup");
		userMenu.add(new JMenuItem("Chat"));
		userMenu.add(new JSeparator(JSeparator.HORIZONTAL));
		userMenu.add(new JMenuItem("List collection"));
		userMenu.add(new JSeparator(JSeparator.HORIZONTAL));
		userMenu.add(new JMenuItem("Make friend"));
		userMenu.add(new JMenuItem("Block"));
		userMenu.setSelected(this);

		// creating table view for user list
		JTable userTable = new JTable(20, 3);

		// setting up to hierarchy
		this.add(userTable);
		this.setComponentPopupMenu(userMenu);
	}
}