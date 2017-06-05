/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.graph.GraphBuilder;
import browser.graph.GraphVizGraph;
import browser.graph.GraphVizNode;
import browser.gui.AppMainWindow;
import browser.gui.dialog.GraphOptionsDialog;
import browser.loader.ClassReflectionData;

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
		
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		AppMainWindow mainWin = getDialog().getMain();
		GraphBuilder builder = new GraphBuilder(this.getCurrentProject(), mainWin.getXref(), this.getDialog());
		builder.setKeepPackageName(this.getKeepPackageName());
		
		GraphVizGraph g = null;
		try {
			
			int depth = 0;
			HashSet<String> visted = new HashSet<String>();
			if ( ! getDialog().getSelected().isEmpty() ) {	
				String item = getDialog().getSelected().get(0);
				int ndx = item.lastIndexOf(".");
				g = new GraphVizGraph( builder.getGraphName(item.substring(ndx+1)));
				for ( String selected : getDialog().getSelected()) {
					ClassReflectionData theClass = mainWin.getLoader().load(selected);
		
					visted.add(theClass.getName());
					GraphVizNode root = new GraphVizNode( theClass.getName(), GraphVizNode.SHAPE.fromClass(theClass), 0);
					g.add(root);
					builder.addToClassInteractionGraph(mainWin.getXref().getUsesClasstRef(theClass), depth+1, g, root, visted);
				}
				
			} else {
				
				g = new GraphVizGraph(builder.getGraphName("ClassInteraction"));
				for ( String item : this.getListItems() ) {
					ClassReflectionData theClass = mainWin.getLoader().load(item);
					
					visted.add(theClass.getName());
					GraphVizNode root = new GraphVizNode( theClass.getName(), GraphVizNode.SHAPE.fromClass(theClass), 0);
					g.add(root);
					builder.addToClassInteractionGraph(mainWin.getXref().getUsesClasstRef(theClass), depth+1, g, root, visted);
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
