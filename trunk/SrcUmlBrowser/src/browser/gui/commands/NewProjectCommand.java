package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import browser.gui.AppMainWindow;
import browser.gui.dialog.NewProjectDialog;

public class NewProjectCommand implements ActionListener {

	private AppMainWindow window = null;
	
	public NewProjectCommand(AppMainWindow win)
	{
		window = win;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		NewProjectDialog dialog = new NewProjectDialog(this.window);
		dialog.setVisible(true);
	}

}
