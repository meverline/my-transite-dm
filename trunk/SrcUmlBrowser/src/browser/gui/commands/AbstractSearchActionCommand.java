/**
 * 
 */
package browser.gui.commands;

import java.util.Collection;

import browser.gui.dialog.SearchDialog;
import browser.util.Project;

/**
 * @author markeverline
 *
 */
public abstract class AbstractSearchActionCommand implements
		SearchActionCommand {

	private String actionButton = null;
	private SearchDialog dialog = null;
	private String title = null;
	private Project project = null;
	
	protected AbstractSearchActionCommand(String buttonLabel, String dialogTitle)
	{
		this.title = dialogTitle;
		this.actionButton = buttonLabel;
	}
	
	/* (non-Javadoc)
	 * @see browser.gui.commands.setCurrentProject#setCurrentProject(Project project)
	 */
	public void setCurrentProject(Project project) {
		this.project = project;
	}
	
	/* (non-Javadoc)
	 * @see browser.gui.commands.SearchActionCommand#actionButtonLabel()
	 */
	public String actionButtonLabel() {
		return this.actionButton;
	}

	/* (non-Javadoc)
	 * @see browser.gui.commands.SearchActionCommand#setDialgo(browser.gui.dialog.SearchDialog)
	 */
	public void setDialog(SearchDialog dialog) {
		this.dialog = dialog;
	}

	/* (non-Javadoc)
	 * @see browser.gui.commands.SearchActionCommand#title()
	 */
	public String title() {
		return this.title;
	}

	/**
	 * @return the dialog
	 */
	public SearchDialog getDialog() {
		return dialog;
	}

	/**
	 * @param actionButton the actionButton to set
	 */
	public void setActionButton(String actionButton) {
		this.actionButton = actionButton;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/*
	 * (non-Javadoc)
	 * @see browser.gui.commands.SearchActionCommand#getListItems()
	 */
	public Collection<String> getListItems()
	{
		return getDialog().getMain().getLoader().getClassList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see browser.gui.commands.SearchActionCommand#itemsName()
	 */
	public String itemsName()
	{
		return "Class";
	}

	/* (non-Javadoc)
	 * @see browser.gui.commands.SearchActionCommand#isMultiSelect()
	 */
	public boolean isMultiSelect() {
		return true;
	}
	
	protected boolean showClass(String name)
	{		
		if (project.getPackageRoot() == null  ) {
			if ( name.startsWith("java") || name.startsWith("javax") ) {
				return false;
			}
			return true;
		}
		
		String allowed[] = project.getPackageRoot().split(";");
		
		for ( String packageRoot : allowed) {
			if ( name.startsWith(packageRoot) ) {
				return true;
			}
		}
		return false;
	}
	
}
