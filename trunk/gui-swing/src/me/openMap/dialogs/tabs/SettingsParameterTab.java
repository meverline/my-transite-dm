package me.openMap.dialogs.tabs;

import javax.swing.JDialog;
import javax.swing.JPanel;

public interface SettingsParameterTab  {

	public void init(JDialog dialog);
	public JPanel getPanel();
	public String getName();
	public void save();
	
}
