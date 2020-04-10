/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JComponent;

import browser.gui.dialog.SearchDialog;
import browser.util.Project;

/**
 * @author markeverline
 *
 */
public interface SearchActionCommand extends ActionListener {
	
	
	public void setCurrentProject(Project project);
	/**
	 * 
	 * @return
	 */
	public String title();
	
	/**
	 * 
	 * @return
	 */
	public String actionButtonLabel();
	
	/**
	 * 
	 * @param dialog
	 */
	public void setDialog(SearchDialog dialog);
	
	/**
	 * 
	 */
	public Collection<String> getListItems();
	
	/**
	 * 
	 * @return
	 */
	public String itemsName();
	
	/**
	 * 
	 * @return
	 */
	public boolean isMultiSelect();
	
	
	/**
	 * 
	 * @return
	 */
	public JComponent getUI();
	
	/**
	 * 
	 * @return
	 */
	public int getWidth();

}
