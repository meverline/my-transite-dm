package browser.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.gui.commands.ClassInteractionGraphCommand;
import browser.gui.commands.ClassSelection;
import browser.gui.commands.ExitCommand;
import browser.gui.commands.InheritanceGraphCommand;
import browser.gui.commands.LoadProjectCommand;
//import browser.gui.commands.NewProjectCommand;
import browser.gui.commands.PackageGraphCommand;
import browser.gui.commands.SearchActionCommand;
import browser.gui.commands.SettingDialogCommand;
//import browser.gui.commands.SettingsProjectCommand;
import browser.gui.commands.ShowClassCommand;
import browser.gui.dialog.SearchDialog;
import browser.gui.models.NodeData;
import browser.loader.ClassReflectionData;
import browser.loader.ScannedClassLoader;
import browser.scanner.ClassScanner;
import browser.util.ApplicationSettings;
import browser.util.ClassXRef;
import browser.util.Project;

@SuppressWarnings("serial")
public class AppMainWindow extends JFrame {

	private Log log = LogFactory.getLog(AppMainWindow.class);

	private JTree classList = null;
	private ScannedClassLoader loader = null;
	private ClassXRef xref = null;
	private Project project = null;
	private JPanel top = null;
	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("classes");
	
	/**
	 * 
	 */
	public AppMainWindow()
	{
		ApplicationSettings settings = ApplicationSettings.instance();
		
		top = build();		
		this.getContentPane().add(top);
		
		if ( settings.getSettings().getCurrentProject() != null ) {
			loadProject(settings.getSettings().getCurrentProject());
		}
		
		this.pack();
		setSize(400, 600);
		setLocationRelativeTo(null);
	}
	
	/**
	 * 
	 * @param newProject
	 */
	public void loadProject(Project newProject) {
		
		this.project = newProject;
		ApplicationSettings.instance().getSettings().setCurrentProject(newProject);

		try {
			
			ClassScanner cs = new ClassScanner();
			cs.scan(this.project.getScanClassPath());
			
			System.out.println("Class Scanner found: " + cs.getFiles().size());
			
			setLoader(new ScannedClassLoader(cs.getFiles(),  newProject.getLoadPath(), cs.getSrcFiles()));
			setXref(new ClassXRef(loader));
			
			root.removeAllChildren();
			if ( this.getLoader() != null && ! this.getLoader().getClassList().isEmpty()  ) {
			   buildNodeList(root);
			}
			
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}

	}
	
	/**
	 * 
	 */
	private void createMenuBar()
	{
		JMenuBar bar = new JMenuBar();
		
		JMenu menu = new JMenu("File");
		bar.add(menu);
		
		JMenuItem button = new JMenuItem("Search...");
		button.addActionListener( new SearchCommand(this, new ShowClassCommand()));
		menu.add(button);
		
		button = new JMenuItem("Metrics...");
		menu.add(button);
		
		button = new JMenuItem("Settings...");
		button.addActionListener( new SettingDialogCommand(this));
		menu.add(button);

		menu.add(new JSeparator());
		
		button = new JMenuItem("Exit");
		button.addActionListener( new ExitCommand());
		menu.add(button);
		
		/////////////////////////////////////////////////////

		menu = new JMenu("Project");
		bar.add(menu);
		
		button = new JMenuItem("Load...");
		button.addActionListener( new LoadProjectCommand(this));
		menu.add(button);
	
		/////////////////////////////////////////////////////
		
		menu = new JMenu("UML");
		bar.add(menu);
		
		button = new JMenuItem("Package Dependacy ...");
		button.addActionListener( new SearchCommand(this, new PackageGraphCommand()));
		menu.add(button);
			
		button = new JMenuItem("Class Interaction ...");
		button.addActionListener( new SearchCommand(this, new ClassInteractionGraphCommand()));
		menu.add(button);
		
		button = new JMenuItem("Inheritance ...");
		button.addActionListener( new SearchCommand(this, new InheritanceGraphCommand()));
		menu.add(button);
				
		setJMenuBar(bar);
		
	}
	
