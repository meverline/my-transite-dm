package me.openMap.dialogs.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.text.NumberFormatter;

import me.openMap.ApplicationSettings;
import me.openMap.models.layout.SpringLayoutUtilities;
import me.utils.GradientParameters;

public class DataMiningColorsTab  implements SettingsParameterTab {

	/**
	 * 
	 */
	private List<Gradiante> colorSetsing = new ArrayList<Gradiante>();
	private JPanel panel = null;
	
	/**
	 * 
	 * @param parent
	 */
	public DataMiningColorsTab() 
	{
	}
	
	public void init(JDialog parent)
	{
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel dataArea = new JPanel();
		dataArea.setLayout(new SpringLayout());
		
		JLabel lable = new JLabel("Start Color");
		
		dataArea.add(lable);
		
		lable = new JLabel("End Color");
		dataArea.add(lable);
		
		lable = new JLabel("Start%");
		dataArea.add(lable);
		
		lable = new JLabel("Range");
		dataArea.add(lable);
		
		lable = new JLabel("Steps");
		dataArea.add(lable);
		
		lable = new JLabel("Alpha");
		dataArea.add(lable);
		
		colorSetsing.add(new Gradiante(parent,dataArea, Color.decode("#A0A0FF"), Color.BLUE, 0.1, 29.9, 15, 95));
		colorSetsing.add(new Gradiante(parent,dataArea,Color.BLUE, Color.GREEN, 30, 29.9,10, 95));
		colorSetsing.add(new Gradiante(parent,dataArea,Color.GREEN, Color.YELLOW, 60.0,19.9, 10, 95));
		colorSetsing.add(new Gradiante(parent,dataArea,Color.YELLOW, Color.decode("#FF0000"), 80.0, 9.9, 10, 95));
		colorSetsing.add(new Gradiante(parent,dataArea,Color.decode("#FF0000"), Color.RED,90.0, 10.0, 10, 95));
		
		SpringLayoutUtilities.makeCompactGrid(dataArea,
				  6, 6, //rows, cols
				  0, 0,        //initX, initY
				  3, 3);       //xPad, yPad
		
		panel.add(dataArea, BorderLayout.CENTER);
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.openMap.dialogs.IColorParameterTab#getName()
	 */
	public String getName()
	{
		return "Color Gradiante";
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
		List<GradientParameters> parms = new ArrayList<GradientParameters>();
		for ( Gradiante entry : colorSetsing ) {
			parms.add( entry.getParameters());
		}
		ApplicationSettings.create().setGradParms(parms);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	private class Gradiante {
		
		private JButton startColor;
		private JButton endColor;
		private JFormattedTextField startPercential;
		private JFormattedTextField range;
		private JFormattedTextField steps;
		private JFormattedTextField alphaValue;	
		
		/**
		 * 
		 * @param dialog
		 * @param panel
		 * @param startColor
		 * @param endColor
		 * @param startPercential
		 * @param range
		 * @param steps
		 * @param alphaValue
		 */
		public Gradiante(JDialog dialog, JPanel panel, Color startColor, Color endColor, double startPercential,
			  			 double range, int steps, int alphaValue)
		{
			this.startColor = new JButton();
			this.startColor.setBackground( startColor);
			this.startColor.setForeground( startColor);
			this.startColor.setBorderPainted(false);
			this.startColor.setSize(20, 20);
			this.startColor.addActionListener( new SetColorAction(dialog, this.startColor));
			panel.add(this.startColor);
			
			this.endColor = new JButton();
			this.endColor.setBackground( endColor);
			this.endColor.setForeground( endColor);
			this.endColor.setBorderPainted(false);
			this.endColor.setSize(20, 20);
			this.endColor.addActionListener( new SetColorAction(dialog, this.endColor));
			panel.add(this.endColor);
			
			NumberFormatter format = new NumberFormatter();
			this.startPercential = new JFormattedTextField(format);
			this.startPercential.setColumns(2);
			this.startPercential.setValue( new Double(startPercential));
			panel.add(this.startPercential);
			
			this.range = new JFormattedTextField(format);
			this.range.setColumns(2);
			this.range.setValue( new Double(range));
			panel.add(this.range);
			
			this.steps = new JFormattedTextField(format);
			this.steps.setColumns(3);
			this.steps.setValue( new Integer(steps));
			panel.add(this.steps);
			
			this.alphaValue = new JFormattedTextField(format);
			this.alphaValue.setColumns(3);
			this.alphaValue.setValue( new Integer(alphaValue));
			panel.add(this.alphaValue);
			
		}
		
		/**
		 * 
		 * @return
		 */
		public GradientParameters getParameters()
		{
			return new GradientParameters(this.startColor.getBackground(), 
										  this.endColor.getBackground(), 
										  Double.class.cast(this.startPercential.getValue()).doubleValue(), 
										  Double.class.cast(this.range.getValue()).doubleValue(), 
										  Integer.class.cast(this.steps.getValue()).intValue(), 
										  Integer.class.cast(this.alphaValue.getValue()).intValue());
		}
		
		//////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////

		private class SetColorAction implements ActionListener {
			
			private JDialog dialog_ = null;
			private JButton  colorPanel  = null;
			
			public SetColorAction(JDialog dialog, JButton cpanel)
			{
				dialog_ = dialog;
				colorPanel = cpanel;
			}

			public void actionPerformed(ActionEvent e) {
				
				Color bgColor = JColorChooser.showDialog(dialog_,
										                 "Choose Color",
										                 colorPanel.getBackground());
				
				colorPanel.setBackground(bgColor);
				colorPanel.setForeground(bgColor);
			}
		}
		
	}
	
}