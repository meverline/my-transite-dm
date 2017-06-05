/**
 * 
 */
package browser.gui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
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

import browser.graph.GraphBuilder;
import browser.graph.GraphVizGraph;
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
		setLocationRelativeTo(frame);
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
		close.addActionListener( new InteractionCommand());
		buttonBox.add(close);
		
	    close = new JButton("Inherit Graph");
		close.addActionListener( new InheritCommand());
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
		
		JList<ClassReflectionData> list = null;
		if (set != null) {
			ClassReflectionData array[] = new ClassReflectionData[set.size()];
			int ndx = 0;
			for (ClassXRef.Association cls : set) {
				array[ndx++] = cls.getTheClass();
			}
			list = new JList<ClassReflectionData>(array);
		} else {
			list = new JList<ClassReflectionData>();
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
		
		JList<ClassReflectionData> list;
		if (set != null) {
			ClassReflectionData array[] = new ClassReflectionData[set.size()];
			int ndx = 0;
			for (ClassReflectionData cls : set) {
				array[ndx++] = cls;
			}
			list = new JList<ClassReflectionData>(array);
		} else {
			list = new JList<ClassReflectionData>();
		}
		tabpane.addTab("SubClasses", new JScrollPane(list));		
	}
	
	/**
	 * 
	 * @param tabpane
	 */
	private void createInheritPanel(JTabbedPane tabpane)
	{
		List<String> inf = getDisplayClass().getInterfaces();
		String data[] = new String[inf.size()];
		inf.toArray(data);
		
		JList<String> list = new JList<String>(data);
		tabpane.addTab("Inherit", new JScrollPane(list));		
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
        
        String objs[]  = new String[getDisplayClass().getFields().size()];
        int ndx = 0;
        for ( FieldData cons : getDisplayClass().getFields() ) {
        	objs[ndx++] = fieldToString(cons);
        }
        
        JList<String> fldList = new JList<String>(objs);
        top.add(new JScrollPane(fldList), BorderLayout.CENTER);
        
        JPanel bottom = new JPanel();
        bottom.setLayout( new BorderLayout());
        bottom.add( new JLabel("Methods"), BorderLayout.NORTH);
        
        String array[] = new String[getDisplayClass().getMethods().size() ];
               
        ndx = 0;
        for ( MethodData cons : getDisplayClass().getMethods()) {
        	array[ndx++] = methodToString(cons);
        }
        
        JList<String> methList = new JList<String>(array);
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
	
	protected class InteractionCommand implements ActionListener {
			
		@Override
		public void actionPerformed(ActionEvent arg0) {
	
			HashSet<String> visted = new HashSet<String>();
			GraphVizGraph g = null;
			GraphBuilder builder = new GraphBuilder( main.getProject(), main.getXref());
			builder.setKeepPackageName(true);
						
			int ndx = displayClass.getName().lastIndexOf(".");
			g = new GraphVizGraph( builder.getGraphName(displayClass.getName().substring(ndx+1)));
			
			String name = builder.stripPackage(displayClass.getName());
			GraphVizNode root = new GraphVizNode( name, GraphVizNode.SHAPE.fromClass(displayClass), 0);
			g.add(root);
			
			builder.addToClassInteractionGraph(main.getXref().getUsesClasstRef(displayClass), 1, g, root, visted);
			
			GraphOptionsDialog dialog = new GraphOptionsDialog(DisplayClassDialog.this, g);
			dialog.setVisible(true);

		}
		
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	
	protected class InheritCommand implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
	
			HashSet<String> visted = new HashSet<String>();
			GraphVizGraph g = null;
			GraphBuilder builder = new GraphBuilder( main.getProject(), main.getXref());
			builder.setKeepPackageName(true);
						
			int ndx = displayClass.getName().lastIndexOf(".");
			g = new GraphVizGraph( builder.getGraphName(displayClass.getName().substring(ndx+1)));
			
			String name = builder.stripPackage(displayClass.getName());
			GraphVizNode root = new GraphVizNode( name, GraphVizNode.SHAPE.fromClass(displayClass), 0);
			g.add(root);
			
			int depth = 0;
			builder.addToInheritanceGraph(main.getXref().getInheritRef(displayClass), depth+1, g, root, visted, false);

			GraphOptionsDialog dialog = new GraphOptionsDialog(DisplayClassDialog.this, g);
			dialog.setVisible(true);			
		}
		
	}
	
}