	/**
	 * 
	 */
	private JPanel build()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Source/Class Code Analysis");
		setSize(700, 700);
		
		JPanel panel = new JPanel();	
		panel.setLayout( new BorderLayout());
						
		classList = new JTree(root);
		((DefaultTreeModel) classList.getModel()).setRoot(root);
		classList.addTreeSelectionListener( new ClassSelection(classList, this));
		classList.setCellRenderer(new TreeCellRenderer());

		panel.add( new JScrollPane(classList), BorderLayout.CENTER);
		createMenuBar();
		this.doLayout();
				
		return panel;
	}
	
	private void buildNodeList( DefaultMutableTreeNode root ) {
		
		Map<String,DefaultMutableTreeNode> nodeMap = 
						new HashMap<String,DefaultMutableTreeNode>();
		
		System.out.println( this.getLoader().getClassList().size());
		DefaultMutableTreeNode node = root;
		StringBuilder builder = new StringBuilder();
		for ( String name : this.getLoader().getClassList() ) {
			
			ClassReflectionData cls = null;
			try {
				cls = this.getLoader().load(name);
			} catch (Exception e) {
				log.error(e);
				cls = null;
			}
			
			List<String> classPath = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(name, ".");
		    while (st.hasMoreTokens()) {
		        classPath.add(st.nextToken());
		    }
		    
			node = root;
			builder.delete(0, builder.length());
			for  ( String component : classPath ) {
				builder.append(component);
				if ( nodeMap.containsKey(builder.toString())) {
					node = nodeMap.get(builder.toString());
				} else {
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(component);
					
					StringBuilder label = new StringBuilder();
					label.append( component);
					child.setUserObject(new NodeData(builder.toString(), label.toString(), cls));
					node.add(child);
					nodeMap.put(builder.toString(), child);
					node = child;
				}
				
				builder.append(".");
			}
		}	
	}
	
	/**
	 * @return the log
	 */
	public Log getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	protected void setLog(Log log) {
		this.log = log;
	}

	/**
	 * @return the loader
	 */
	public ScannedClassLoader getLoader() {
		return loader;
	}

	/**
	 * @param loader the loader to set
	 */
	protected void setLoader(ScannedClassLoader loader) {
		this.loader = loader;
	}

	/**
	 * @return the xref
	 */
	public ClassXRef getXref() {
		return xref;
	}

	/**
	 * @param xref the xref to set
	 */
	protected void setXref(ClassXRef xref) {
		this.xref = xref;
	}
	
	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public class SearchCommand implements ActionListener {
		
		private SearchActionCommand command = null;
		private AppMainWindow main = null;
		
		public SearchCommand(AppMainWindow win, SearchActionCommand cmd)
		{
			command = cmd;
			main = win;
		}

		public void actionPerformed(ActionEvent e) {
			command.setCurrentProject(main.getProject());
			SearchDialog dialog = new SearchDialog(main, command);
			dialog.setVisible(true);
		}
		
	}
	
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	 public class TreeCellRenderer extends DefaultTreeCellRenderer {

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, 
													  boolean expanded, boolean leaf, int row, 
													  boolean hasFocus) {

			JLabel label = JLabel.class.cast(super.getTreeCellRendererComponent(tree, value, 
																				sel, expanded, 
																				leaf, row, 
																				hasFocus));
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			ImageIcon icon = null;
			
			if ( node.isLeaf() && (!(node.getUserObject() instanceof String)) ) {
				ClassReflectionData data = ((NodeData) node.getUserObject()).getCls();	
				icon = IconLoader.instance().getIcon(data);
			} else {
				icon = IconLoader.instance().getIcon("P");
				
			}
			if ( icon != null ) {
				label.setIcon(icon);
			}
			return label;
		}
		 
		 
	 }
}
