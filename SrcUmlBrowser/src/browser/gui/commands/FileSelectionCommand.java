/**
 * 
 */
package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

/**
 * @author markeverline
 *
 */
public class FileSelectionCommand implements ActionListener {
	
	private JTextField fieldToSet = null;
	private JDialog dialog = null;
	private boolean dirOnly = false;
	private ActionListener copy = null;
	
	public FileSelectionCommand(JTextField field, JDialog dialog, boolean dirOnly)
	{
		fieldToSet = field;
		this.dialog = dialog;
		this.dirOnly = dirOnly;
	}
	
	public FileSelectionCommand(JTextField field, JDialog dialog, boolean dirOnly, ActionListener cp)
	{
		fieldToSet = field;
		this.dialog = dialog;
		this.dirOnly = dirOnly;
		this.copy = cp;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		
		JFileChooser chooser = new JFileChooser();
	
		if ( dirOnly ) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
	    if(chooser.showOpenDialog(this.dialog) == JFileChooser.APPROVE_OPTION) {
	       File file = chooser.getSelectedFile();
	       fieldToSet.setText( file.toString());
	       if ( this.copy != null ) {
	    	   this.copy.actionPerformed(arg0);
	       }
	    }
	}

}
