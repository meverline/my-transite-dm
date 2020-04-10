/**
 * 
 */
package me.openMap.dialogs.tabs;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.bandwidth.MesureOfSpread;
import me.datamining.bandwidth.ScottsRule;
import me.datamining.bandwidth.SlivermanRule;
import me.openMap.ApplicationSettings;
import me.openMap.models.layout.SpringLayoutUtilities;

/**
 * @author meverline
 *
 */
public class HeatMapTab implements SettingsParameterTab {

	private JPanel panel = null;
	private JComboBox<BandWidthType> xbandWidthBox = null;
	private JComboBox<BandWidthType> ybandWidthBox = null;
	private JComboBox<KernalType> kernalBox = null;
	
	public enum BandWidthType {
		SlivermanRule {
			public IBandwidth getBandWidth() {
				return new SlivermanRule();
			}
		},
		ScottsRule{
			public IBandwidth getBandWidth() {
				return new ScottsRule();
			}
		},
		MesureOfSpread{
			public IBandwidth getBandWidth() {
				return new MesureOfSpread();
			}
		};
		
		public abstract IBandwidth getBandWidth();
	}
	
	public enum KernalType {
		Biweight {
			public IDensityKernel getKernal() {
				return new me.datamining.Kernel.Biweight();
			}
		},
		Epanechnikov{
			public IDensityKernel getKernal() {
				return new me.datamining.Kernel.Epanechnikov();
			}
		},
		Gaussian{
			public IDensityKernel getKernal() {
				return new me.datamining.Kernel.Gaussian();
			}
		};
		
		public abstract IDensityKernel getKernal();
	}
	
	public HeatMapTab() 
	{
	}
		
	public void init(JDialog parent)
	{
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new SpringLayout());
		
		ApplicationSettings settings = ApplicationSettings.create();

		dataPanel.add(new JLabel("Kernal"));	
		this.kernalBox = new JComboBox<KernalType>(KernalType.values());
		if ( settings.getDenstiyKernal() != null ) {
		   this.kernalBox.setSelectedItem( KernalType.valueOf(settings.getDenstiyKernal().getClass().getSimpleName()));
		}
		dataPanel.add(this.kernalBox);
		
		dataPanel.add(new JLabel("X BandWidth"));	
		this.xbandWidthBox = new JComboBox<BandWidthType>(BandWidthType.values());
		if ( settings.getXbandWidth() != null ) {
		   this.xbandWidthBox.setSelectedItem( BandWidthType.valueOf(settings.getXbandWidth().getClass().getSimpleName()));
		}
		dataPanel.add(this.xbandWidthBox);
	
		dataPanel.add(new JLabel("Y BandWidth"));	
		this.ybandWidthBox = new JComboBox<BandWidthType>(BandWidthType.values());
		if ( settings.getYbandWidth() != null ) {
		    this.ybandWidthBox.setSelectedItem( BandWidthType.valueOf(settings.getYbandWidth().getClass().getSimpleName()));
		}
		dataPanel.add(this.ybandWidthBox);
		
		SpringLayoutUtilities.makeCompactGrid(dataPanel,
				  3, 2, //rows, cols
				  0, 0,        //initX, initY
				  3, 3);       //xPad, yPad
		
		panel.add(dataPanel, BorderLayout.NORTH);
				
	}
	/* (non-Javadoc)
	 * @see me.openMap.dialogs.tabs.SettingsParameterTab#getPanel()
	 */
	@Override
	public JPanel getPanel() {
		return panel;
	}

	/* (non-Javadoc)
	 * @see me.openMap.dialogs.tabs.SettingsParameterTab#getName()
	 */
	@Override
	public String getName() {
		return "HeatMap Parameters";
	}

	/* (non-Javadoc)
	 * @see me.openMap.dialogs.tabs.SettingsParameterTab#save()
	 */
	@Override
	public void save() {
		
		ApplicationSettings settings = ApplicationSettings.create();
		
		settings.setXbandWidth(BandWidthType.class.cast(this.xbandWidthBox.getSelectedItem()).getBandWidth());
		settings.setYbandWidth(BandWidthType.class.cast(this.ybandWidthBox.getSelectedItem()).getBandWidth());
		settings.setDenstiyKernal( KernalType.class.cast(this.kernalBox.getSelectedItem()).getKernal());
	}

}
