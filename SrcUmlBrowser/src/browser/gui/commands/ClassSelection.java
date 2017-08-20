/**
 * 
 */
package browser.gui.commands;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.gui.AppMainWindow;
import browser.gui.dialog.DisplayClassDialog;
import browser.gui.dialog.ExceptionDialog;
import browser.gui.models.NodeData;

/**
 * @author markeverline
 *
 */
public class ClassSelection implements TreeSelectionListener {

	private Log log = LogFactory.getLog(ClassSelection.class);
	private JTree aTree = null;
	private AppMainWindow mainWin = null;
	
	/**
	 * 
	 * @param source
	 */
	public ClassSelection(JTree source, AppMainWindow aLoader) {
		aTree = source;
		mainWin = aLoader;
	}
	
	/**
	 * @return the aTree
	 */
	public JTree getTree() {
		return aTree;
	}
	
	/**
	 * @param aTree the aTree to set
	 */
	public void setTree(JTree aTree) {
		this.aTree = aTree;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent event) {
		
		TreePath path = event.getPath();
			
		if ( getTree().getModel().isLeaf(path.getLastPathComponent() )) {
			DefaultMutableTreeNode node = 
				 DefaultMutableTreeNode.class.cast(path.getLastPathComponent());
			
			if ( node.getChildCount() == 0 &&  (! node.isRoot()))  {
				NodeData data = NodeData.class.cast(node.getUserObject());
				try {
					DisplayClassDialog dialog = 
						new DisplayClassDialog(mainWin,mainWin.getLoader().load(data.getPath()));
					dialog.setVisible(true);
				} catch (Exception e) {
					@SuppressWarnings("unused")
					ExceptionDialog ed = new ExceptionDialog(mainWin, e);
					log.error(e);
				}
			}

		}
	}

}
