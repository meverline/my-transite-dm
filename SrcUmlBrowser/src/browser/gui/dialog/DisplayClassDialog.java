/**
 * 
 */
package browser.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import browser.graph.GraphVizEdge;
import browser.graph.GraphVizNode;
import browser.gui.AppMainWindow;
import browser.gui.commands.CloseDialogAction;
import browser.gui.layout.SpringLayoutUtilities;
import browser.loader.ClassReflectionData;
import browser.loader.FieldData;
import browser.loader.MethodData;
import browser.loader.ParamaterData;
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
		
		protected final static String ROOT_NODE = "__ROOT_NODE__";
		protected final static String SHAPE = "shape";
		protected final static String NAME = "name";
		protected final static String DATA = "data";
		protected final static String TYPE = "type";

		private JDialog dialog = null;
		
		protected GraphCommand(JDialog parent) {
			dialog = parent;
		}
		
		protected void createGraph(Graph g, String title ) {
			
			Cursor current = getCursor();
			setCursor( new Cursor(Cursor.WAIT_CURSOR));
			try {
				
				ImageDialog dg = new ImageDialog(dialog, g, title + ": " + displayClass.getShortName());
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
		
		/**
		 * 
		 * @param g
		 * @param aClass
		 * @return
		 */
		protected Node createNode(Graph g, ClassReflectionData aClass, HashMap<String, Node> map, String name)
		{
			Node root = g.addNode();
			root.set(SHAPE, new Integer(GraphVizNode.SHAPE.fromClass(aClass).ordinal()));
			root.set(NAME, name);
			root.set(DATA, aClass);
			map.put(name, root);
			return root;
		}
		
		
		protected Graph createGraph()
		{
			Graph g = new Graph();
			g.addColumn(SHAPE, Integer.class);
			g.addColumn(NAME, String.class);
			g.addColumn(DATA, ClassReflectionData.class);
			g.addColumn(TYPE, Integer.class);
			return g;
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
	
			HashMap<String, Node> map = new HashMap<String, Node>();
			Graph g = this.createGraph();
			
			this.addSupperClasstoGraph(g, map, displayClass, 0, displayClass.getPackagePath());
			this.createGraph(g, "Interaction");
		}
		
		/**
		 * 
		 * @param g
		 * @param aClass
		 * @param shortName
		 * @param depth
		 * @return
		 */
		private Node addSupperClasstoGraph(Graph g, HashMap<String, Node> map, ClassReflectionData aClass, int depth, String ppath)
		{
			String name = aClass.getName();
			if ( aClass.getPackagePath().equals(ppath) ) {
				name = aClass.getShortName();
			} else {
				name = this.getShortsName(ppath, aClass.getName());
			}
					
			Node root = this.createNode(g, aClass, map, name);
			
			this.addNodesToGraph(main.getXref().getUsesClasstRef(aClass), map, g, root, ppath);
			
			if ( aClass.getSuperClass() != null ) {
				ClassReflectionData data = main.getLoader().load(aClass.getSuperClass());
				if ( data != null) {
					Node node = this.addSupperClasstoGraph(g, map, data, depth++, ppath);
					Edge edge = g.addEdge(root, node);
					edge.set(TYPE, GraphVizEdge.TYPE.normal.ordinal());
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
		private void addNodesToGraph(Set<ClassXRef.Association> set, HashMap<String, Node> map, Graph g, Node root, String ppath)
		{
			for ( ClassXRef.Association item : set ) {
				Node node = map.get(item.toString());
				
				if ( node == null ) {
					String name = this.getShortsName(ppath, item.toString());
					node = this.createNode(g, item.getTheClass(), map, name);					
				}
				
				Edge edge = g.addEdge(root,  node);
				if ( item.isField() ) {
					edge.set(TYPE, GraphVizEdge.TYPE.none.ordinal());
				} else {
					edge.set(TYPE, GraphVizEdge.TYPE.dot.ordinal());
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
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
	
			Graph g = this.createGraph();
			
			HashSet<String> visted = new HashSet<String>();
			HashMap<String, Node> map = new HashMap<String, Node>();
			
			this.addClassToGraph(g, map, displayClass, displayClass.getPackagePath(), visted, 0, true);
			this.createGraph(g, "Inherit");
						
		}
		
		private Node addClassToGraph(Graph g, HashMap<String, Node> map, ClassReflectionData classRoot, String packagePath, HashSet<String> visted, int depth, boolean showSubClass)
		{
			String name = this.getShortsName(packagePath, classRoot.getName());
			
		    Node root = this.createNode(g, classRoot, map, name);
			
			if ( classRoot.getPackagePath().contains(packagePath) ) 
			{
				this.addNodesToGraph(main.getXref().getInheritRef(classRoot), map, depth+1, g, root, visted, packagePath);
			}
			
			List<ClassReflectionData> aList = new ArrayList<ClassReflectionData>();
			for ( String itemCls : classRoot.getInterfaces()) {
				if ( this.showClass(itemCls)) {
					ClassReflectionData data = main.getLoader().load(itemCls);
					Node node = map.get(itemCls);
					if ( node == null ) {
						node = this.createNode(g, data, map, itemCls);
					}
					Edge edge = g.addEdge(root, node);
					edge.set(TYPE, GraphVizEdge.TYPE.normal.ordinal());
					
					aList.clear();
					if ( data != null ) {
						for ( String cls : data.getInterfaces()) {
							ClassReflectionData crd = main.getLoader().load(cls);
							if ( crd != null ) {
							     aList.add(crd);
							}
						}
						this.addNodesToGraph(aList, map, depth+1, g, root, visted, packagePath);	
					}
				}
			}
			
			if ( classRoot.getSuperClass() != null && this.showClass(classRoot.getSuperClass()) ) {
			   ClassReflectionData data = main.getLoader().load(classRoot.getSuperClass());
			   Node parent = this.addClassToGraph(g, map, data, packagePath, visted, depth+1, false);
			   
			   if ( parent != null ) {
				   Edge edge = g.addEdge(root, parent);
				   edge.set(TYPE, GraphVizEdge.TYPE.normal.ordinal());
			   }
			}
			return root;
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
		
		
		private void addNodesToGraph(Collection<ClassReflectionData> set, HashMap<String, Node> map, int depth, Graph g, Node root, HashSet<String> visted, String ppath)
		{
			if ( set == null ) { return; }
			
			for ( ClassReflectionData item : set ) {
				if ( this.showClass(item.getName())) {
					String name = this.getShortsName(displayClass.getPackagePath(), item.getName());
					Node node =  map.get(name);
					if ( node == null ) {
						node = this.createNode(g, item, map, name);
					}
					
					Edge edge = g.addEdge(root, node);
					edge.set(TYPE, GraphVizEdge.TYPE.normal.ordinal());
					
					if ( ! visted.contains(item.getName())) {
					   visted.add(item.getName());
					   if ( item.getPackagePath().contains(ppath)) {
						   this.addNodesToGraph(main.getXref().getInheritRef(item), map,
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
