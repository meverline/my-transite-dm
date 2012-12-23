package me.openMap.models.query;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.text.NumberFormatter;

import me.factory.DaoBeanFactory;
import me.openMap.OpenTransitMap;
import me.openMap.handlers.DataDisplayHandler;
import me.openMap.handlers.TransitStopDataHandler;
import me.openMap.mapUtils.RegionSelector;
import me.openMap.models.layout.SpringLayoutUtilities;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.Agency;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;



public class RegionalMapSearch extends AbstractSearchParameters 
	implements RegionSelector.RegionSelectListener 
{

	private static final long serialVersionUID = 1L;

	private JFormattedTextField  ullat_;
	private JFormattedTextField  ullon_;

	private JFormattedTextField  lrlat_;
	private JFormattedTextField  lrlon_;
	private JComboBox agencyBox = null;

	private OpenTransitMap mainWindow_ = null;
	private GeometryFactory factory_  = new GeometryFactory();
	private Logger logger = Logger.getLogger(RegionalMapSearch.class.getSimpleName());

	/**
	 * 
	 */
	public RegionalMapSearch() {
		setLayout(new BorderLayout());
	}
	
	@Override
	public void initilize(OpenTransitMap map, List<String> sampleList) {
		mainWindow_ = map;
		build(map);
	}
	

	/* (non-Javadoc)
	 * @see me.openMap.models.query.ISearchParameters#getName()
	 */
	public String getName() { return "Regional"; }

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

		JPanel fields = new JPanel();

		fields.setLayout(new SpringLayout());
	
		add(fields, BorderLayout.NORTH);
		
		// 1. Agency Box
		agencyBox = this.addAgencyComboxBox(fields);

		// 2.Coordinates
		fields.add(new JLabel("Coordinates"));

		JPanel inputs = new JPanel();
		inputs.setLayout(new GridLayout(3, 2));
		fields.add(inputs);

		NumberFormatter format = new NumberFormatter();
		format.setMinimum( new Double(-90.0));
		format.setMaximum(new Double(90.0));

		ullat_ = new JFormattedTextField(format);
		ullat_.setColumns(5);
		ullat_.setValue( new Double(0.0));

		inputs.add(ullat_);

		format = new NumberFormatter();
		format.setMinimum( new Double(-180.0));
		format.setMaximum(new Double(180.0));

		ullon_ = new JFormattedTextField(format);
		ullon_.setColumns(5);
		ullon_.setValue( new Double(0.0));

		inputs.add(ullon_);


		
	    format = new NumberFormatter();
		format.setMinimum( new Double(-90.0));
		format.setMaximum(new Double(90.0));

		lrlat_ = new JFormattedTextField(format);
		lrlat_.setColumns(5);
		lrlat_.setValue( new Double(0.0));

		inputs.add(lrlat_);

		format = new NumberFormatter();
		format.setMinimum( new Double(-180.0));
		format.setMaximum(new Double(180.0));

		lrlon_ = new JFormattedTextField(format);
		lrlon_.setColumns(5);
		lrlon_.setValue( new Double(0.0));

		inputs.add(lrlon_);
		
		// 3. LowerRight Coordinate
		JButton button = new JButton("Select");
		button.addActionListener( new SelectRegionCommand(mainWindow_, this));
		inputs.add(button);
		
		SpringLayoutUtilities.makeCompactGrid(fields,
				 2, 2, //rows, cols
				 6, 6,        //initX, initY
				 6, 6);       //xPad, yPad
	}

	/**
	 * 
	 */
	public void regionSelected(GeoPosition nw, GeoPosition se) {
		ullat_.setValue( new Double(nw.getLatitude()));
		ullon_.setValue( new Double(nw.getLongitude()));

		lrlat_.setValue( new Double(se.getLatitude()));
		lrlon_.setValue( new Double(se.getLongitude()));
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.models.query.ISearchParameters#search()
	 */
	@Override
	public List<DataDisplayHandler> search() {
		StopQueryConstraint query = new StopQueryConstraint();
		
		double lat = Double.class.cast(ullat_.getValue()).doubleValue();
		double lon = Double.class.cast(ullon_.getValue()).doubleValue();
		
		logger.setLevel(Level.FINE);
		
		logger.fine("Search ur: " + lat + " " + lon);
		
		Point ur = this.factory_.createPoint(new Coordinate(lon, lat));
		
		lat = Double.class.cast(lrlat_.getValue()).doubleValue();
		lon = Double.class.cast(lrlon_.getValue()).doubleValue();
		
		logger.fine("Search ur: " + lat + " " + lon);
		
		Point ll = this.factory_.createPoint(new Coordinate(lon, lat));
	
		query.addRectangleConstraint(ll, ur);
		
		for ( Object obj : this.agencyBox.getSelectedObjects()) {
			if ( obj instanceof Agency ) {
				query.addAgency( Agency.class.cast(obj));
			}
		}
		
		TransiteStopDao dao =
				TransiteStopDao.class.cast(DaoBeanFactory.create().getDaoBean(TransiteStopDao.class));
		
		List<DataDisplayHandler> rtn = new ArrayList<DataDisplayHandler>();
		rtn.add(new TransitStopDataHandler(dao.query(query)));
		return rtn;
	}




}
