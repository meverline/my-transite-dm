/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import browser.graph.GraphBuilder;
import browser.graph.GraphVizGraph;
import browser.graph.GraphVizNode;
import browser.gui.AppMainWindow;
import browser.gui.dialog.GraphOptionsDialog;

/**
 * @author markeverline
 *
 */
public class PackageGraphCommand extends AbstractSearchActionCommand {
	
	
	private JTextField packageDepth = null;
	/**
	 * 
	 */
	public PackageGraphCommand() {
		super("Generate", "Package Referance");
	}
	
	/**
	 * 
	 */
	public int getWidth() {
		return 700;
	}
	
	/**
	 * 
	 */
	public JComponent getUI() {
		
		JPanel panel = new JPanel();
		
		panel.add( new JLabel("Package Max Depth:"));
		
		packageDepth = new JTextField();
		packageDepth.setColumns(3);
		panel.add(packageDepth);
		
		return panel;
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
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

		AppMainWindow mainWin = getDialog().getMain();

		GraphVizGraph g = null;
		int depth = 0;
		HashSet<String> visted = new HashSet<String>();

		GraphBuilder builder = new GraphBuilder(this.getCurrentProject(), mainWin.getXref(), this.getDialog());		
		if ( this.packageDepth.getText() != null && (! this.packageDepth.getText().isEmpty() ) ) {
			builder.setMaxPackageDepth( Integer.parseInt(this.packageDepth.getText()));
		}
		
		if ( ! getDialog().getSelected().isEmpty() ) {

			String item = getDialog().getSelected().get(0);
			int ndx = item.lastIndexOf(".");
			g = new GraphVizGraph( builder.getGraphName(item.substring(ndx+1)));
			
			for (String selected : getDialog().getSelected()) {
				String name = builder.stripPackage(selected);
				if ( ! visted.contains(name) ) {
				   GraphVizNode root = new GraphVizNode(name, GraphVizNode.SHAPE.folder, 0);
				   g.add(root);
				   visted.add(name);
				   builder.addToPackageGraph(mainWin.getXref().getPackRef(selected), depth + 1, g, root, visted);
				}
			}
		} else {
			g = new GraphVizGraph(builder.getGraphName("Package"));
			for (String selected : this.getListItems() ) {
				String name = builder.stripPackage(selected);
				if ( ! getDialog().filter(selected) && ( ! visted.contains(name)) ) {
			     	GraphVizNode root = new GraphVizNode(name, GraphVizNode.SHAPE.folder, 0);
			    	g.add(root);
				    visted.add(name);
				    builder.addToPackageGraph(mainWin.getXref().getPackRef(selected), depth + 1, g, root, visted);
				}
			}
		}
		GraphOptionsDialog dialog = new GraphOptionsDialog(this.getDialog(), g);
		dialog.setVisible(true);
	}

}
