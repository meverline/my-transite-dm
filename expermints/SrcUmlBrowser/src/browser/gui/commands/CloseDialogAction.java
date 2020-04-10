package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

public class CloseDialogAction implements ActionListener {
	
	private JDialog dialog = null; 
	
	/**
	 * 
	 * @param dialog
	 */
	public CloseDialogAction(JDialog dialog)
	{
		this.dialog = dialog;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		
		this.dialog.setVisible(false);
	}

}
