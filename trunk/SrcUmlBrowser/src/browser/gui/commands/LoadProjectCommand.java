package browser.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import browser.gui.AppMainWindow;
import browser.util.ApplicationSettings;
import browser.util.Project;

public class LoadProjectCommand implements ActionListener {
	
	private AppMainWindow window = null;
	
	public LoadProjectCommand(AppMainWindow win)
	{
		window = win;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ApplicationSettings settings = ApplicationSettings.instance();
		JFileChooser chooser = new JFileChooser( settings.getFilepath());
		
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int result = chooser.showOpenDialog(this.window);
	    if(result == JFileChooser.APPROVE_OPTION ) {
	       File file = chooser.getSelectedFile();
	
	       Project project = settings.loadProject(file);
	       if ( project != null ) {
	    	   window.loadProject(project);
	       } else {
	    	   System.out.println("Project is null: " + file.getName());
	       }
	    }
	}

}
