package me.transit.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import me.factory.DaoBeanFactory;
import me.transit.dao.AgencyDao;
import me.transit.dao.CalendarDateDao;
import me.transit.dao.RouteDao;
import me.transit.dao.RouteGeometryDao;
import me.transit.dao.ServiceDateDao;
import me.transit.dao.TransiteStopDao;
import me.transit.database.Agency;
import me.transit.database.CalendarDate;
import me.transit.database.Route;
import me.transit.database.RouteGeometry;
import me.transit.database.RouteStopData;
import me.transit.database.ServiceDate;
import me.transit.database.StopTime;
import me.transit.database.TransitData;
import me.transit.database.TransitStop;
import me.transit.database.Trip;
import me.transit.database.impl.RouteGeometryImpl;
import me.transit.database.impl.RouteStopDataImpl;
import me.transit.database.impl.ServiceDateImpl;
import me.transit.database.impl.StopTimeImpl;
import me.transit.database.impl.TripImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


/**
 * 
 * @author markeverline
 *
 */
public class TransitFeedParser {

	private static final String WINDOWS_PATH = "C:/java/me-transite/my-transite-dm";
	private static final String UNIX_PATH ="/Users/markeverline/Perforce/Transite";
	
	private static final String LOCATION = "Location";
	private static final String LATITUDE = "Latitude";
	private static final String LONGITUDE = "Longitude";
	private static final String ID = "Id";
	private static final String AGENCYID = "AgencyId";
	private static final String SET = "set";
	private static final String GET = "get";

	private static GeometryFactory factory = new GeometryFactory();
	private Agency agency = null;
	private Properties properties = new Properties();
	private HashMap<String,RouteGeometry> shaps = new HashMap<String,RouteGeometry>();
	private HashMap<String,ServiceDate> service = new HashMap<String,ServiceDate>();
	private HashMap<String,String> tripToRoute = new HashMap<String, String>();
	
	public static Log log = LogFactory.getLog(TransitFeedParser.class);
	
	/**
	 * 
	 */
	public TransitFeedParser(String path)
	{
		DaoBeanFactory.initilize();
		initilize(path);
	}
	
	/**
	 * @return the agencyId
	 */
	public String getAgencyName() {
		return agency.getName();
	}

	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgency(Agency agencyId) {
		this.agency = agencyId;
	}
	
	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public ServiceDate getServiceDate(long id)
	{
		if ( this.service.containsKey(id)) {
			return this.service.get(id);
		}
		log.info(this.getClass().getSimpleName() + " ServiceData not found: " + id);
		return null;
	}

