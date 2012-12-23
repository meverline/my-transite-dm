package me.openMap.command;

import java.awt.event.ActionEvent;
import java.util.List;

import me.openMap.OpenTransitMap;
import me.openMap.dialogs.ApplicationSettingsDialog;

public class ApplicationSettingsCommand implements ICommand{

	private OpenTransitMap top_ = null;
	private List<String> tabParmeters = null;

	/**
	 * 
	 * @param top
	 */
	public ApplicationSettingsCommand()
	{
	}
	
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		ApplicationSettingsDialog.show(top_, tabParmeters);
	}



	@Override
	public void initilize(OpenTransitMap app) {
		top_  = app;
	}
	
	@Override
	public void paramenters(List<String> parameters) {
		this.tabParmeters = parameters;
	}


}
