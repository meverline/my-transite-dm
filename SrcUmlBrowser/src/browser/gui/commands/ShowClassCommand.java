/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.gui.AppMainWindow;
import browser.gui.dialog.DisplayClassDialog;
import browser.gui.dialog.ExceptionDialog;

/**
 * @author markeverline
 *
 */
public class ShowClassCommand extends AbstractSearchActionCommand {

	private Log log = LogFactory.getLog(ClassSelection.class);
	
	public ShowClassCommand() {
		super("Show", "Find Class");
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		
		String selected = getDialog().getSelected().get(0);
		AppMainWindow mainWin = getDialog().getMain();
		
		DisplayClassDialog dialog;
		try {
			dialog = new DisplayClassDialog(mainWin,mainWin.getLoader().load(selected));
			dialog.setVisible(true);
		} catch (Exception e) {
			log.error(e);
			@SuppressWarnings("unused")
			ExceptionDialog ed = new ExceptionDialog(getDialog(), e);
		}
		
	}

	/* (non-Javadoc)
	 * @see browser.gui.commands.AbstractSearchActionCommand#isMultiSelect()
	 */
	@Override
	public boolean isMultiSelect() {
		return false;
	}
	
	

}
