/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.graph.GraphVizEdge;
import browser.graph.GraphVizGraph;
import browser.graph.GraphVizNode;
import browser.gui.AppMainWindow;
import browser.gui.dialog.GraphOptionsDialog;
import browser.loader.ClassReflectionData;
import browser.util.ApplicationSettings;

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

	/**
	 * 
	 * @param set
	 * @param depth
	 * @param g
	 * @param root
	 */
	private void addNodesToGraph(Collection<ClassReflectionData> set, int depth, GraphVizGraph g, GraphVizNode root, HashSet<String> visted, boolean invert)
	{
		if ( set == null ) { return; }
		
		ApplicationSettings settings = ApplicationSettings.instance();
		AppMainWindow mainWin = getDialog().getMain();
		GraphVizGraph.DEPTH maxDepth = GraphVizGraph.DEPTH.valueOf(settings.getSettings().getGraphDepth());
	
		if ( depth > maxDepth.depth() ) { return; }
		
		for ( ClassReflectionData item : set ) {
			if ( this.showClass(item.getName())) {
				String name = getDialog().stripPackage(item.getName());
				GraphVizNode node = g.getNodeByName(name);
				if ( node == null ) {
					node = new GraphVizNode( name, GraphVizNode.SHAPE.fromClass(item), depth);
					g.add(node);
				}
				if ( invert ) {
					node.addEdge( new GraphVizEdge(root, GraphVizEdge.TYPE.normal));
				} else {
					node.addEdge( new GraphVizEdge(root, GraphVizEdge.TYPE.normal));
				}
				if ( ! visted.contains(item.getName())) {
					visted.add(item.getName());
			       this.addNodesToGraph(mainWin.getXref().getInheritRef(item), depth+1, g, node, visted, invert);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		String selected = getDialog().getSelected().get(0);
		AppMainWindow mainWin = getDialog().getMain();
		
		HashSet<String> visted = new HashSet<String>();
		GraphVizGraph g = null;
		try {
			ClassReflectionData theClass = mainWin.getLoader().load(selected);
			
			String item = getDialog().getSelected().get(0);
			int ndx = item.lastIndexOf(".");
			g = new GraphVizGraph( item.substring(ndx+1));
			
			String name = getDialog().stripPackage(theClass.getName());
			GraphVizNode root = new GraphVizNode( name, GraphVizNode.SHAPE.fromClass(theClass), 0);
			g.add(root);
					
			int depth = 0;
			this.addNodesToGraph(mainWin.getXref().getInheritRef(theClass), depth+1, g, root, visted, false);
			
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
					this.addNodesToGraph(aList, depth+1, g, root, visted, true);	
				}

			}
			
			if ( theClass.getSuperClass() != null ) {
				ClassReflectionData data = mainWin.getLoader().load(theClass.getSuperClass());
				name = getDialog().stripPackage(theClass.getSuperClass());
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
