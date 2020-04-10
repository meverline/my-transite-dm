package me.openMap.dialogs.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import me.factory.DaoBeanFactory;
import me.openMap.ApplicationSettings;
import me.openMap.models.layout.SpringLayoutUtilities;
import me.transit.dao.AgencyDao;
import me.transit.database.Agency;

public class AgencyColorTab implements SettingsParameterTab {

	/**
	 * 
	 */
	private HashMap<String,Color> colorMap = new HashMap<String,Color>();
	private JPanel panel = null;
	
	/**
	 * 
	 * @param parent
	 */
	public AgencyColorTab() 
	{}
	
	public void init(JDialog parent)
	{
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
	
		AgencyDao dao = AgencyDao.class.cast(DaoBeanFactory.create().getDaoBean(AgencyDao.class));
		List<Agency> agencys = dao.list();
		
		
		JPanel agencyPane = new JPanel();
		agencyPane.setLayout(new SpringLayout());
		
		agencyPane.add(new JLabel("Transite Agency"));
		agencyPane.add(new JLabel("Stop/Route Color"));
		
		for ( Agency item : agencys ) {
			
			// 1. UpperLeft Name
			agencyPane.add(new JLabel(item.getName()));
				
			JButton colorPane = new JButton();	
			colorPane.setBackground( ApplicationSettings.create().getAgencyColorByUrl(item.getUrl()));
			colorPane.setForeground( ApplicationSettings.create().getAgencyColorByUrl(item.getUrl()));
			colorPane.setBorderPainted(false);
			colorPane.setSize(20, 20);
			colorPane.addActionListener(new SetColorAction(parent, item.getUrl(), colorPane));
			agencyPane.add(colorPane);
		}
		
		SpringLayoutUtilities.makeCompactGrid(agencyPane,
				  agencys.size()+1, 2, //rows, cols
				  0, 0,        //initX, initY
				  3, 3);       //xPad, yPad
		
		panel.add(agencyPane, BorderLayout.CENTER);
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.openMap.dialogs.IColorParameterTab#getName()
	 */
	public String getName()
	{
		return "Agency Color";
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.openMap.dialogs.IColorParameterTab#getPanel()
	 */
	public JPanel getPanel()
	{
		return panel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.openMap.dialogs.IColorParameterTab#save()
	 */
	public void save()
	{
		for ( Entry<String, Color> entry : colorMap.entrySet()) {
			ApplicationSettings.create().setAgencyColorByUrl(entry.getKey(), entry.getValue());
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	private class SetColorAction implements ActionListener {
		
		private JDialog dialog_ = null;
		private String  url = null;
		private JButton  colorPanel  = null;
		
		public SetColorAction(JDialog dialog, String agencyUrl, JButton cpanel)
		{
			url = agencyUrl;
			dialog_ = dialog;
			colorPanel = cpanel;
		}

		public void actionPerformed(ActionEvent e) {
			
			Color bgColor = JColorChooser.showDialog(dialog_,
									                 "Choose Agency Color",
									                 colorPanel.getBackground());
			
			colorPanel.setBackground(bgColor);
			colorPanel.setForeground(bgColor);
			colorMap.put(url, bgColor);
		}
	}
	
}