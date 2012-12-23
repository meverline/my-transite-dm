/**
 * 
 */
package me.openMap.command;

import java.awt.event.ActionEvent;
import java.util.List;

import me.openMap.OpenTransitMap;

/**
 * @author markeverline
 *
 */
public class ExitCommand implements ICommand {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	@Override
	public void initilize(OpenTransitMap app) {
	}

	@Override
	public void paramenters(List<String> parameters) {
	}

}
