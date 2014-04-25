/**
 * 
 */
package me.openMap.models.query.sample;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import me.datamining.metric.AbstractSpatialMetric;
import me.datamining.metric.ServiceFrequnceAtStop;
import me.openMap.models.layout.SpringLayoutUtilities;
import me.transit.database.ServiceDate;

/**
 * @author meverline
 *
 */
public class FrequanceOfService extends DataSample {

	private List<WeekdayCheckBox> checkBoxs = new ArrayList<WeekdayCheckBox>();
	private JComboBox startHour = null;
	private JComboBox startMin = null;
	private JComboBox startAmPm = null;
	private JComboBox endHour = null;
	private JComboBox endMin = null;
	private JComboBox endAmPm = null;
	private JPanel panel = null;
	
	/* (non-Javadoc)
	 * @see me.openMap.models.query.sample.DataSample#getSampleType()
	 */
	@Override
	public AbstractSpatialMetric getSampleType() {
		
		ServiceFrequnceAtStop sample = new ServiceFrequnceAtStop();
		if ( startHour != null ) {
			int shour = Integer.class.cast(startHour.getSelectedItem()).intValue();
			String ampm = String.class.cast(startAmPm.getSelectedItem());
			if ( ampm.compareTo("PM") == 0) {
				shour += 12;
			}
			
			StringBuilder time = new StringBuilder();
			time.append(shour);
			time.append(Integer.class.cast(startMin.getSelectedItem()).intValue());
			
			sample.setStartTime( Long.parseLong(time.toString()));
			
			int ehour = Integer.class.cast(endHour.getSelectedItem()).intValue();
			ampm = String.class.cast(endAmPm.getSelectedItem());
			if ( ampm.compareTo("PM") == 0) {
				ehour += 12;
			}
			
			time = new StringBuilder();
			time.append(ehour);
			time.append(Integer.class.cast(endMin.getSelectedItem()).intValue());
			
			sample.setEndTime( Long.parseLong(time.toString()));
		}	
			Set<ServiceDate.WeekDay> dayList = new HashSet<ServiceDate.WeekDay>();
			for (WeekdayCheckBox box : checkBoxs) {
				if ( box.isSelected() ) {
					dayList.add(box.getDay());
				}
			}
		
		return sample;
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.models.query.sample.DataSample#getName()
	 */
	@Override
	public String toString() {
		return "Frequency of Service";
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.models.query.sample.DataSample#hasUserInputs()
	 */
	@Override
	public boolean hasUserInputs() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.models.query.sample.DataSample#getUserInputs()
	 */
	@Override
	public JPanel getUserInputs() {
		if ( panel == null ) {
			
			panel = new JPanel();
			panel.setLayout(new SpringLayout());
			
			JPanel timePanel = new JPanel();
			panel.add(timePanel);
			timePanel.setLayout(new SpringLayout());
			
			Integer hour[] = { 0,1,2,3,4,5,6,7,8,9,10,11,12 };
			Integer min[] = { 0,15,30,45,59 };
			String  ampm[] = { "AM", "PM" };
			
			timePanel.add( new JLabel("Start Time (HH:MM)"));
			startHour = new JComboBox(hour);
			timePanel.add(startHour);
			
			startMin = new JComboBox(min);
			timePanel.add(startMin);
			
			startAmPm = new JComboBox(ampm);
			timePanel.add(startAmPm);
			
			timePanel.add( new JLabel("End Time (HH:MM)"));
			endHour = new JComboBox(hour);
			endHour.setSelectedItem(hour[hour.length-1]);
			timePanel.add(endHour);
	
			endMin = new JComboBox(min);
			endMin.setSelectedItem(min[min.length-1]);
			timePanel.add(endMin);
			
			endAmPm = new JComboBox(ampm);
			endAmPm.setSelectedItem(ampm[ampm.length-1]);
			timePanel.add(endAmPm);
			
			
			SpringLayoutUtilities.makeCompactGrid(timePanel,
					  								2, 4, //rows, cols
					  								0, 0,        //initX, initY
					  								6, 6);       //xPad, yPad
			
			JPanel dayPanel = new JPanel();
			panel.add(dayPanel);
			dayPanel.setLayout(new GridLayout(4, 2));
			
			for ( ServiceDate.WeekDay day : ServiceDate.WeekDay.values())
			{
				WeekdayCheckBox box = new WeekdayCheckBox(day);
				box.setSelected(true);
				dayPanel.add(box);
				checkBoxs.add(box);
			}
			
			
			
			SpringLayoutUtilities.makeCompactGrid(panel,
						2, 1, //rows, cols
						0, 0,        //initX, initY
						6, 6);       //xPad, yPad
		}
		
		return panel;
	}
	

}
