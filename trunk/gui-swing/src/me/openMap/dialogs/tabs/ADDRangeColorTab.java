/**
 * 
 */
package me.openMap.dialogs.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

/**
 * @author meverline
 *
 */
public class ADDRangeColorTab implements SettingsParameterTab {

	private JPanel panel = null;
	private JButton colorPane = null;
	private JFormattedTextField alphaValue;
	
	/**
	 * 
	 * @param parent
	 */
	public ADDRangeColorTab() 
	{
	}
	
	public void init(JDialog parent)
	{
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new SpringLayout());
		
		
		dataPanel.add(new JLabel("ADD Range Color"));
		
		colorPane = new JButton();	
		colorPane.setBackground( ApplicationSettings.create().getADDRangeColor());
		colorPane.setForeground( ApplicationSettings.create().getADDRangeColor());
		colorPane.setBorderPainted(false);
		colorPane.setSize(20, 20);
		colorPane.addActionListener(new SetColorAction(parent, colorPane));
		dataPanel.add(colorPane);
		
		dataPanel.add(new JLabel("Alpha Value"));
		
		NumberFormatter format = new NumberFormatter();
		format.setMinimum( new Integer(00));
		format.setMaximum(new Integer(255));
		
		alphaValue = new JFormattedTextField(format);
		alphaValue.setColumns(2);
		alphaValue.setValue( new Integer( ApplicationSettings.create().getADDAlphaValue() ));
		dataPanel.add(alphaValue);
		
		SpringLayoutUtilities.makeCompactGrid(dataPanel,
				  2, 2, //rows, cols
				  0, 0,        //initX, initY
				  3, 3);       //xPad, yPad
		
		panel.add(dataPanel, BorderLayout.NORTH);
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.dialogs.tabs.IColorParameterTab#getPanel()
	 */
	@Override
	public JPanel getPanel() {
		return panel;
	}

	/* (non-Javadoc)
	 * @see me.openMap.dialogs.tabs.IColorParameterTab#getName()
	 */
	@Override
	public String getName() {
		return "ADD Range Color";
	}

	/* (non-Javadoc)
	 * @see me.openMap.dialogs.tabs.IColorParameterTab#save()
	 */
	@Override
	public void save() {
		ApplicationSettings.create().setADDRangeColor(this.colorPane.getBackground());
		ApplicationSettings.create().setADDAlphaValue(Integer.class.cast(this.alphaValue.getValue()).intValue());
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
									                 "Choose ADD Color",
									                 colorPanel.getBackground());
			
			colorPanel.setBackground(bgColor);
			colorPanel.setForeground(bgColor);
		}
	}

}
