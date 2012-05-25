package view.items.creators;

import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.BaseTable;
import model.entity.Author;
import model.entity.Movie;
import settings.GlobalPaths;
import view.imagePanel.ImagePanel;
import view.items.CreatorContentNullPointerException;
import view.items.ItemCreator;
import view.items.minipanels.AuthorMiniPanel;
import view.items.minipanels.TagCloudMiniPanel;
import controller.SQLStamentType;
import controller.entity.AuthorSQLFactory;

/**
 * 
 * Klasa która definiuje strukturę okienka zapewniającego miejsce do
 * definiowanie nowego filmu.
 * 
 * @author kornicameister
 * 
 */
public class MovieCreator extends ItemCreator {
	private static final long serialVersionUID = -8029181092669910824L;
	private JTextField titleField;
	private ImagePanel coverPanel;
	private TagCloudMiniPanel tagCloud;
	private AuthorMiniPanel directorsPanel;
	private JFormattedTextField durationField;
	private Movie selectedMovie;

	public MovieCreator(String title) throws HeadlessException,
			CreatorContentNullPointerException {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void layoutComponents() {
		super.layoutComponents();
		GroupLayout gl = new GroupLayout(this.contentPanel);
		this.contentPanel.setLayout(gl);

		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				coverPanel.setImage(new File(GlobalPaths.DEFAULT_COVER_PATH
						.toString()));
			}
		});
		this.revalidate();
		this.pack();
	}

	@Override
	protected void initComponents() throws CreatorContentNullPointerException {
		super.initComponents();
		this.contentPanel = new JPanel(true);
		this.titleField = new JTextField();
		this.titleField.setBorder(BorderFactory.createTitledBorder("Title"));
		this.tagCloud = new TagCloudMiniPanel();
		this.tagCloud.setBorder(BorderFactory.createTitledBorder("Tag cloud"));
		this.durationField = new JFormattedTextField(new SimpleDateFormat("hh:mm"));
		this.durationField.setBorder(BorderFactory.createTitledBorder("Duration"));
		this.coverPanel = new ImagePanel();
		try {
			AuthorSQLFactory asf = new AuthorSQLFactory(SQLStamentType.SELECT,
					new Author());
			asf.executeSQL(true);
			TreeSet<Author> bridge = new TreeSet<>();
			for (Author a : asf.getAuthors()) {
				bridge.add(a);
			}
			this.directorsPanel = new AuthorMiniPanel("Bands", bridge);
			this.directorsPanel
					.addPropertyChangeListener(new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent e) {
							String property = e.getPropertyName();
							if (property.equals("bandSelected")
									|| property.equals("bandCreated")) {
								Author tmp = (Author) e.getNewValue();
								if (selectedMovie.getAuthors() == null) {
									selectedMovie.addAuthor(tmp);
									return;
								}
							}
						}
					});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void clearContentFields() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Boolean createItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void fetchFromAPI(String query, String criteria) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void cancelAPISearch() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void fillWithResult(BaseTable table) {
		// TODO Auto-generated method stub

	}

}
