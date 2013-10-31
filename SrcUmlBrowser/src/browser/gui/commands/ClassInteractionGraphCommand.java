/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.util.HashSet;
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
import browser.util.ClassXRef;

/**
 * @author markeverline
 *
 */
public class ClassInteractionGraphCommand extends AbstractSearchActionCommand {

	private Log log = LogFactory.getLog(InheritanceGraphCommand.class);

	/**
	 * 
	 */
	public ClassInteractionGraphCommand() {
		super("Generate", "Class Interaction");
	}
	
	private void addNodesToGraph(Set<ClassXRef.Association> set, int depth, GraphVizGraph g, GraphVizNode root, HashSet<String> visted)
	{
		if ( set == null ) { return; }
		
		ApplicationSettings settings = ApplicationSettings.instance();
		AppMainWindow mainWin = getDialog().getMain();
		GraphVizGraph.DEPTH maxDepth = GraphVizGraph.DEPTH.valueOf(settings.getSettings().getGraphDepth());
		
		if ( depth > maxDepth.depth() ) { return; }
		
		for ( ClassXRef.Association item : set ) {
			if ( this.showClass(item.toString() )) {
				GraphVizNode node = g.getNodeByName(item.toString());
				if ( node == null ) {
					node = new GraphVizNode( item.toString(), GraphVizNode.SHAPE.fromClass(item.getTheClass()), 0);
					g.add(node);
				}
				
				if ( item.isField() ) {
					root.addEdge( new GraphVizEdge(node, GraphVizEdge.TYPE.none));
				} else {
					root.addEdge( new GraphVizEdge(node, GraphVizEdge.TYPE.dot));
				}
				if ( ! visted.contains(item.toString()) ) {
					visted.add(item.toString());
					this.addNodesToGraph(mainWin.getXref().getUsesClasstRef(item.getTheClass()), 
																			depth+1, 
																			g, 
																			node, 
																			visted);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		AppMainWindow mainWin = getDialog().getMain();
		
		GraphVizGraph g = null;
		try {
			
			int depth = 0;
			HashSet<String> visted = new HashSet<String>();
			if ( ! getDialog().getSelected().isEmpty() ) {	
				String item = getDialog().getSelected().get(0);
				int ndx = item.lastIndexOf(".");
				g = new GraphVizGraph( item.substring(ndx+1));
				for ( String selected : getDialog().getSelected()) {
					ClassReflectionData theClass = mainWin.getLoader().load(selected);
		
					visted.add(theClass.getName());
					GraphVizNode root = new GraphVizNode( theClass.getName(), GraphVizNode.SHAPE.fromClass(theClass), 0);
					g.add(root);
					this.addNodesToGraph(mainWin.getXref().getUsesClasstRef(theClass), depth+1, g, root, visted);
				}
				
			} else {
				
				g = new GraphVizGraph("ClassInteraction");
				for ( String item : this.getListItems() ) {
					ClassReflectionData theClass = mainWin.getLoader().load(item);
					
					visted.add(theClass.getName());
					GraphVizNode root = new GraphVizNode( theClass.getName(), GraphVizNode.SHAPE.fromClass(theClass), 0);
					g.add(root);
					this.addNodesToGraph(mainWin.getXref().getUsesClasstRef(theClass), depth+1, g, root, visted);
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
