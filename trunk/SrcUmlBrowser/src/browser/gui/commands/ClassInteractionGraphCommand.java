/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.graph.Edge;
import browser.graph.Graph;
import browser.graph.Node;
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
	
	private void addNodesToGraph(Set<ClassXRef.Association> set, int depth, Graph g, Node root, HashSet<String> visted)
	{
		if ( set == null ) { return; }
		
		ApplicationSettings settings = ApplicationSettings.instance();
		AppMainWindow mainWin = getDialog().getMain();
		Graph.DEPTH maxDepth = Graph.DEPTH.valueOf(settings.getSettings().getGraphDepth());
		
		if ( depth > maxDepth.depth() ) { return; }
		
		for ( ClassXRef.Association item : set ) {
			if ( this.showClass(item.toString() )) {
				Node node = g.getNodeByName(item.toString());
				if ( node == null ) {
					node = new Node( item.toString(), Node.SHAPE.fromClass(item.getTheClass()), 0);
					g.add(node);
				}
				
				if ( item.isField() ) {
					root.addEdge( new Edge(node, Edge.TYPE.none));
				} else {
					root.addEdge( new Edge(node, Edge.TYPE.dot));
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
		
		Graph g = null;
		try {
			
			int depth = 0;
			HashSet<String> visted = new HashSet<String>();
			if ( ! getDialog().getSelected().isEmpty() ) {	
				String item = getDialog().getSelected().get(0);
				int ndx = item.lastIndexOf(".");
				g = new Graph( item.substring(ndx+1));
				for ( String selected : getDialog().getSelected()) {
					ClassReflectionData theClass = mainWin.getLoader().load(selected);
		
					visted.add(theClass.getName());
					Node root = new Node( theClass.getName(), Node.SHAPE.fromClass(theClass), 0);
					g.add(root);
					this.addNodesToGraph(mainWin.getXref().getUsesClasstRef(theClass), depth+1, g, root, visted);
				}
				
			} else {
				
				g = new Graph("ClassInteraction");
				for ( String item : this.getListItems() ) {
					ClassReflectionData theClass = mainWin.getLoader().load(item);
					
					visted.add(theClass.getName());
					Node root = new Node( theClass.getName(), Node.SHAPE.fromClass(theClass), 0);
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
