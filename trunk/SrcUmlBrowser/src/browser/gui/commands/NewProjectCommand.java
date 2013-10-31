package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import browser.gui.AppMainWindow;
import browser.gui.dialog.EditProjectDialog;

public class NewProjectCommand implements ActionListener {

	private AppMainWindow window = null;
	
	public NewProjectCommand(AppMainWindow win)
	{
		window = win;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		EditProjectDialog dialog = new EditProjectDialog(this.window, null);
		dialog.setVisible(true);
	}

}
