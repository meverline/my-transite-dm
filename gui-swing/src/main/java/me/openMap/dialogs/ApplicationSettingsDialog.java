/**
 * 
 */
package me.openMap.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import me.openMap.ApplicationSettings;
import me.openMap.dialogs.tabs.SettingsParameterTab;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author meverline
 *
 */
public class ApplicationSettingsDialog extends JDialog {

	/**
	 * 
	 */
	private Log log = LogFactory.getLog(ApplicationSettingsDialog.class);
	private static final long serialVersionUID = 1886097313093400990L;
	private JTabbedPane tabbedPane_;
	private List<SettingsParameterTab> tabs = new ArrayList<SettingsParameterTab>();
	
	/**
	 * 
	 * @param f
	 * @param tabParmeters
	 */
	public ApplicationSettingsDialog(Frame f, List<String> tabParmeters) 
	{
		super(f, true);
		
		this.setName("Color Gradiante");
		setSize(400, 250);
		build(tabParmeters);	
		setModalityType(ModalityType.DOCUMENT_MODAL);
		this.setLocationByPlatform(true);
		this.setLocationRelativeTo(f);
		this.setTitle("Application Settings");
		this.setVisible(true);
		
	}
	
	/**
	 * 
	 * @param tabParmeters
	 */
	public void build(List<String> tabParmeters)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		tabbedPane_ = new JTabbedPane();
		panel.add(tabbedPane_, BorderLayout.CENTER);
		
		for ( String className : tabParmeters ) {
			try {
				Class<?> cls = Class.forName(className);
				SettingsParameterTab tab = SettingsParameterTab.class.cast(cls.newInstance());
				
				tab.init(this);
				tabbedPane_.addTab(tab.getName(), tab.getPanel());
				this.tabs.add(tab);
			} catch (Exception e) {
				log.error(e);
			}
		}
				
		JPanel buttons = new JPanel();
		
		JButton save = new JButton("Apply");
		save.addActionListener( new SaveAction());
		save.addActionListener( new CloseAction(this));
		buttons.add(save);
		
		JButton close = new JButton("Close");
		close.addActionListener( new CloseAction(this));
		buttons.add(close);
		
		panel.add(buttons, BorderLayout.SOUTH);
		
		this.setContentPane(panel);	
		
	}
	
	/**
	 * 
	 * @param f
	 * @param tabParmeters
	 */
	public static void show(Frame f, List<String> tabParmeters)
	{
	    new ApplicationSettingsDialog(f, tabParmeters);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	private class SaveAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			for ( SettingsParameterTab entry : tabs ) {
				entry.save();
			}
			
			ApplicationSettings.create().save();
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	private class CloseAction implements ActionListener {
		
		private JDialog dialog_ = null;
		
		public CloseAction(JDialog dialog)
		{
			dialog_ = dialog;
		}

		public void actionPerformed(ActionEvent e) {
			dialog_.setVisible(false);
		}
	}
}
