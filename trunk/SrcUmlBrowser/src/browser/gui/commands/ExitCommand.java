/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author markeverline
 *
 */
public class ExitCommand implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
	}

}
