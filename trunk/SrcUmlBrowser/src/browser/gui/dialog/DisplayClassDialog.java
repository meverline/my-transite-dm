/**
 * 
 */
package browser.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.tokenmarker.JavaTokenMarker;

import browser.graph.Edge;
import browser.graph.Graph;
import browser.graph.Node;
import browser.gui.AppMainWindow;
import browser.gui.commands.CloseDialogAction;
import browser.gui.layout.SpringLayoutUtilities;
import browser.loader.ClassReflectionData;
import browser.loader.FieldData;
import browser.loader.MethodData;
import browser.loader.ParamaterData;
import browser.util.ApplicationSettings;
import browser.util.ClassXRef;

/**
 * @author markeverline
 *
 */
public class DisplayClassDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(DisplayClassDialog.class);
	private AppMainWindow main = null;
	private ClassReflectionData displayClass = null;
	private JSplitPane right = null;
	
	public DisplayClassDialog(AppMainWindow frame, ClassReflectionData display)
	{
		super(frame);
		main = frame;
		this.setDisplayClass(display);

		build();
		setSize(600, 500);
		setTitle(this.getDisplayClass().getName());
		this.setLocationByPlatform(true);
		right.setDividerLocation(0.5);

		this.setVisible(true);
	}

	/**
	 * @return the main
	 */
	public AppMainWindow getMainWindow() {
		return main;
	}

	/**
	 * @param main the main to set
	 */
	public void setMainWindow(AppMainWindow main) {
		this.main = main;
	}

	/**
	 * @return the displayClass
	 */
	public ClassReflectionData getDisplayClass() {
		return displayClass;
	}

	/**
	 * @param displayClass the displayClass to set
	 */
	public void setDisplayClass(ClassReflectionData displayClass) {
		this.displayClass = displayClass;
	}
	
	private void build()
	{
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout( new BorderLayout());
		panel.add(mainPanel, BorderLayout.CENTER);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout( new SpringLayout());
		mainPanel.add(infoPanel, BorderLayout.NORTH);
		
		int numberRows = createInfoPanel(infoPanel);
		SpringLayoutUtilities.makeCompactGrid(infoPanel, numberRows, 2, 2, 2, 5, 5);

		JTabbedPane tabpane = new JTabbedPane();
		mainPanel.add(tabpane, BorderLayout.CENTER);
		
		createClassPanel(tabpane);
		createInheritPanel(tabpane);
		createSubClassPanel(tabpane);
		createUsesClassPanel(tabpane);
		createSoucePanel(tabpane);
			
		JPanel buttonBox = new JPanel();
						
	    JButton close = new JButton("Interaction Graph");
		close.addActionListener( new InteractionCommand(this));
		buttonBox.add(close);
		
	    close = new JButton("Inherit Graph");
		close.addActionListener( new InheritCommand(this));
		buttonBox.add(close);
		
		close = new JButton("Close");
		close.addActionListener( new CloseDialogAction(this));
		buttonBox.add(close);
		
		panel.add(buttonBox, BorderLayout.SOUTH);
		
		this.setContentPane(panel);
	}
	
	/**
	 * 
	 * @param tabpane
	 */
	private void createUsesClassPanel(JTabbedPane tabpane)
	{
		Set<ClassXRef.Association> set = main.getXref().getUsesClasstRef(getDisplayClass());
		
		JList list = null;
		if (set != null) {
			ClassReflectionData array[] = new ClassReflectionData[set.size()];
			int ndx = 0;
			for (ClassXRef.Association cls : set) {
				array[ndx++] = cls.getTheClass();
			}
			list = new JList(array);
		} else {
			list = new JList();
		}
		
		tabpane.addTab("Use Classes", new JScrollPane(list));	
	}
	
	/**
	 * 
	 * @param tabpane
	 */
	private void createSubClassPanel(JTabbedPane tabpane)
	{
		Set<ClassReflectionData> set = main.getXref().getInheritRef(getDisplayClass());
		
		JList list;
		if (set != null) {
			ClassReflectionData array[] = new ClassReflectionData[set.size()];
			int ndx = 0;
			for (ClassReflectionData cls : set) {
				array[ndx++] = cls;
			}
			list = new JList(array);
		} else {
			list = new JList();
		}
		tabpane.addTab("SubClasses", new JScrollPane(list));		
	}
	
	/**
	 * 
	 * @param tabpane
	 */
	private void createInheritPanel(JTabbedPane tabpane)
	{
		JList list = new JList(getDisplayClass().getInterfaces().toArray());
		tabpane.addTab("Inherit", new JScrollPane(list));		
	}
	
	/**
	 * Code to find the class's which are a passed/used as part of the generic
	 * arguments. 
	 * @param type
	 * @return
	 */
	protected String discoverGenericTypes(Type type)
	{
		StringBuilder buidler = new StringBuilder();
		
		if ( type instanceof WildcardType) {
			
			WildcardType item = WildcardType.class.cast(type);

			buidler.append("<");
			for ( Type args : item.getUpperBounds()) {
				buidler.append(this.discoverGenericTypes(args));
			}
			
			for ( Type args : item.getLowerBounds()) {
				buidler.append(this.discoverGenericTypes(args));
			}
			buidler.append(">");
			
		} else if ( type instanceof ParameterizedType) {
			
			ParameterizedType item = ParameterizedType.class.cast(type);
			
			buidler.append(this.discoverGenericTypes(item.getRawType()));
			buidler.append("<");
			for (Type args : item.getActualTypeArguments()) {
				buidler.append(this.discoverGenericTypes(args));
			}
			buidler.append(">");
		
		} else if ( type instanceof TypeVariable<?>) {
			
			TypeVariable<?> item = TypeVariable.class.cast(type);
			for (Type args : item.getBounds()) {
				buidler.append(this.discoverGenericTypes(args));
			}
							
		} else if ( type instanceof GenericArrayType) {
			
			GenericArrayType item = GenericArrayType.class.cast(type);
			
			buidler.append(this.discoverGenericTypes(item.getGenericComponentType()));
			
		} else {
			buidler.append( ((Class<?>)type).getSimpleName() );
		}
		return buidler.toString();
	}
	
	private String removeJavaLang(String name) {
		String rtn = name;
		if ( name.startsWith("java.lang") || name.startsWith("java.util") ) {
			int ndx = name.lastIndexOf(".");
			rtn = name.substring(ndx+1);
		} 
		return rtn;
	}
	/**
	 * 
	 * @param method
	 * @return
	 */
	private String methodToString(MethodData method)
	{
		StringBuilder builder = new StringBuilder();
		
		if ( method.isPublic() ) {
			builder.append("public ");
		} else if ( method.isPrivate() ) {
			builder.append("private ");
		} else if ( method.isProtected() ) {
			builder.append("protected ");
		}
		
		if ( method.isThreadSafe() ) {
			builder.append("synchronized ");
		}
		
		if ( method.isStatic() ) {
			builder.append("static ");
		} else if ( method.isAbstract() ) {
			builder.append("abstract ");
		}
		
		builder.append( method.getName());
		builder.append( "(");

		int ndx = 0;
		for ( ParamaterData param : method.getParameters()) {
		   if ( ndx != 0 ) { builder.append(", "); }
		   builder.append(this.removeJavaLang(param.getType()));  
		   if ( param.isGeneric() ) {
			   builder.append("<");
			   int count = 0;
			   for ( String item : param.getGenericTypes()) {
				   if ( count != 0 ) { builder.append(", "); }
				   builder.append(item);
				   count++;
			   }
			   builder.append(">");
		   }
		   ndx++;
		}
		
		builder.append( "): ");
		
		builder.append( this.removeJavaLang(method.getRetrunType().getType()));
		if ( method.getRetrunType().isGeneric() ) {
			builder.append("<");
			   int count = 0;
			   for ( String item : method.getRetrunType().getGenericTypes()) {
				   if ( count != 0 ) { builder.append(", "); }
				   builder.append(item);
			   }
			   builder.append(">");
		}
		return builder.toString();
	}
		
	/**
	 * 
	 * @param method
	 * @return
	 */
	private String fieldToString(FieldData method)
	{
		StringBuilder builder = new StringBuilder();
		
		if ( method.isPublic() ) {
			builder.append("public ");
		} else if ( method.isPrivate() ) {
			builder.append("private ");
		} else if ( method.isProtected() ) {
			builder.append("protected ");
		}
		
		if ( method.isStatic() ) {
			builder.append("static ");
		}
		
		builder.append( method.getName());
		
		builder.append(" : ");
		builder.append( this.removeJavaLang(method.getType()));		
		
		if ( method.isGeneric() ) {
			int ndx = 0;
			builder.append("<");
			for ( String type : method.getGenericTypes() ) {
				if (ndx != 0 ) { builder.append(", "); }
				builder.append(type);
				ndx++;
			}
			builder.append(">");
		}
		return builder.toString();
	}
	
	private void createSoucePanel(JTabbedPane tabpane)
	{
		JEditTextArea area = new JEditTextArea();
		area.setTokenMarker(new JavaTokenMarker());
		
        String fileName = main.getLoader().getClassFilename(getDisplayClass());
        if ( fileName != null ) {
	        try {
	        	BufferedReader reader = new BufferedReader(new FileReader(fileName));
	        	
	        	StringBuilder builder = new StringBuilder();
	        	while (reader.ready() ) {
	        		builder.append(reader.readLine());
	        		builder.append("\n");
	        	}
				reader.close();
	        	area.setText(builder.toString());
	        	area.setCaretPosition(0);
			} catch (Exception e) {
				@SuppressWarnings("unused")
				ExceptionDialog ed = new ExceptionDialog(main, e);
				log.error(e);
			}
        }
        
        tabpane.addTab("Source", area);
	}
	
	/**
	 * 
	 * @param tabpane
	 */
	private void createClassPanel(JTabbedPane tabpane)
	{	
        right = new JSplitPane();
        right.setOneTouchExpandable(true);
        
        tabpane.addTab("Class", right);
  
        right.setOrientation(JSplitPane.VERTICAL_SPLIT);               
        JPanel top = new JPanel();
        top.setLayout( new BorderLayout());
        
        top.add( new JLabel("Fields"), BorderLayout.NORTH);
        
        Object objs[]  = new Object[getDisplayClass().getFields().size()];
        int ndx = 0;
        for ( FieldData cons : getDisplayClass().getFields() ) {
        	objs[ndx++] = fieldToString(cons);
        }
        
        JList fldList = new JList(objs);
        top.add(new JScrollPane(fldList), BorderLayout.CENTER);
        
        JPanel bottom = new JPanel();
        bottom.setLayout( new BorderLayout());
        bottom.add( new JLabel("Methods"), BorderLayout.NORTH);
        
        Object array[] = new Object[getDisplayClass().getMethods().size() ];
               
        ndx = 0;
        for ( MethodData cons : getDisplayClass().getMethods()) {
        	array[ndx++] = methodToString(cons);
        }
        
        JList methList = new JList(array);
        bottom.add(new JScrollPane(methList), BorderLayout.CENTER);
        
        right.setTopComponent(top);
        right.setBottomComponent(bottom);
        right.setDividerLocation(0.50);
 
	}
	
	/**
	 * 
	 * @param panel
	 * @return
	 */
	private int createInfoPanel(JPanel panel)
	{
		int numberRows = 0;
		
		panel.add( new JLabel("Class Name: "));
		panel.add( new JLabel( getDisplayClass().getName() ));
		numberRows++;
		
		if ( getDisplayClass().getSuperClass() != null ) {
			panel.add( new JLabel("Super Class: "));
			panel.add( new JLabel( getDisplayClass().getSuperClass() ));	
		} else {
			panel.add( new JLabel("Super Class: "));
			panel.add( new JLabel( Object.class.getName() ));		
		}
		numberRows++;
		
		panel.add( new JLabel("Class Flags: "));
		
		StringBuilder builder = new StringBuilder();
		
		if ( getDisplayClass().isPublic() ) {
			builder.append("public ");
		} else if ( getDisplayClass().isPrivate() ) {
			builder.append("private ");
		} else if ( getDisplayClass().isProtected() ) {
			builder.append("protected ");
		}
		
		if ( getDisplayClass().isStatic() ) {
		   builder.append("static ");
	    }
		
		if ( getDisplayClass().isEnum() ) {
			builder.append("enum ");
		} else {
			if ( getDisplayClass().isAnnotation() ) {
				builder.append("annotaion ");
			}
			if ( getDisplayClass().isInterface() ) {
				builder.append("interface ");
			}
			if ( getDisplayClass().isEnum() ) {
				builder.append("enum ");
			}
			if ( getDisplayClass().isSynthetic() ) {
				builder.append("synthetic ");
			}
			if ( getDisplayClass().isFinal() ) {
				builder.append("final ");
			}
			if ( getDisplayClass().isAbstract() ) {
				builder.append("abstract ");
			}

		}

		panel.add( new JLabel(builder.toString()) );
		numberRows++;

		return numberRows;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	
	protected abstract class GraphCommand implements ActionListener {
		
		private JDialog dialog = null;
		
		protected GraphCommand(JDialog parent) {
			dialog = parent;
		}
		
		protected void createGraph(Graph g, String title, Graph.CMD command ) {
			
			Cursor current = getCursor();
			setCursor( new Cursor(Cursor.WAIT_CURSOR));
			try {
				
				ApplicationSettings set = ApplicationSettings.instance();
				StringBuilder fileName = new StringBuilder();
				
				fileName.append(set.getSettings().getGraphOutputDir());
				fileName.append(File.separator);
				fileName.append(displayClass.getShortName());
				fileName.append(".dot");
				
				PrintWriter writer = new PrintWriter( fileName.toString() );
				g.write(writer);
				writer.close();
				
				List<String> cmd = new ArrayList<String>();
				
				StringBuilder exe = new StringBuilder();
				
				exe.append( set.getSettings().getGraphHome() );
				exe.append(File.separator);
				exe.append(command.name() );
								
				File fp = new File(exe.toString().replace("\\", "/"));
				if ( ! fp.exists()) {
					cmd.add( command.name() );
				} else {
				    cmd.add(exe.toString());
				}
				
				cmd.add("-T" + Graph.OUTPUT.jpg.name());
				
				String input = fileName.toString();
				fileName.delete(0, fileName.length());
				
				fileName.append(set.getSettings().getGraphOutputDir());
				fileName.append(File.separator);
				fileName.append(displayClass.getShortName());
				fileName.append(".");
				fileName.append( Graph.OUTPUT.jpg.name());
				cmd.add("-o" + fileName.toString());
				cmd.add(input);
				
				StringBuilder builder = new StringBuilder();
				for ( String arg : cmd) {
					builder.append(arg);
					builder.append(" ");
				}
				log.info(builder.toString());
				
				ProcessBuilder pb = new ProcessBuilder(cmd);
				pb.redirectErrorStream(true);
				Process p = pb.start();
				p.waitFor();
				
				if ( p.exitValue() != 0 ) {
					InputStream err = p.getErrorStream();
					
					StringBuilder errString = new StringBuilder();
					errString.append("exit value ");
					errString.append(p.exitValue());
					errString.append(" ");
					while( err.available() != 0 ) {
						errString.append( err.read() );
					}
				    log.error( p.exitValue() + errString.toString());	
					
				}
				
				ImageDialog dg = new ImageDialog(dialog, fileName.toString(), title + ": " + displayClass.getShortName());
				dg.setVisible(true);
				
			} catch (Exception e) {
				log.error(e);
				@SuppressWarnings("unused")
				ExceptionDialog ed = new ExceptionDialog(dialog, e);
			}
			setCursor( current);

		}
		
		/**
		 * 
		 * @param ppath
		 * @param name
		 * @return
		 */
		protected String getShortsName(String ppath, String name)
		{
			StringBuffer rtn = new StringBuffer();
			
			String [] pdata = ppath.split("\\.");
			String [] cdata = name.split("\\.");
			
			int length = Math.min(pdata.length, cdata.length);
			
			for ( int ndx = 0; ndx < length; ndx++ ) {
				if ( ! pdata[ndx].equals(cdata[ndx]) ) {
					if ( rtn.length() > 0 ) { rtn.append("."); }
					rtn.append(cdata[ndx]);
				}
			}
			
			if ( cdata.length > length ) {
				for ( int ndx = length; ndx < cdata.length; ndx++ ) {
					if ( rtn.length() > 0 ) { rtn.append("."); }
					rtn.append(cdata[ndx]);
				}
			}
			return rtn.toString();
		}
		
	}
		
		
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	
	protected class InteractionCommand  extends GraphCommand {
				
		public InteractionCommand(JDialog parent) {
			super(parent);
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
	
			Graph g = new Graph(displayClass.getShortName());
			
			this.addSupperClasstoGraph(g, displayClass, 0, displayClass.getPackagePath());
			this.createGraph(g, "Interaction", Graph.CMD.sfdp);
		}
		
		/**
		 * 
		 * @param g
		 * @param aClass
		 * @param shortName
		 * @param depth
		 * @return
		 */
		private Node addSupperClasstoGraph(Graph g, ClassReflectionData aClass, int depth, String ppath)
		{
			String name = aClass.getName();
			if ( aClass.getPackagePath().equals(ppath) ) {
				name = aClass.getShortName();
			} else {
				name = this.getShortsName(ppath, aClass.getName());
			}
			
			Node root = new Node( name, Node.SHAPE.fromClass(aClass), 0);
			g.add(root);
			
			this.addNodesToGraph(main.getXref().getUsesClasstRef(aClass), g, root, ppath);
			
			if ( aClass.getSuperClass() != null ) {
				ClassReflectionData data = main.getLoader().load(aClass.getSuperClass());
				if ( data != null) {
					Node node = this.addSupperClasstoGraph(g, data, depth++, ppath);
					root.addEdge( new Edge(node, Edge.TYPE.normal));
				}  
			}
			return root;
		}
		
		/**
		 * 
		 * @param set
		 * @param g
		 * @param root
		 */
		private void addNodesToGraph(Set<ClassXRef.Association> set, Graph g, Node root, String ppath)
		{
			for ( ClassXRef.Association item : set ) {
				Node node = g.getNodeByName(item.toString());
				if ( node == null ) {
					String name = this.getShortsName(ppath, item.toString());
					node = new Node( name, Node.SHAPE.fromClass(item.getTheClass()), 0);
					g.add(node);
				}
				
				if ( item.isField() ) {
					root.addEdge( new Edge(node, Edge.TYPE.none));
				} else {
					root.addEdge( new Edge(node, Edge.TYPE.dot));
				}
				
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	
	protected class InheritCommand extends GraphCommand {
		
		public InheritCommand(JDialog parent) {
			super(parent);
		}
		
		
		private Node addClassToGraph(Graph g, ClassReflectionData classRoot, String packagePath, HashSet<String> visted, int depth, boolean showSubClass)
		{
			String name = this.getShortsName(packagePath, classRoot.getName());
			Node root = new Node( name, Node.SHAPE.fromClass(classRoot), 0);
			g.add(root);
			
			
			if ( classRoot.getPackagePath().contains(packagePath) ) 
			{
				this.addNodesToGraph(main.getXref().getInheritRef(classRoot), depth+1, g, root, visted, packagePath);
			}
			
			List<ClassReflectionData> aList = new ArrayList<ClassReflectionData>();
			for ( String itemCls : classRoot.getInterfaces()) {
				if ( this.showClass(itemCls)) {
					ClassReflectionData data = main.getLoader().load(itemCls);
					Node node = g.getNodeByName(itemCls);
					if ( node == null ) {
						node = new Node( itemCls, Node.SHAPE.fromClass(data), depth);
						g.add(node);
					}
					root.addEdge( new Edge(node, Edge.TYPE.normal));
					
					aList.clear();
					if ( data != null ) {
						for ( String cls : data.getInterfaces()) {
							ClassReflectionData crd = main.getLoader().load(cls);
							if ( crd != null ) {
							     aList.add(crd);
							}
						}
						this.addNodesToGraph(aList, depth+1, g, root, visted, packagePath);	
					}
				}
			}
			
			if ( classRoot.getSuperClass() != null && this.showClass(classRoot.getSuperClass()) ) {
			   ClassReflectionData data = main.getLoader().load(classRoot.getSuperClass());
			   Node parent = this.addClassToGraph(g, data, packagePath, visted, depth+1, false);
			   
			   if ( parent != null ) {
					root.addEdge( new Edge(parent, Edge.TYPE.normal));
			   }
			}
			return root;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
	
			Graph g = new Graph(displayClass.getShortName());
			HashSet<String> visted = new HashSet<String>();
			
			this.addClassToGraph(g, displayClass, displayClass.getPackagePath(), visted, 0, true);
			this.createGraph(g, "Inherit", Graph.CMD.dot);
						
		}
		
		protected boolean showClass(String name)
		{		
			if ( name.startsWith("java") || name.startsWith("javax") ) {
				if ( name.contains("servlet") ) {
					return true;
				}
				return false;
			}
			return true;
		}
		
		
		private void addNodesToGraph(Collection<ClassReflectionData> set, int depth, Graph g, Node root, HashSet<String> visted, String ppath)
		{
			if ( set == null ) { return; }
			
			for ( ClassReflectionData item : set ) {
				if ( this.showClass(item.getName())) {
					String name = this.getShortsName(displayClass.getPackagePath(), item.getName());
					Node node = g.getNodeByName(name);
					if ( node == null ) {
						node = new Node( name, Node.SHAPE.fromClass(item), depth);
						g.add(node);
					}
					node.addEdge( new Edge(root, Edge.TYPE.normal));
					
					if ( ! visted.contains(item.getName())) {
					   visted.add(item.getName());
					   if ( item.getPackagePath().contains(ppath)) {
						   this.addNodesToGraph(main.getXref().getInheritRef(item), 
								   				depth+1, 
								   				g, 
								   				node, 
								   				visted, 
								   				ppath);
					   }
					}
				}
			}
		}
	}
	
}
