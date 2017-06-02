package browser.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
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
	
	/**
	 * 
	 */
	public AppMainWindow()
	{
		ApplicationSettings settings = ApplicationSettings.instance();
		
		top = build();		
		this.getContentPane().add(top);
		
		if ( settings.getSettings().getCurrentProject() != null ) {
			Project newProject = settings.loadCurrentProject();
			if ( newProject != null ) {
			   loadProject(newProject);
			}
		}
		
		this.pack();
		setSize(400, 600);
		this.setLocation(50, 50);
		
	}
	
	public void loadProject(Project newProject) {
		
		this.project = newProject;
		ApplicationSettings.instance().getSettings().setCurrentProject(newProject.getName());

		try {
			
			ClassScanner cs = new ClassScanner();
			cs.scan(this.project.getScanClassPath());
			
			System.out.println("Class Scanner found: " + cs.getFiles().size());
			
			setLoader(new ScannedClassLoader(cs.getFiles(),  newProject.getLoadPath()));
			setXref(new ClassXRef(loader));
			
			DefaultMutableTreeNode root = null;
			if ( this.getLoader() != null && ! this.getLoader().getClassList().isEmpty()  ) {
			   
			   root = new DefaultMutableTreeNode("classes");
			   buildNodeList(root);
			}
			((DefaultTreeModel) classList.getModel()).setRoot(root);
			classList.addTreeSelectionListener( new ClassSelection(classList, this));
			
			top.removeAll();
			top.add( new JScrollPane(classList), BorderLayout.CENTER);
			top.doLayout();
			this.doLayout();
			
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
				
		DefaultMutableTreeNode root = null;
		
		classList = new JTree(root);
		panel.add( new JScrollPane(classList), BorderLayout.CENTER);
		
		createMenuBar();
				
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
					if ( builder.toString().compareTo(name) == 0) {
						if ( cls != null ) {
							if ( cls.isInterface() ) {
								label.append("  [I]");
							} else if ( cls.isEnum() ) {
								label.append("  [E]");
							} else  {
								label.append("  [C]");
							}
						}
					}
					child.setUserObject(new NodeData(builder.toString(), label.toString()));
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
}
