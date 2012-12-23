package me.openMap.dialogs.tabs;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.text.NumberFormatter;

import me.openMap.ApplicationSettings;
import me.openMap.models.layout.SpringLayoutUtilities;

public class ClusterTab implements SettingsParameterTab {

	private JPanel panel = null;
	private JFormattedTextField confidence = null;
	private JFormattedTextField density = null;
	private JFormattedTextField hi = null;
	private JFormattedTextField low = null;
	
	public ClusterTab() 
	{
	}
	
	public void init(JDialog parent)
	{
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		ApplicationSettings settings = ApplicationSettings.create();
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new SpringLayout());
		
		dataPanel.add(new JLabel("Hi"));
		NumberFormatter format = new NumberFormatter();
	
		hi = new JFormattedTextField(format);
		hi.setColumns(2);
		hi.setValue( new Integer( settings.getClusterHiRange() ));
		dataPanel.add(hi);
		
		dataPanel.add(new JLabel("Low"));
		
		low = new JFormattedTextField(format);
		low.setColumns(2);
		low.setValue( new Integer( settings.getClusterLowRange()));
		dataPanel.add(low);
		
		dataPanel.add(new JLabel("Confidence"));
		
		confidence = new JFormattedTextField(format);
		confidence.setColumns(2);
		confidence.setValue( new Double( settings.getClusterConfidence() ));
		dataPanel.add(confidence);
		
		dataPanel.add(new JLabel("Density"));
		
		density = new JFormattedTextField(format);
		density.setColumns(2);
		density.setValue( new Double( settings.getClusterDensity() ));
		dataPanel.add(density);
		
		SpringLayoutUtilities.makeCompactGrid(dataPanel,
				  4, 2, //rows, cols
				  0, 0,        //initX, initY
				  3, 3);       //xPad, yPad
		
		panel.add(dataPanel, BorderLayout.NORTH);
	}
	
	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public String getName() {
		return "STING Cluster";
	}

	@Override
	public void save() {
		ApplicationSettings settings = ApplicationSettings.create();
		
		settings.setClusterConfidence(Double.class.cast(this.confidence.getValue()).doubleValue());
		settings.setClusterDensity(Double.class.cast(this.density.getValue()).doubleValue());
		settings.setClusterLowRange(Integer.class.cast(this.low.getValue()).intValue());
		settings.setClusterHiRange(Integer.class.cast(this.hi.getValue()).intValue());
	}

}
