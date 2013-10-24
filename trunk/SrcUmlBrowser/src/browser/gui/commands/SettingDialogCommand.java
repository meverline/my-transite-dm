/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import browser.gui.dialog.SettingsDialog;

/**
 * @author markeverline
 *
 */
public class SettingDialogCommand implements ActionListener {

	private JFrame frame = null;
	
	public SettingDialogCommand(JFrame frame)
	{
		this.frame = frame;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		SettingsDialog dialog = new SettingsDialog(this.frame);
		dialog.setVisible(true);
	}

}
