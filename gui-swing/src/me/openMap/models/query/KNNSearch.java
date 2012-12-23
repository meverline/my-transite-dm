package me.openMap.models.query;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import me.factory.DaoBeanFactory;
import me.openMap.OpenTransitMap;
import me.openMap.handlers.DataDisplayHandler;
import me.openMap.handlers.TransitStopDataHandler;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.Agency;
import me.transit.database.TransitStop;
import me.utils.AddressToCoordinate;
import me.utils.TransiteEnums;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.Point;


public class KNNSearch extends AbstractSearchParameters {

	private static final long serialVersionUID = 1L;
	private static final String SEPERATOR = ";";
	private static Log log = LogFactory.getLog(KNNSearch.class);
		
	private JFormattedTextField number_;
	private JTextField street_;
	private JTextField city_;
	private JComboBox state_;
	private JComboBox units_;
	private JFormattedTextField zipCode_;
	
	private JList addressList_;
	private DefaultListModel model_;
	private JFormattedTextField distance_;
	private JComboBox agencyBox = null;

	/**
	 * 
	 * @param map
	 */
	public KNNSearch() {
		setLayout(new BorderLayout());
		
	}
	
	@Override
	public void initilize(OpenTransitMap map, List<String> sampleList) {
		build(map);
	}
	

	/* (non-Javadoc)
	 * @see me.openMap.models.query.ISearchParameters#getName()
	 */
	public String getName() { return "Circle"; }

	/* (non-Javadoc)
	 * @see me.openMap.models.query.ISearchParameters#getResultType()
	 */
	@Override
	public String getResultType() { return "Stops"; }
	
	/**
	 * 
	 * @param map
	 */
	private void build(OpenTransitMap map) {

		setLayout(new BorderLayout());
		JPanel fields = new JPanel();
	
		fields.setLayout(new GridLayout(7, 2));
		
		agencyBox = this.addAgencyComboxBox(fields);

		// 1. Distance
		fields.add(new JLabel("Distance (mi)"));	
		
		JPanel inputs = new JPanel();
		inputs.setLayout(new GridLayout(1, 2));
		
		distance_ = new JFormattedTextField();
		distance_.setValue( new Double(0.5));
		inputs.add(distance_);
		
		units_ = new JComboBox( TransiteEnums.DistanceUnitType.values());
		inputs.add(units_);
		fields.add(inputs);
		
		// 2. Number
		fields.add(new JLabel("Number"));
		number_ = new JFormattedTextField();
		number_.setValue( new Integer(0));
		number_.setEditable(true);
		fields.add(number_);

		// 3. Street
		fields.add(new JLabel("Street"));
		street_ = new JTextField();
		street_.setEditable(true);
		fields.add(street_);
		
		// 4. City
		fields.add(new JLabel("City"));
		city_ = new JTextField();
		city_.setEditable(true);
		fields.add(city_);
		
		// 5. State
		
		String [] twoLetterState = { "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DC",
									 "DE", "FL", "GA", "HI", "ID", "IL", "IN",
									 "IA", "KS", "KY", "LA",
									 "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT",
									 "NE", "NV", "NH", "NM", "NY", "NC", "ND",
									 "OH", "OK", "OR", "PA", "RI", "SC", "SD",
									 "TN", "TX", "UT", "VT", "VA",
									 "WA", "WV", "WI", "WY",
									 "AS", "FM", "GU", "MH", "MP", "PW", "PR", "VI"
						
		};
		
		fields.add(new JLabel("State"));
		state_ = new JComboBox(twoLetterState);
		state_.setSelectedItem("VA");
		fields.add(state_);
		
		// 6. Zip
		fields.add(new JLabel("Zip"));
		zipCode_ = new JFormattedTextField();
		zipCode_.setValue( new Integer(0));
		zipCode_.setEditable(true);
		fields.add(zipCode_);
		
		add(fields,BorderLayout.NORTH);
			
		JPanel outterFields = new JPanel();
		outterFields.setLayout(new BorderLayout());

		JPanel buttonPannel = new JPanel();
	
		JButton button = new JButton("Add Address");
		button.addActionListener(new AddAddress());
		buttonPannel.add(button);
		
		button = new JButton("Remove Address");
		button.addActionListener(new RemoveAddressSelection());
		buttonPannel.add(button);

		outterFields.add(buttonPannel, BorderLayout.NORTH);
		addressList_ = new JList();
		model_ = new DefaultListModel();
		addressList_.setModel(model_ );
		
		outterFields.add( new JScrollPane(addressList_), BorderLayout.CENTER);
		add(outterFields, BorderLayout.CENTER);
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.models.query.ISearchParameters#search()
	 */
	@Override
	public List<DataDisplayHandler> search() {
				
		TransiteEnums.DistanceUnitType unit = 
			TransiteEnums.DistanceUnitType.class.cast(this.units_.getSelectedItem());

		double distanceInMeters = unit.toMeters(Double.class.cast(this.distance_.getValue()).doubleValue());

		TransiteStopDao dao =
			TransiteStopDao.class.cast(DaoBeanFactory.create().getDaoBean(TransiteStopDao.class));

		AddressToCoordinate coder = new AddressToCoordinate();
		StopQueryConstraint query = new StopQueryConstraint();	

		List<TransitStop> result = new ArrayList<TransitStop>();
		for ( int ndx = 0; ndx < model_.size(); ndx++) {
			
			String data[] = String.class.cast(model_.getElementAt(ndx)).split(KNNSearch.SEPERATOR);
			
			Point location = coder.geoCode(Integer.parseInt(data[0]), 
										   data[1], 
										   data[2], 
										   data[3],
										   Integer.parseInt(data[4]));

			if (location != null ) {
				
				query.addCircleConstriant(location, distanceInMeters);
				
				for ( Object obj : this.agencyBox.getSelectedObjects()) {
					if ( obj instanceof Agency ) {
						query.addAgency( Agency.class.cast(obj));
					}
				}
				
				List<TransitStop> rtn = dao.query(query);
				
				KNNSearch.log.info("search: " + rtn.size());
				if ( ! rtn.isEmpty() ) {
					result.addAll(rtn);
				}
				query.clear();
			}
		}
		
		List<DataDisplayHandler> rtn = new ArrayList<DataDisplayHandler>();
		rtn.add(new TransitStopDataHandler(result));
		return rtn;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	private class AddAddress implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			StringBuilder builder = new StringBuilder();
			
			builder.append(number_.getValue());
			builder.append(KNNSearch.SEPERATOR);
			builder.append(street_.getText());
			builder.append(KNNSearch.SEPERATOR);
			builder.append(city_.getText());
			builder.append(KNNSearch.SEPERATOR);
			builder.append( state_.getSelectedItem().toString());
			builder.append(KNNSearch.SEPERATOR);
			builder.append(zipCode_.getValue());
			
			model_.addElement(builder.toString());	
		}
		
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	private class RemoveAddressSelection implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			model_.remove(addressList_.getSelectedIndex());
		}

	}


}
