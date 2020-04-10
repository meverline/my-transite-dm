/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.graph.GraphBuilder;
import browser.graph.GraphVizEdge;
import browser.graph.GraphVizGraph;
import browser.graph.GraphVizNode;
import browser.gui.AppMainWindow;
import browser.gui.dialog.GraphOptionsDialog;
import browser.loader.ClassReflectionData;

/**
 * @author markeverline
 *
 */
public class InheritanceGraphCommand extends AbstractSearchActionCommand {

	private Log log = LogFactory.getLog(InheritanceGraphCommand.class);
	
	/**
	 * 
	 */
	public InheritanceGraphCommand() {
		super("Generate", "Class Inheritance");
	}
	
	/* (non-Javadoc)
	 * @see browser.gui.commands.AbstractSearchActionCommand#isMultiSelect()
	 */
	@Override
	public boolean isMultiSelect() {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		String selected = getDialog().getSelected().get(0);
		AppMainWindow mainWin = getDialog().getMain();
		
		HashSet<String> visted = new HashSet<String>();
		GraphVizGraph g = null;
		GraphBuilder builder = new GraphBuilder(this.getCurrentProject(), mainWin.getXref(), this.getDialog());
		builder.setKeepPackageName(this.getKeepPackageName());
		
		try {
			ClassReflectionData theClass = mainWin.getLoader().load(selected);
			
			String item = getDialog().getSelected().get(0);
			int ndx = item.lastIndexOf(".");
			g = new GraphVizGraph( builder.getGraphName(item.substring(ndx+1)));
			
			String name = builder.stripPackage(theClass.getName());
			GraphVizNode root = new GraphVizNode( name, GraphVizNode.SHAPE.fromClass(theClass), 0);
			g.add(root);
					
			int depth = 0;
			builder.addToInheritanceGraph(mainWin.getXref().getInheritRef(theClass), depth+1, g, root, visted, false);
			
			Set<String> interfaces = new HashSet<String>(); 
			for ( String cls : theClass.getInterfaces()) {
				interfaces.add(cls);
			}

			List<ClassReflectionData> aList = new ArrayList<ClassReflectionData>();
			for (String itemCls : interfaces ) {
				ClassReflectionData data = mainWin.getLoader().load(itemCls);
				GraphVizNode node = g.getNodeByName(itemCls);
				if ( node == null ) {
					node = new GraphVizNode( itemCls, GraphVizNode.SHAPE.fromClass(data), depth);
					g.add(node);
				}
				root.addEdge( new GraphVizEdge(node, GraphVizEdge.TYPE.normal));
				
				aList.clear();
				if ( data != null ) {
					for ( String cls : data.getInterfaces()) {
						ClassReflectionData crd = mainWin.getLoader().load(cls);
						if ( crd != null ) {
						     aList.add(crd);
						}
					}
					builder.addToInheritanceGraph(aList, depth+1, g, root, visted, true);	
				}

			}
			
			if ( theClass.getSuperClass() != null ) {
				ClassReflectionData data = mainWin.getLoader().load(theClass.getSuperClass());
				name = builder.stripPackage(theClass.getSuperClass());
				GraphVizNode node = g.getNodeByName(name);
				if ( node == null && theClass.getSuperClass() != null) {
					node = new GraphVizNode( name, 
							          GraphVizNode.SHAPE.fromClass(data), 
							          depth);
					g.add(node);

				} 
				if ( node != null ) {
					root.addEdge( new GraphVizEdge(node, GraphVizEdge.TYPE.normal));
				}
			}
			
					
		} catch (Exception e1) {
			log.error(e1);
			return;
		}
				
		GraphOptionsDialog dialog = new GraphOptionsDialog(this.getDialog(), g);
		dialog.setVisible(true);
	}

}