	/**
	 * 
	 */
	private void initilize(String path)
	{
		FileReader inStream;
		try {
			inStream = new FileReader( path + "/common/config/ClassMap.properties");
			getProperties().load(inStream);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	

	
	/**
	 * @param agencyId the agencyId to set
	 */
	public Agency getAgency() {
		return this.agency;
	}

	public RouteGeometry getShape(long id)
	{
		if ( this.shaps.containsKey(id)) {
			return this.shaps.get(id);
		}
		log.info(this.getClass().getSimpleName() + " Shape not found: " + id);
		return null;
	}
	
	/**
	 * Create the filePath.
	 * @param directory
	 * @param file
	 * @return
	 */
	public  String filePath(String directory, String file)
	{
		StringBuilder rtn = new StringBuilder();
		
		rtn.append(directory.trim());
		rtn.append("/");
		rtn.append(file.trim());
		return rtn.toString();
	}
	
	/**
	 * The directory where the data file are located at.
	 * @param diretory
	 */
	public void parse(String diretory)
	{
		String files[] = getProperties().get("order").toString().split(",");
		HashMap<String, RouteTripPair> tripMap = null;
		
		for ( String dataFile : files ) {	
			log.info("parse: " + dataFile + " " + diretory);
			if ( dataFile.trim().compareTo("shapes.txt") == 0 ) {
				parseShape(filePath(diretory,dataFile));
			} 
			else if ( dataFile.trim().compareTo("calendar.txt") == 0 ) {
				parseServiceDate(filePath(diretory,dataFile));
			} 
			else if ( dataFile.trim().compareTo("stop_times.txt") == 0 ) {
				parseStopTime(filePath(diretory,dataFile), tripMap);
			}
			else if ( dataFile.trim().compareTo("trips.txt") == 0 ) {
				tripMap = parseTrip(filePath(diretory,dataFile));
			}
			else {
				parse(filePath(diretory,dataFile), dataFile);
			}
		}
		log.info("done processing: " + diretory);
	}
	
	/**
	 * Process the header.
	 * @param header
	 * @param strip
	 * @return
	 */
	private HashMap<String,Integer>  processHeader(String header, String strip, List<String> order)
	{
		String fields[] = header.split(",");
		HashMap<String,Integer> indexMap = new HashMap<String,Integer>(); 

		int ndx = 0;
		String mapTo = null;
		for ( String fld : fields) {
			String item = fld.replace('"', ' ').trim();
			if ( item.indexOf('_') == -1 ) {
				mapTo = item.substring(0,1).toUpperCase() + item.substring(1);
				order.add(mapTo);
			    indexMap.put(mapTo, ndx);
			} else {
				String data[] = item.toLowerCase().split("_");
				StringBuilder fieldName = new StringBuilder();
				
				for ( String name : data) {
					if ( name.compareTo(strip) != 0) {
					     mapTo = name.substring(0,1).toUpperCase() + name.substring(1);
						fieldName.append(mapTo);
					}
				}
				order.add(fieldName.toString());
				indexMap.put(fieldName.toString(), ndx);
			}
			ndx++;
		}
		
		return indexMap;
	}
	
	/**
	 * 
	 * @param header
	 * @param type
	 * @param objClass
	 * @return
	 * @throws NoSuchMethodException
	 */
	private List<DataSaver> mapMethods(String header, String type, Class<?> objClass) throws NoSuchMethodException 
	{
		List<DataSaver> rtn = new ArrayList<DataSaver>();
		
		HashMap<String, Method> methodMap = new HashMap<String,Method>();
		for ( Method m : objClass.getMethods()) {
			if ( m.getName().startsWith(TransitFeedParser.SET) || 
				 m.getName().startsWith(TransitFeedParser.GET)) {
				methodMap.put(m.getName(), m);
			}
		}
		
		int ndx = type.indexOf(".");
		List<String> order = new ArrayList<String>();
		processHeader(header, type.substring(0,ndx), order);
		
		for (String name : order ) {
			
			String useName = name;
			String altKey = objClass.getSimpleName()+"."+name;
			if ( getProperties().containsKey(name) ) {
				useName = this.getProperties().getProperty(name);
			} else if ( getProperties().containsKey(altKey)) {
				useName = this.getProperties().getProperty(altKey);
			} 
			
			String setMethodName = TransitFeedParser.SET + useName;
			if ( useName.compareTo(TransitFeedParser.LATITUDE) == 0 ||
				 useName.compareTo(TransitFeedParser.LONGITUDE) == 0) {
				setMethodName = TransitFeedParser.SET + TransitFeedParser.LOCATION;
			}
			if ( ! methodMap.containsKey(setMethodName) ) {
				throw new NoSuchMethodException(setMethodName + " class: " + objClass.getName() + " " + name);
			}
			
			String getMethodName = TransitFeedParser.GET + useName;
			if ( useName.compareTo(TransitFeedParser.LATITUDE) == 0 ||
					 useName.compareTo(TransitFeedParser.LONGITUDE) == 0) {
				getMethodName = TransitFeedParser.GET + TransitFeedParser.LOCATION;
			}
			if ( ! methodMap.containsKey(getMethodName) ) {
				log.info(useName + " " + TransitFeedParser.LATITUDE + " " + useName.compareTo(TransitFeedParser.LATITUDE));
				log.info(useName + " " + TransitFeedParser.LONGITUDE + " " + useName.compareTo(TransitFeedParser.LONGITUDE));
				throw new NoSuchMethodException(getMethodName + " class: " + objClass.getName() + " " + name);
			}
			rtn.add( new DataSaver(methodMap.get(getMethodName), 
					               methodMap.get(setMethodName),
					               useName,
					               this));
			
		}
		return rtn;
	}
	
	/**
	 * 
	 * @param lat
	 * @param lon
	 * @param saver
	 * @param obj
	 */
	private void handleCoordinate(String lat, String lon, DataSaver saver, Object obj)
	{
		StringBuilder coord = new StringBuilder();
		
		coord.append(lat);
		coord.append(",");
		coord.append(lon);
		saver.save(obj, coord.toString());
		return;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private Calendar convertDate(String date)
	{
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		int year = Integer.parseInt(date.substring(0,4));
		int month = Integer.parseInt(date.substring(4,6)) - 1;
		int day = Integer.parseInt(date.substring(6));
		
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return cal;
	}

	/**
	 * 
	 * @param aLine
	 * @return
	 */
	private List<String> breakLine(String aLine)
	{
		List<String> array = new ArrayList<String>();
		String token = aLine;
		boolean hasToken = true;
		while (hasToken)
		{
			int ndx = token.indexOf(",");
			if ( token.startsWith("\"") ) {
				token = token.substring(1);
				int start = token.indexOf("\"");
				ndx = token.indexOf(",", start);
			}
			if ( ndx == -1) {
				hasToken = false; 
				if ( token.trim().length() > 0 ) {
					array.add(token);
				}
			}
			else
			{
			   array.add(token.substring(0, ndx).trim());
			   token = token.substring(ndx+1);
			}
		}
		
		return array;
	}
	
	/**
	 * 
	 * @param agency
	 */
	private void deleteAgencyData(Agency agency)
	{
		/**
		AgencyDao agencyDao = 
			 AgencyDao.class.cast(DaoBeanFactory.create().getDaoBean( AgencyDao.class));
		
		List<Agency> found = agencyDao.findAllByName(agency.getName());
		if  ( found.isEmpty() ) { 
			return; 
		}
		
		try {
			for ( Agency rec : found ) {
				
				CalendarDateDao calDao = 
					CalendarDateDao.class.cast(DaoBeanFactory.create().getDaoBean( CalendarDateDao.class));
				calDao.deleteByAgency(rec);
				
				ServiceDateDao serviceDao = 
					ServiceDateDao.class.cast(DaoBeanFactory.create().getDaoBean( ServiceDateDao.class));
				serviceDao.deleteByAgency(rec);
				
				RouteDao routeDao = 
					RouteDao.class.cast(DaoBeanFactory.create().getDaoBean( RouteDao.class));
				routeDao.deleteByAgency(rec);
				agencyDao.delete(rec.getUUID());
			}
		} catch (SQLException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		**/
		
	}
	
	/**
	 * 
	 * 
	 * @param obj
	 * @throws SQLException
	 */
	public void save(Object obj) throws SQLException
	{
		if ( obj instanceof Agency ) {
			AgencyDao dao = 
					 AgencyDao.class.cast(DaoBeanFactory.create().getDaoBean( AgencyDao.class));
			
			dao.save( Agency.class.cast(obj));
		} else	if ( obj instanceof  TransitStop ) {
			TransiteStopDao dao = 
					TransiteStopDao.class.cast(DaoBeanFactory.create().getDaoBean( TransiteStopDao.class));
			
			dao.save( TransitStop.class.cast(obj));
		} else	if ( obj instanceof  Route ) {
			RouteDao dao = 
					RouteDao.class.cast(DaoBeanFactory.create().getDaoBean( RouteDao.class));
			
			dao.save( Route.class.cast(obj));
		} else	if ( obj instanceof  CalendarDate ) {
			CalendarDateDao dao = 
					CalendarDateDao.class.cast(DaoBeanFactory.create().getDaoBean( CalendarDateDao.class));
			
			dao.save( CalendarDate.class.cast(obj));
		} else {
			log.error("save: Unkown class: " + obj.getClass().getName() );
		}
		
	}
	
	/**
	 * Parse the calendar file.
	 * @param shapeFile
	 */
	private void parse(String filePath, String type)
	{
		String line = null;
		try {
			
			File fp = new File(filePath);
			if ( ! fp.exists() ) { return; }

			BufferedReader inStream = new BufferedReader(new FileReader(filePath));
			if ( ! inStream.ready()) {
				inStream.close();
				return;
			}
			
			line = inStream.readLine();
			@SuppressWarnings("rawtypes")
			Class objClass = Class.forName(this.getProperties().getProperty(type));
			List<DataSaver> header = mapMethods(line, type, objClass);
			
			while ( inStream.ready() ) {
				line = inStream.readLine();
				List<String> data = this.breakLine(line);
				
				Object obj = objClass.newInstance();
				int fldNdx = 0;
				int headerNdx = 0;
				String lat = null;
				String lon = null;
				
				try {
					while ( fldNdx < data.size()) {
						DataSaver saver = header.get(headerNdx++);
						
						String outData = data.get(fldNdx).trim();
	
						if ( outData.length() > 0 ) {
							if ( saver.getField().compareTo(TransitFeedParser.LATITUDE) == 0) {
								lat = outData;
								if ( lat != null && lon != null ) {
									handleCoordinate(lat, lon, saver, obj);
									lat = null;
									lon = null;
								}
							}
							else if ( saver.getField().compareTo(TransitFeedParser.LONGITUDE) == 0) {
								lon = outData;
								if ( lat != null && lon != null ) {
									handleCoordinate(lat, lon, saver, obj);		
									lat = null;
									lon = null;
								}
							} else if ( saver.getField().compareTo(TransitFeedParser.AGENCYID) == 0) {
								saver.save(obj, this.getAgencyName());
							} else {
								saver.save(obj, outData);		
							}
						}
						fldNdx++;
					}
					
					if ( obj instanceof Agency ) {
						Agency rec = Agency.class.cast(obj);
						this.setAgency(rec);
						deleteAgencyData(rec);
					}
					
					if ( TransitData.class.isAssignableFrom(obj.getClass()) ) {
						TransitData td = TransitData.class.cast(obj);
						td.setAgency(this.getAgency());
					}
					
					save(obj);
				
				} catch (Exception e) {
					log.error(e.getLocalizedMessage() + " >> " + line, e);
				}
			}
			inStream.close();
		
		} catch (Exception e) {
			log.error(e.getLocalizedMessage() + " >> " + line, e);
		}
		
	}
	
	public boolean convertToBoolean(String data)
	{
		if ( data.trim().compareTo("1") == 0 ) {
			return true;
		}
		return false;
	}
	
	/**
	 * Parse the calendar file.
	 * @param shapeFile
	 */
	private void parseServiceDate(String shapeFile)
	{

		try {
			
			File fp = new File(shapeFile);
			if ( ! fp.exists() ) { return; }

			BufferedReader inStream = new BufferedReader(new FileReader(shapeFile));
			if ( ! inStream.ready()) {
				inStream.close();
				return;
			}
			List<String> header = new ArrayList<String>();
			HashMap<String,Integer> indexMap = processHeader(inStream.readLine(), "service", header);
			
			while ( inStream.ready() ) {
				String line = inStream.readLine();
				if ( line.trim().length() > 0 && line.indexOf(',') != -1)  {
					String data[] = line.split(",");
				
					String id = null;
					if ( indexMap.get( TransitFeedParser.ID ) != null ) {
						id = data[indexMap.get( TransitFeedParser.ID )].replace('"', ' ').trim();
					} else {
						if ( indexMap.get( "serviceId" ) == null ) {
							id = data[0].replace('"', ' ').trim();
						} else {
							id = data[indexMap.get( "serviceId" )].replace('"', ' ').trim();
						}
					}
					
					ServiceDate sd = new ServiceDateImpl();
					
					sd.setId(id.trim());
					sd.setStartDate( this.convertDate(data[indexMap.get("StartDate")].replace('"', ' ').trim()));
					sd.setEndDate( this.convertDate(data[indexMap.get("EndDate")].replace('"', ' ').trim()));
					sd.setAgency(getAgency());
					
					int serviceFlag = 0;
					boolean weekday = false, sat = false, sun = false;
					if ( this.convertToBoolean(data[indexMap.get("Sunday")]) ) {
						serviceFlag |= ServiceDate.WeekDay.SUNDAY.getBit();
						sun = true;
					}
					if ( this.convertToBoolean(data[indexMap.get("Monday")]) ) {
						serviceFlag |= ServiceDate.WeekDay.MONDAY.getBit();
						weekday |= true;
					}
					if ( this.convertToBoolean(data[indexMap.get("Tuesday")]) ) {
						serviceFlag |= ServiceDate.WeekDay.TUESDAY.getBit();
						weekday |= true;
					}
					if ( this.convertToBoolean(data[indexMap.get("Wednesday")]) ) {
						serviceFlag |= ServiceDate.WeekDay.WENSDAY.getBit();
						weekday |= true;
					}
					if ( this.convertToBoolean(data[indexMap.get("Thursday")]) ) {
						serviceFlag |= ServiceDate.WeekDay.THURSDAY.getBit();
						weekday |= true;
					}
					if ( this.convertToBoolean(data[indexMap.get("Friday")]) ) {
						serviceFlag |= ServiceDate.WeekDay.FRIDAY.getBit();
						weekday |= true;
					}
					if ( this.convertToBoolean(data[indexMap.get("Saturday")]) ) {
						serviceFlag |= ServiceDate.WeekDay.SATURDAY.getBit();
						sat = true;
					}
					
					if ( sat && sun && weekday ) {
						sd.setService( ServiceDate.ServiceDays.ALL_WEEK );
					} else if ( weekday ) {
						sd.setService( ServiceDate.ServiceDays.WEEKDAY_SERVICE );
					} else if ( sat && weekday ) {
						sd.setService( ServiceDate.ServiceDays.WEEKDAY_SAT_SERVICE );
					} else if ( sat && sun ) {
						sd.setService( ServiceDate.ServiceDays.WEEKEND_SERVICE );
					} else if ( sat ) {
						sd.setService( ServiceDate.ServiceDays.SATURDAY_SERVICE );
					} else if ( sun ) {
						sd.setService( ServiceDate.ServiceDays.SUNDAY_SERVICE );
					}
					
					sd.setServiceDayFlag(serviceFlag);
						
					ServiceDateDao serviceDao = 
							ServiceDateDao.class.cast(DaoBeanFactory.create().getDaoBean( ServiceDateDao.class));				
					serviceDao.save(sd);
					this.service.put(sd.getId(), sd);
				}
			}
			inStream.close();
		
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		
	}
	
	private void saveShape(String id, List<Coordinate> coords)
	{
		try {
			Coordinate array[] = new Coordinate[coords.size()];
			coords.toArray(array);
			
			LineString poly =  factory.createLineString(array);
		
			RouteGeometry db = new RouteGeometryImpl();
			db.setAgency(getAgency());
			db.setId(id);
			db.setShape(poly);

			RouteGeometryDao dao = 
				RouteGeometryDao.class.cast(DaoBeanFactory.create().getDaoBean(RouteGeometryDao.class));
			dao.save(db);
			this.shaps.put(id, db);
		} catch (Exception e) {
			log.error("ID: " + id + " : coords size " + coords.size() + "\n" + e.getLocalizedMessage()  , e);
		}
		
	}
	
	/**
	 * Parse the shape file.
	 * @param shapeFile
	 */
	private void parseShape(String shapeFile) 
	{
		try {
			
			File fp = new File(shapeFile);
			if ( ! fp.exists() ) { return; }
			
			BufferedReader inStream = new BufferedReader(new FileReader(shapeFile));
			if ( ! inStream.ready()) {
				inStream.close();
				return;
			}
			List<String> header = new ArrayList<String>();
			HashMap<String,Integer> indexMap = processHeader(inStream.readLine(), "shape", header);
			
			List<Coordinate> coords = new ArrayList<Coordinate>();
			String current = null;
			while ( inStream.ready() ) {
				String line = inStream.readLine();
				String data[] = line.split(",");
			    String id = data[indexMap.get(TransitFeedParser.ID)].trim();
				
				if ( current == null ) { current = id; }
				
				if ( current.compareTo(id) != 0 ) {
					saveShape(current, coords);
					coords.clear();
					current = id;
				}
				
				double lat = Double.parseDouble( data[indexMap.get("PtLat")]);
				double lon = Double.parseDouble( data[indexMap.get("PtLon")]);				
				coords.add(new Coordinate(lon, lat));
			}
			saveShape(current, coords);
			inStream.close();
							
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	

	/**
	 * Parse the shape file.
	 * @param shapeFile
	 */
	@SuppressWarnings("unused")
	private HashMap<Long,RouteTripPair> readRouteShapeFile(String shapeFile ) 
	{
		HashMap<Long,RouteTripPair> tripMap = new HashMap<Long,RouteTripPair>();
		File fp = new File(shapeFile);
		
		String [] files = fp.list( new ShapeFileFilter());
		for ( String name : files ) {
			try {
				FileInputStream inStream = new FileInputStream(name);
				
				ValidationPreferences prefs = new ValidationPreferences();
			    prefs.setMaxNumberOfPointsPerShape(16650);
			    ShapeFileReader r = new ShapeFileReader(inStream, prefs);
			    
			    AbstractShape s;
			    while ((s = r.next()) != null) {
				    	switch (s.getShapeType()) {
						case NULL:
							break;
						case POINT:
							break;
						case POLYGON:
							break;
						case POLYLINE:
							PolylineShape aPolyline = (PolylineShape) s;
					        System.out.println("I read a Polyline with "
					            + aPolyline.getNumberOfParts() + " parts and "
					            + aPolyline.getNumberOfPoints() + " points");
					        List<Coordinate> coords = new ArrayList<Coordinate>();
					        for (int i = 0; i < aPolyline.getNumberOfParts(); i++) {
					          PointData[] points = aPolyline.getPointsOfPart(i);
					          coords.add(new Coordinate(points[i].getY(), points[i].getX()));
					          //saveShape(current, coords);
					        }
							break;
						default:
							break;
				    }
				    	
				    inStream.close();
				
			    }
				
			} catch (Exception e) {
				log.error(e.getLocalizedMessage(), e);
			}
			
		}
		return tripMap;
	}
	
	/**
	 * Parse the shape file.
	 * @param shapeFile
	 */
	@SuppressWarnings("unused")
	private void readStopShapeFile(String shapeFile, HashMap<Long,RouteTripPair> tripMap) 
	{	
		File fp = new File(shapeFile);
		
		String [] files = fp.list( new ShapeFileFilter());
		for ( String name : files ) {
			try {
				FileInputStream inStream = new FileInputStream(name);
				
				ValidationPreferences prefs = new ValidationPreferences();
			    prefs.setMaxNumberOfPointsPerShape(16650);
			    ShapeFileReader r = new ShapeFileReader(inStream, prefs);
			    
			    AbstractShape s;
			    while ((s = r.next()) != null) {
				    	switch (s.getShapeType()) {
						case NULL:
							break;
						case POINT:
							break;
						case POLYGON:
							break;
						case POLYLINE:
							PolylineShape aPolyline = (PolylineShape) s;
					        System.out.println("I read a Polyline with "
					            + aPolyline.getNumberOfParts() + " parts and "
					            + aPolyline.getNumberOfPoints() + " points");
					        List<Coordinate> coords = new ArrayList<Coordinate>();
					        for (int i = 0; i < aPolyline.getNumberOfParts(); i++) {
					          PointData[] points = aPolyline.getPointsOfPart(i);
					          coords.add(new Coordinate(points[i].getY(), points[i].getX()));
					          //saveShape(current, coords);
					        }
							break;
						default:
							break;
				    }
				    	
				    inStream.close();
				
			    }
				
			} catch (Exception e) {
				log.error(e.getLocalizedMessage(), e);
			}
		}	
		return;
	}
	
	/**
	 * Parse the shape file.
	 * @param shapeFile
	 */
	private HashMap<String,RouteTripPair> parseTrip(String shapeFile) 
	{
		HashMap<String,RouteTripPair> tripMap = new HashMap<String,RouteTripPair>();
		try {
			
			File fp = new File(shapeFile);
			if ( ! fp.exists() ) { return tripMap; }

			BufferedReader inStream = new BufferedReader(new FileReader(shapeFile));
			if ( ! inStream.ready()) {
				inStream.close();
				return tripMap;
			}
			
			List<String> header = new ArrayList<String>();
			HashMap<String,Integer> indexMap = processHeader(inStream.readLine(), "trip", header);

			String routeId = null;
			
			this.tripToRoute.clear();
			
			while ( inStream.ready() ) {
				
				String line = inStream.readLine();
				String data[] = line.split(",");
				
				Trip trip = new TripImpl();
								
				if ( indexMap.containsKey("RouteId") ) {
				   routeId = data[indexMap.get("RouteId")].replace('"', ' ').trim();			 
				} 
				
				if ( indexMap.containsKey("Id") ) {
				   String id = data[indexMap.get("Id")].trim();
				   trip.setId(id);
				}
				
				if ( indexMap.containsKey("ServiceId") ) {
					String id = data[indexMap.get("ServiceId")].trim();
					
					if ( service.get(id) != null ) {
						trip.setService(service.get(id));
					} else {
						log.error("Invalid trip service id: " + id + " route: " + routeId);
					}
				}
				
				if ( indexMap.containsKey("Headsign") ) {
				   trip.setHeadSign(data[indexMap.get("Headsign")].trim());
				} 
				
				if ( indexMap.containsKey("DirectionId") ) {
				   try {
					   int id = Integer.parseInt(data[indexMap.get("DirectionId")].trim());
					   Trip.DirectionType[] type = Trip.DirectionType.values();
					   trip.setDirectionId(type[id]);
				   } catch (Exception e) {	   
				   }
				} 
				
				if ( indexMap.containsKey("ShapeId") &&  data.length > indexMap.get("ShapeId")  ) {
				   String id = data[indexMap.get("ShapeId")].trim();
				   trip.setShape( shaps.get(id));
				}
				
				this.tripToRoute.put(trip.getId(), routeId);
				trip.setAgency(this.getAgency());
				
				tripMap.put(trip.getId(), new RouteTripPair(routeId, trip) );
			}
			inStream.close();
							
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return tripMap;
	}
	
	/**
	 * 
	 * @param routeId
	 * @return
	 */
	private void updateStop(StopRouteList data )
	{
		TransiteStopDao stopDao = 
			TransiteStopDao.class.cast(DaoBeanFactory.create().getDaoBean(TransiteStopDao.class));

		TransitStop stop = null;
		
		try {
			
			stop = TransitStop.class.cast(stopDao.loadById(data.getStop(), getAgencyName()));
			stop.setRoutes(data.getRouteList());
			stopDao.save(stop);
			
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 
	 * @param routeId
	 * @param aStopList
	 */
	private void updateRoute(String routeId, List<Trip> tripList)
	{
		Route route = null;
		try {
			
			RouteDao dao = 
				RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(RouteDao.class));
			
			route = Route.class.cast(dao.loadById(routeId, getAgencyName()));
			if ( route == null ) {
				log.info("Unable to find Route: " + routeId);
				return;
			}
			
			route.getTripList().addAll(tripList);
			dao.save(route);
		
		} catch (SQLException e) {
			if ( route != null ) {
				log.error("Route:" + route.toString());
			}
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 
	 * @param stopMap
	 * @return
	 */
	private List<StopRouteList> xrefStopToRoutes(HashMap<String,List<StopTripInfo>> stopMap)
	{
		List<StopRouteList> rtn = new ArrayList<StopRouteList>();
		
		RouteDao dao = 
				RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(RouteDao.class));
	
		Route route = null; 
		HashMap<String, String> routeMap = new HashMap<String, String>();
		
		try {
			
			for ( Entry<String,List<StopTripInfo>> entry : stopMap.entrySet()) {
				List<RouteStopData> routesList = new ArrayList<RouteStopData>();

				for ( StopTripInfo info : entry.getValue() ) {
					if ( ! routeMap.containsKey(info.getTripId()) ) {
						route = Route.class.cast(dao.loadById(info.getTripId(), getAgencyName()));
						if ( route != null ) {
							routeMap.put(info.getTripId(), route.getShortName());
						}
					}
					if ( routeMap.containsKey(info.getTripId()) ) {
						routesList.add( new RouteStopDataImpl( routeMap.get(info.getTripId()), info.getHeadSign()));
					}
				}
				
				rtn.add( new StopRouteList( entry.getKey(), routesList) );
			}
			
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		
		return rtn;
	}
	
	/**
	 * Parse the shape file.
	 * @param shapeFile
	 */
	private void parseStopTime(String shapeFile, HashMap<String, RouteTripPair> tripMap) 
	{
		try {
			
			File fp = new File(shapeFile);
			if ( ! fp.exists() ) { return; }

			BufferedReader inStream = new BufferedReader(new FileReader(shapeFile));
			if ( ! inStream.ready()) {
				inStream.close();
				return;
			}
			
			List<String> header = new ArrayList<String>();
			HashMap<String,Integer> indexMap = processHeader(inStream.readLine(), "shape", header);
			
			String current = null;
			long lineCnt = 0;
			long cnt = 0;
			StopTime last = null;
			
			RouteTripPair trip = null;
			StopTime stopTime = null;
			
			HashMap<String,List<StopTripInfo>> stopMap = new HashMap<String,List<StopTripInfo>>();
			
			while ( inStream.ready() ) {
				
				String line = inStream.readLine();
				if ( line.trim().length() > 0 && line.indexOf(',') != -1 ) {
					String data[] = line.split(",");
					
					stopTime = new StopTimeImpl(last);
					
					String id = data[indexMap.get("TripId")].replace('"', ' ').trim();
					
					if ( indexMap.containsKey("ArrivalTime") ) {
					  String time[] = data[indexMap.get("ArrivalTime")].trim().split(":");
					  StringBuilder builder = new StringBuilder();
					  for ( String str : time ) {
						  builder.append(str);
					  }
					  if ( builder.toString().length() > 0 ) {	  
						  stopTime.setArrivalTime(Long.parseLong(builder.toString().replace('"', ' ').trim()));
					  }
					} 
					
					if ( indexMap.containsKey("DepartureTime") ) {
						String time[]  = data[indexMap.get("DepartureTime")].trim().split(":");
						StringBuilder builder = new StringBuilder();
						for ( String str : time ) {
							builder.append(str);
						}
						if ( builder.toString().length() > 0 ) {	 
						   stopTime.setDepartureTime(Long.parseLong(builder.toString().replace('"', ' ').trim()));	
						}
						stopTime.setStopId(data[indexMap.get("StopId")].replace('"', ' ').trim());
					} 
					
					if ( indexMap.containsKey("DropOffType") ) {
					  int ndx = Integer.parseInt(data[indexMap.get("DropOffType")].replace('"', ' ').trim());
					  stopTime.setDropOffType(StopTime.PickupType.values()[ndx]);
					} 
					
					if ( indexMap.containsKey("PickupType") ) {
					   int ndx = Integer.parseInt(data[indexMap.get("PickupType")].replace('"', ' ').trim());
					   stopTime.setPickupType(StopTime.PickupType.values()[ndx]);
					} 
					
					if ( indexMap.containsKey("DistTravel") ) {
						double dist = Double.parseDouble(data[indexMap.get("DistTravel")].replace('"', ' ').trim());
						stopTime.setShapeDistTravel(dist);
					} 
					
					if ( indexMap.containsKey("StopHeadSign") ) {
						stopTime.setStopHeadSign(data[indexMap.get("StopHeadSign")].trim());
					}
					
					if ( indexMap.containsKey("StopId") ) {
						stopTime.setStopId(data[indexMap.get("StopId")].replace('"', ' ').trim());
						if ( ! stopMap.containsKey( stopTime.getStopId()) ) {
							stopMap.put( stopTime.getStopId(), new ArrayList<StopTripInfo>());
						}
					}
					
					if ( current == null ) { current = id; }
					
					if ( current.compareTo(id) != 0 ) {
						trip = tripMap.get(id);
						trip.getTrip().addStopTime(stopTime);
						
						if ( ! stopMap.get(stopTime.getStopId()).contains(trip.getRouteId()) ) {
							stopMap.get(stopTime.getStopId()).add( new StopTripInfo( trip.getRouteId(), 
																					 trip.getTrip().getHeadSign()));
						}
						current = id;
						trip = null;
					}
						
					trip = tripMap.get(current);
					trip.getTrip().addStopTime(stopTime);
					if ( ! stopMap.get(stopTime.getStopId()).contains(trip.getRouteId()) ) {
						stopMap.get(stopTime.getStopId()).add( new StopTripInfo( trip.getRouteId(), 
																				 trip.getTrip().getHeadSign()));
					}
					lineCnt++;
					cnt++;
					if ( cnt > 100000 ) {
						log.info("parseStopTimes " + lineCnt + " ...");
						cnt = 0;
					}
				}
			}
			
			if ( stopTime != null ) {
			   trip = tripMap.get(current);
			   trip.getTrip().addStopTime(stopTime);
			   if ( ! stopMap.get(stopTime.getStopId()).contains(trip.getRouteId()) ) {
				  stopMap.get(stopTime.getStopId()).add( new StopTripInfo( trip.getRouteId(), 
							 											   trip.getTrip().getHeadSign()));
			   }
			}
			
			HashMap<String,List<Trip>> routeToTrip = new HashMap<String,List<Trip>>();
			
			log.info("parseStopTimes:routetoTrip " + lineCnt + " ...");
			for ( RouteTripPair pair : tripMap.values()) {
				if ( ! routeToTrip.containsKey(pair.getRouteId()) ) {
					routeToTrip.put(pair.getRouteId(), new ArrayList<Trip>());
				}
				routeToTrip.get(pair.getRouteId()).add(pair.getTrip());
			}
			
			log.info("parseStopTimes:updateRoute " + lineCnt + " ...");
			for ( Entry<String,List<Trip>> data : routeToTrip.entrySet()) {
				updateRoute(data.getKey(), data.getValue());
			}
			
			inStream.close();
			
			log.info("parseStopTimes:xrefStopToRoutes " + stopMap.size());
			List<StopRouteList> aList = this.xrefStopToRoutes(stopMap);
			
			log.info("parseStopTimes:updateStop " + aList.size());
			for ( StopRouteList entry : aList) {
				this.updateStop(entry);
			}
			
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	
	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) {
		
		String path = TransitFeedParser.UNIX_PATH;
		if ( System.getProperty("os.name").toLowerCase().contains("windows")) {
			path = TransitFeedParser.WINDOWS_PATH;
		}
		
		if ( args.length > 0 ) {
			path =  args[0];
		}

		TransitFeedParser feedParser = new TransitFeedParser(path);
		
		String dir = path + "/google_transit";
		String orginization[] = { "ChapelHill", 
								  "C-Tran", 
								  "Data", 
								  "CAT",
								  "TriangleTransit",
								  "TriangleTransit-Express",
								  "FairfaxConnector",
								  "ART",
								  "metro",
								  "DC_Circulator"
								  };
		
		for ( String agency : orginization ) {
			TransitFeedParser.log.info(dir + " " +  agency);
			feedParser.parse(feedParser.filePath(dir, agency));
		}
		
	}
	
	//////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	
	private class RouteTripPair {
		
		private String routeId = null;
		private Trip trip = null;
		
		public RouteTripPair(String routeId, Trip trip)
		{
			this.routeId = routeId;
			this.trip = trip;
		}

		public String getRouteId() {
			return routeId;
		}

		public Trip getTrip() {
			return trip;
		}
	}
	
	//////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////

	private class StopRouteList {
		
		private String stop = null;
		private List<RouteStopData> routesList = null;
		
		public StopRouteList(String id, List<RouteStopData> list ) {
			stop = id;
			routesList = list;
		}
		
		public String getStop() {
			return stop;
		}
		
		public List<RouteStopData> getRouteList() {
			return this.routesList;
		}

	}
	
	//////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////

	private class StopTripInfo  {
		
		private String tripId = null;
		private String headSign = null;
		
		public StopTripInfo(String id, String head ) {
			tripId = id;
			headSign = head;
		}

		public String getTripId() {
			return tripId;
		}

		public String getHeadSign() {
			return headSign;
		}
		
		public int hashCode() {
			return tripId.hashCode();
		}
		
	}
	
	//////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////

	private class ShapeFileFilter implements FilenameFilter {

		@Override
		public boolean accept(File arg0, String arg1) {
			if ( arg0.getName().endsWith("shp") ) {
				return true;
			}
			return false;
		}
		
	}
		
}
