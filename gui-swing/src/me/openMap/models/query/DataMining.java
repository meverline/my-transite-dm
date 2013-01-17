package me.openMap.models.query;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.text.NumberFormatter;

import me.factory.DaoBeanFactory;
import me.math.grid.SpatialGridPoint;
import me.openMap.ApplicationSettings;
import me.openMap.OpenTransitMap;
import me.openMap.dialogs.SampleOptionsDialog;
import me.openMap.handlers.DataDisplayHandler;
import me.openMap.handlers.SpatialPointDataHandler;
import me.openMap.handlers.TransitStopDataHandler;
import me.openMap.mapUtils.RegionSelector;
import me.openMap.models.layout.SpringLayoutUtilities;
import me.openMap.models.query.sample.DataSample;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.Agency;
import me.transit.database.TransitStop;
import me.utils.TransiteEnums;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jfree.util.ArrayUtilities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class DataMining extends AbstractSearchParameters 
				implements RegionSelector.RegionSelectListener {
	
	private static final long serialVersionUID = 1L;
	
	private JFormattedTextField  ullat_;
	private JFormattedTextField  ullon_;

	private JFormattedTextField  lrlat_;
	private JFormattedTextField  lrlon_;
	
	private JComboBox agencyBox = null;
	private JComboBox produceBox = null;
	private JFormattedTextField  gridSize_ = null;
	private JComboBox  percentile_;
	private JPanel samplePanel = null;
	private JComboBox sampleTypes = null;
	private JComboBox units = null;
	private JButton sampleOptions = null;

	private OpenTransitMap mainWindow_ = null;
	private GeometryFactory factory_  = new GeometryFactory();
	
	private Log logger = LogFactory.getLog(DataMining.class.getSimpleName());
	
	/**
	 * 
	 * @param map
	 */
	public DataMining() {
		setLayout(new BorderLayout());
	}
	
	@Override
	public void initilize(OpenTransitMap map, List<String> sampleList) {
		mainWindow_ = map;
		build(map, sampleList);
	}

	/* (non-Javadoc)
	 * @see me.openMap.models.query.ISearchParameters#getName()
	 */
	public String getName() { return "DataMining"; }

	/* (non-Javadoc)
	 * @see me.openMap.models.query.ISearchParameters#getResultType()
	 */
	@Override
	public String getResultType() { return "Overlays"; }
	
	/**
	 * 
	 * @param map
	 */
	private void build(OpenTransitMap map, List<String> sampleList) {
		
		JPanel fields = new JPanel();

		fields.setLayout(new SpringLayout());
	
		add(fields, BorderLayout.NORTH);
		
		// 1. What to produce
		fields.add(new JLabel("Show"));
		produceBox = new JComboBox( DataSample.DataMiningType.values());
		fields.add(produceBox);

		// 2. Type of Sampleing Panel
		fields.add(new JLabel("Sample"));
		samplePanel = new JPanel();
		samplePanel.setLayout(new SpringLayout());
		
		fields.add(samplePanel);
		
		List<DataSample> aList = new ArrayList<DataSample>();
		for ( String name : sampleList) {

			try {
				Class<?>  cls = Class.forName(name);
				aList.add(DataSample.class.cast(cls.newInstance()));
			} catch (Exception e) {
				logger.error("Error: "+name, e);
			}
			
		}
		
		this.sampleOptions = new JButton("Options ...");
		
		sampleTypes = new JComboBox( aList.toArray() );
		sampleTypes.addActionListener( new SampleChangeActionListener(sampleOptions));
		samplePanel.add(sampleTypes);

		sampleOptions.setEnabled(aList.get(0).hasUserInputs());
		sampleOptions.addActionListener( new SampleOptionsActionListener(mainWindow_, sampleTypes));
		samplePanel.add(this.sampleOptions);

		SpringLayoutUtilities.makeCompactGrid(samplePanel,
											  2, 1, //rows, cols
											  0, 0,        //initX, initY
											  6, 6);       //xPad, yPad
		
		// 3. Agency Box
		agencyBox = this.addAgencyComboxBox(fields);
		
		// 4. UpperLeft Coordinagte
		fields.add(new JLabel("Grid size(miles)"));

		NumberFormatter format = new NumberFormatter();
		format.setMinimum( new Double(10.0));

		JPanel inputs = new JPanel();
		inputs.setLayout(new GridLayout(1, 2));
		
		gridSize_ = new JFormattedTextField(format);
		gridSize_.setColumns(3);
		gridSize_.setValue( new Double(0.10));

		inputs.add(gridSize_);
		
		units = new JComboBox( TransiteEnums.DistanceUnitType.values());
		inputs.add(units);
		fields.add(inputs);
		
		// 4. UpperLeft Coordinagte
		fields.add(new JLabel("Percentail Min"));

		Integer array[] = { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90 };
		percentile_ = new JComboBox(array);
		fields.add(percentile_);

		// 5. Coordinagte
		fields.add(new JLabel("Coordinates"));

		inputs = new JPanel();
		inputs.setLayout(new GridLayout(3, 2));
		fields.add(inputs);

		format = new NumberFormatter();
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
		
		JButton button = new JButton("Select");
		button.addActionListener( new SelectRegionCommand(mainWindow_, this));
		inputs.add(button);
		
		SpringLayoutUtilities.makeCompactGrid(fields,
	                 						  6, 2, //rows, cols
	                 						  6, 6,        //initX, initY
	                 						  6, 6);       //xPad, yPad

	}
	
	/*
	 * (non-Javadoc)
	 * @see me.openMap.mapUtils.RegionSelector.RegionSelectListener#regionSelected(org.jdesktop.swingx.mapviewer.GeoPosition, org.jdesktop.swingx.mapviewer.GeoPosition)
	 */
	public void regionSelected(GeoPosition nw, GeoPosition se) {
		ullat_.setValue( new Double(nw.getLatitude()));
		ullon_.setValue( new Double(nw.getLongitude()));

		lrlat_.setValue( new Double(se.getLatitude()));
		lrlon_.setValue( new Double(se.getLongitude()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.openMap.models.query.ISearchParameters#search()
	 */
	@Override
	public List<DataDisplayHandler> search() {
		StopQueryConstraint query = new StopQueryConstraint();

		double lat = Double.class.cast(ullat_.getValue()).doubleValue();
		double lon = Double.class.cast(ullon_.getValue()).doubleValue();

		logger.info("Search ur: " + lat + " " + lon);

		Point ur = this.factory_.createPoint(new Coordinate(lon, lat));

		lat = Double.class.cast(lrlat_.getValue()).doubleValue();
		lon = Double.class.cast(lrlon_.getValue()).doubleValue();

		logger.info("Search ur: " + lat + " " + lon);

		Point ll = this.factory_.createPoint(new Coordinate(lon, lat));

		query.addRectangleConstraint(ll, ur);

		for (Object obj : this.agencyBox.getSelectedObjects()) {
			if (obj instanceof Agency) {
				query.addAgency(Agency.class.cast(obj));
			}
		}

		TransiteStopDao dao = TransiteStopDao.class.cast(DaoBeanFactory
				.create().getDaoBean(TransiteStopDao.class));

		List<TransitStop> results = dao.query(query);

		List<DataDisplayHandler> rtn = new ArrayList<DataDisplayHandler>();

		TransiteEnums.DistanceUnitType unit = TransiteEnums.DistanceUnitType.class.cast(this.units.getSelectedItem());

		double distanceInMeters = 
					unit.toMeters(Double.class.cast(gridSize_.getValue()).doubleValue());
		
		DataSample.DataMiningType dmType =
						DataSample.DataMiningType.class.cast(this.produceBox.getSelectedItem());

		DataSample sample = DataSample.class.cast(this.sampleTypes.getSelectedItem());

		DescriptiveStatistics stats = new DescriptiveStatistics();
		List<SpatialGridPoint> data = sample.process(results, ur, ll, distanceInMeters, dmType);
		
		double [] array = new double[data.size()];
		int ndx = 0;
		for (SpatialGridPoint pt : data) {
			double value = pt.getData().getInterpolationValue();
			stats.addValue(value);
			array[ndx++] = value;
		}

		ApplicationSettings.create().setColorGradient(ApplicationSettings.create().getGradParms(), stats);
		
		if ( percentile_.getSelectedIndex() != 0  ) {
			Arrays.sort(array);					
			int index = (int) Math.rint(array.length / 10.0) * percentile_.getSelectedIndex();
			List<SpatialGridPoint> toDisplay = new ArrayList<SpatialGridPoint>();

			for (SpatialGridPoint pt : data) {
				if ( pt.getData().getInterpolationValue() > array[index] ) {
					toDisplay.add(pt);
				}
			}
			data = toDisplay;		
		}

		rtn.add(new SpatialPointDataHandler(data));
		rtn.add(new TransitStopDataHandler(results));
		return rtn;

	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////

	public class SampleOptionsActionListener implements ActionListener {
		
		private OpenTransitMap mainWindow = null;
		private JComboBox sampleTypes = null;
		
		public SampleOptionsActionListener(OpenTransitMap panel, JComboBox sampleBox)
		{
			this.mainWindow = panel;
			sampleTypes = sampleBox;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Object selected = sampleTypes.getSelectedItem();
			if ( selected != null ) {
				DataSample sample = DataSample.class.cast(selected);
				if ( sample.hasUserInputs() ) {
					sample.getUserInputs().setVisible(true);
					
					SampleOptionsDialog dialog = 
						new SampleOptionsDialog(mainWindow, sample.toString());
					
					JPanel panel = dialog.getLayOutPanel();
					panel.setLayout(new BorderLayout());
					panel.add( sample.getUserInputs(),  BorderLayout.CENTER);
					
					dialog.setVisible(true);
				}
			}
		}
		
	}

	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////

	public class SampleChangeActionListener implements ActionListener {
		
		private JButton optionsButton = null;
		
		public SampleChangeActionListener(JButton optionButton)
		{
			this.optionsButton = optionButton;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Object selected = sampleTypes.getSelectedItem();
			
			if ( selected != null ) {
				DataSample sample = DataSample.class.cast(selected);
				optionsButton.setEnabled(sample.hasUserInputs());
			}			
		}
		
	}
	
}
