/**
 * 
 */
package me.openMap.models.query.sample;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import me.datamining.metric.AbstractSpatialMetric;
import me.datamining.metric.ServiceDateSample;
import me.transit.database.ServiceDate;

/**
 * @author meverline
 *
 */
public class ServiceSample extends DataSample {
	
	private List<WeekdayCheckBox> checkBoxs = new ArrayList<WeekdayCheckBox>();
	private JPanel panel = null;

	/* (non-Javadoc)
	 * @see me.openMap.models.query.sample.DataSample#getName()
	 */
	@Override
	public String toString() {
		return "Days of Service";
	}

	/* (non-Javadoc)
	 * @see me.openMap.models.query.sample.DataSample#getSampleType()
	 */
	@Override
	public AbstractSpatialMetric getSampleType() {
		ServiceDateSample sample = new ServiceDateSample();
		
		Set<ServiceDate.WeekDay> dayList = new HashSet<ServiceDate.WeekDay>();
		for (WeekdayCheckBox box : checkBoxs) {
			if ( box.isSelected() ) {
				dayList.add(box.getDay());
			}
		}
		sample.setDaysOfIntrest(dayList);
		return sample;
	}

	/* (non-Javadoc)
	 * @see me.openMap.models.query.sample.DataSample#hasUserInputs()
	 */
	@Override
	public boolean hasUserInputs() {
		return true;
	}
;
	/* (non-Javadoc)
	 * @see me.openMap.models.query.sample.DataSample#getUserInputs()
	 */
	@Override
	public JPanel getUserInputs() {
		if ( panel == null ) {
			
			panel = new JPanel();
			panel.setLayout(new GridLayout(4, 2));
			
			for ( ServiceDate.WeekDay day : ServiceDate.WeekDay.values())
			{
				WeekdayCheckBox box = new WeekdayCheckBox(day);
				box.setSelected(true);
				panel.add(box);
				checkBoxs.add(box);
			}
		}
		
		return panel;
	}
	
}
