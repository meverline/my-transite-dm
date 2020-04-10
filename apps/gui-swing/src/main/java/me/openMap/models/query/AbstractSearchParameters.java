//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.openMap.models.query;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import me.factory.DaoBeanFactory;
import me.openMap.OpenTransitMap;
import me.openMap.mapUtils.RegionSelector;
import me.transit.dao.AgencyDao;
import me.transit.database.Agency;


public abstract class AbstractSearchParameters extends JPanel implements ISearchParameters {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String METERS = "m";
	public static final String KILOMETER = "km";
	public static final String FT = "ft";
	public static final String MILE = "mi";

	private JFormattedTextField startDateMon_;
	private JFormattedTextField startDateDay_;
	private JFormattedTextField startDateYear_;

	private JFormattedTextField endDateMon_;
	private JFormattedTextField endDateDay_;
	private JFormattedTextField endDateYear_;
	
	public double milesToMeters(double distance)
	{
		return distance*1609.344;
	}

	protected boolean hasStartDate()
	{
		if ( startDateMon_.getText().length() >  0) {
			return true;
		}
		return false;
	}

	protected Date getStartDate() {

		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.MONTH, Integer.parseInt(startDateMon_.getText()));
		cal.set(Calendar.DAY_OF_MONTH, Integer
				.parseInt(startDateDay_.getText()));

		int year = Integer.parseInt(startDateYear_.getText());
		if ( year < 10 ) {
			year += 2000;
		} else {
			year += 1900;
		}

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}

	protected boolean hasEndDate()
	{
		if ( endDateMon_.getText().length() >  0) {
			return true;
		}
		return false;
	}

	protected Date getEndDate() {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.MONTH, Integer.parseInt(endDateMon_.getText()));
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(endDateDay_.getText()));

		int year = Integer.parseInt(startDateYear_.getText());
		if ( year < 10 ) {
			year += 2000;
		} else {
			year += 1900;
		}

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		return cal.getTime();
	}

	protected JPanel getStartDatePanel()
	{
		JPanel date = new JPanel();

		NumberFormatter format = new NumberFormatter();
		format.setMinimum( new Integer(1));
		format.setMaximum(new Integer(12));

		startDateMon_ = new JFormattedTextField(format);
		startDateMon_.setColumns(2);
		date.add(startDateMon_);
		date.add(new JLabel("/"));

		format = new NumberFormatter();
		format.setMinimum( new Integer(1));
		format.setMaximum(new Integer(31));

		startDateDay_ = new JFormattedTextField(format);
		startDateDay_.setColumns(2);
		date.add(startDateDay_);
		date.add(new JLabel("/"));

		format = new NumberFormatter();
		format.setMinimum( new Integer(2));
		format.setMaximum(new Integer(9));

		startDateYear_ = new JFormattedTextField(format);
		startDateYear_.setColumns(2);
		date.add(startDateYear_);

		return date;
	}
	
	protected JPanel getEndDatePanel()
	{
		JPanel date = new JPanel();

		NumberFormatter format = new NumberFormatter();
		format.setMinimum( new Integer(1));
		format.setMaximum(new Integer(12));

		format = new NumberFormatter();
		format.setMinimum( new Integer(1));
		format.setMaximum(new Integer(12));

		endDateMon_ = new JFormattedTextField(format);
		endDateMon_.setColumns(2);
		date.add(endDateMon_);
		date.add(new JLabel("/"));

		format = new NumberFormatter();
		format.setMinimum( new Integer(1));
		format.setMaximum(new Integer(31));

		endDateDay_ = new JFormattedTextField(format);
		endDateDay_.setColumns(2);
		date.add(endDateDay_);
		date.add(new JLabel("/"));

		format = new NumberFormatter();
		format.setMinimum( new Integer(00));
		format.setMaximum(new Integer(99));

		endDateYear_ = new JFormattedTextField(format);
		endDateYear_.setColumns(2);
		date.add(endDateYear_);

		return date;
	}
	
	protected JComboBox<Agency> addAgencyComboxBox(JPanel panel)
	{
	
		panel.add( new JLabel("Agencys"));
		
		List<Agency> aList = new ArrayList<Agency>();
		aList.add(new Agency("None"));
		
		AgencyDao dao =
			AgencyDao.class.cast(DaoBeanFactory.create().getDaoBean(AgencyDao.class));

		aList.addAll(dao.list());
		
		Agency array[] = new Agency[dao.list().size()];
		
		JComboBox<Agency> box = new JComboBox<Agency>( aList.toArray(array));
		panel.add(box);
		
		return box;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	public class SelectRegionCommand implements ActionListener {

		OpenTransitMap mainWindow_ = null;
		RegionSelector selector_ = null;
		RegionSelector.RegionSelectListener search_ = null;

		public SelectRegionCommand(OpenTransitMap g, RegionSelector.RegionSelectListener search)
		{
			mainWindow_  = g;
			selector_ = new RegionSelector();
			search_ = search;
		}

		public void actionPerformed(ActionEvent e) {

			selector_.startSelect(mainWindow_.getMap().getMapKit(), search_);
		}

	}

}
