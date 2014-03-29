/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import browser.graph.GraphVizEdge;
import browser.graph.GraphVizGraph;
import browser.graph.GraphVizNode;
import browser.gui.AppMainWindow;
import browser.gui.dialog.GraphOptionsDialog;
import browser.util.ApplicationSettings;
import browser.util.ClassXRef;

/**
 * @author markeverline
 *
 */
public class PackageGraphCommand extends AbstractSearchActionCommand {
	
	/**
	 * 
	 */
	public PackageGraphCommand() {
		super("Generate", "Package Referance");
	}
	
	/*
	 * (non-Javadoc)
	 * @see browser.gui.commands.SearchActionCommand#getListItems()
	 */
	public Collection<String> getListItems()
	{
		Set<String> rtn = new HashSet<String>();
		
		for ( String item : getDialog().getMain().getLoader().getClassList()) {
			int last = item.lastIndexOf('.');
			rtn.add( item.substring(0, last));
		}
		return rtn;
	}
	
	/*
	 * (non-Javadoc)
	 * @see browser.gui.commands.SearchActionCommand#itemsName()
	 */
	public String itemsName()
	{
		return "Package";
	}
	
	/**
	 * 
	 * @param set
	 * @param depth
	 * @param g
	 * @param root
	 */
	private void addNodesToGraph(Set<ClassXRef.PackageAssociation> set, int depth, GraphVizGraph g, GraphVizNode root, HashSet<String> visted)
	{
		if ( set == null ) { return; }
		
		ApplicationSettings setting = ApplicationSettings.instance();
		AppMainWindow mainWin = getDialog().getMain();
		GraphVizGraph.DEPTH maxDepth = GraphVizGraph.DEPTH.valueOf(setting.getSettings().getGraphDepth());
	
		if ( depth > maxDepth.depth() ) { return; }
		
		for ( ClassXRef.PackageAssociation item : set ) {
			String name = getDialog().stripPackage(item.getPackageName());
			if ( this.showClass(item.getPackageName()) && ( ! getDialog().filter(item.getPackageName())) ) {
				GraphVizNode node = g.getNodeByName(name);
				if ( node == null ) {
					node = new GraphVizNode( name, GraphVizNode.SHAPE.folder, depth);
					g.add(node);
				}
				if ( item.isInheritance() && (! item.isRelation() ) ) {
   				   root.addEdge( new GraphVizEdge(node, GraphVizEdge.TYPE.normal));
				} else if ((! item.isInheritance()) && item.isRelation() ) {
				   node.addEdge(new GraphVizEdge(root, GraphVizEdge.TYPE.dot));
				} else if ( (! item.isInheritance()) && (! item.isRelation()) ) {
					root.addEdge( new GraphVizEdge(node, GraphVizEdge.TYPE.none));
				} else {
				   root.addEdge( new GraphVizEdge(node, GraphVizEdge.TYPE.normal, GraphVizGraph.COLOR.red));
				}
				if ( ! visted.contains(name)) {
					visted.add(name);
					this.addNodesToGraph(mainWin.getXref().getPackRef(item.getPackageName()), depth+1, g, node, visted);
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
		int depth = 0;
		HashSet<String> visted = new HashSet<String>();
		
		if ( ! getDialog().getSelected().isEmpty() ) {

			String item = getDialog().getSelected().get(0);
			int ndx = item.lastIndexOf(".");
			g = new GraphVizGraph( item.substring(ndx+1));
			
			for (String selected : getDialog().getSelected()) {
				String name = getDialog().stripPackage(selected);
				if ( ! visted.contains(name) ) {
				   GraphVizNode root = new GraphVizNode(name, GraphVizNode.SHAPE.folder, 0);
				   g.add(root);
				   visted.add(name);
				   this.addNodesToGraph(mainWin.getXref().getPackRef(selected), depth + 1, g, root, visted);
				}
			}
		} else {
			g = new GraphVizGraph("Package");
			for (String selected : this.getListItems() ) {
				String name = getDialog().stripPackage(selected);
				if ( ! getDialog().filter(selected) && ( ! visted.contains(name)) ) {
			     	GraphVizNode root = new GraphVizNode(name, GraphVizNode.SHAPE.folder, 0);
			    	g.add(root);
				    visted.add(name);
				    this.addNodesToGraph(mainWin.getXref().getPackRef(selected), depth + 1, g, root, visted);
				}
			}
		}
		GraphOptionsDialog dialog = new GraphOptionsDialog(this.getDialog(), g);
		dialog.setVisible(true);
	}

}