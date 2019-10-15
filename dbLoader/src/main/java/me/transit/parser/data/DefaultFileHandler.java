package me.transit.parser.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;

import me.database.neo4j.IGraphDatabaseDAO;
import me.factory.DaoBeanFactory;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;
import me.transit.dao.AgencyDao;
import me.transit.dao.CalendarDateDao;
import me.transit.dao.RouteDao;
import me.transit.dao.TransiteStopDao;
import me.transit.database.Agency;
import me.transit.database.CalendarDate;
import me.transit.database.Route;
import me.transit.database.TransitData;
import me.transit.database.TransitStop;

public class DefaultFileHandler extends FileHandler {

	private static final String LOCATION = "Location";
	private static final String LATITUDE = "Latitude";
	private static final String LONGITUDE = "Longitude";
	private static final String AGENCYID = "AgencyId";

	private Log log = LogFactory.getLog(getClass().getName());
	private final Properties properties = new Properties();
	private final Map<String, Map<String, Method>> classMethodMap = new HashMap<>();

	/**
	 * 
	 * @param path
	 */
	public DefaultFileHandler(Blackboard blackboard) {
		super(blackboard);
		initilize();
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @return the classMethodMap
	 */
	public Map<String, Map<String, Method>> getClassMethodMap() {
		return classMethodMap;
	}

	/**
	 * 
	 * @param path
	 */
	private void initilize() {
		InputStream inStream;
		try {
			inStream =  getClass().getClassLoader().getResourceAsStream("ClassMap.properties");
			getProperties().load(inStream);

			Reflections reflections = new Reflections("me.transit.database");
			Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(GTFSFileModel.class);

			for (Class<?> cls : annotated) {
				for (Annotation annoation : cls.getAnnotations()) {
					if (annoation.annotationType() == GTFSFileModel.class) {
						GTFSFileModel model = GTFSFileModel.class.cast(annoation);
						this.getProperties().put(model.filename(), cls);
						Map<String, Method> clsmap = new HashMap<>();
						for (Method mth : cls.getDeclaredMethods()) {
							for (Annotation man : mth.getAnnotations()) {
								if (man.annotationType() == GTFSSetter.class) {
									GTFSSetter setter = GTFSSetter.class.cast(man);
									clsmap.put(setter.column(), mth);

								}
							}
						}
						getClassMethodMap().put(cls.getName(), clsmap);
					}
				}

			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 
	 * @param lat
	 * @param lon
	 * @param saver
	 * @param obj
	 */
	private void handleCoordinate(String lat, String lon, DataSaver saver, Object obj) {
		StringBuilder coord = new StringBuilder();

		coord.append(lat);
		coord.append(",");
		coord.append(lon);
		saver.save(obj, coord.toString());
		return;
	}

	/**
	 * 
	 * @param aLine
	 * @return
	 */
	private List<String> breakLine(String aLine) {
		List<String> array = new ArrayList<String>();
		String token = aLine;
		boolean hasToken = true;
		while (hasToken) {
			int ndx = token.indexOf(",");
			if (token.startsWith("\"")) {
				token = token.substring(1);
				int start = token.indexOf("\"");
				ndx = token.indexOf(",", start);
			}
			if (ndx == -1) {
				hasToken = false;
				if (token.trim().length() > 0) {
					array.add(token);
				}
			} else {
				array.add(token.substring(0, ndx).trim());
				token = token.substring(ndx + 1);
			}
		}

		return array;
	}

	/**
	 * 
	 * 
	 * @param obj
	 * @throws SQLException
	 */
	public void save(Object obj) throws SQLException {
		IGraphDatabaseDAO graph = IGraphDatabaseDAO.class
				.cast(DaoBeanFactory.create().getDaoBean(IGraphDatabaseDAO.class));

		if (obj instanceof Agency) {
			Agency current = Agency.class.cast(obj);
			AgencyDao dao = AgencyDao.class.cast(DaoBeanFactory.create().getDaoBean(AgencyDao.class));

			Agency saved = dao.findByName(current.getName());
			if (saved != null) {
				getBlackboard().resetMBR();
				getBlackboard().setAgency(saved);
			} else {
				dao.save(current);
				graph.addNode(current);
				getBlackboard().setAgency(current);
			}

		} else if (obj instanceof TransitStop) {
			TransiteStopDao dao = TransiteStopDao.class.cast(DaoBeanFactory.create().getDaoBean(TransiteStopDao.class));

			TransitStop stop = TransitStop.class.cast(obj);

			getBlackboard().getMBR().extend(stop.getLocation());

			if (stop.getDesc().isEmpty()) {
				stop.setDesc(stop.getName());
			}
			stop.setName(stop.getName().toLowerCase());
			dao.save(stop);
			graph.addNode(stop);

		} else if (obj instanceof Route) {
			RouteDao dao = RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(RouteDao.class));

			Route route = Route.class.cast(obj);
			if (route.getShortName() == null || route.getShortName().isEmpty()) {
				route.setShortName(route.getLongName());
			}
			dao.save(route);
			graph.addNode(route);

		} else if (obj instanceof CalendarDate) {
			CalendarDateDao dao = CalendarDateDao.class.cast(DaoBeanFactory.create().getDaoBean(CalendarDateDao.class));

			dao.save(CalendarDate.class.cast(obj));
		} else {
			log.error("save: Unkown class: " + obj.getClass().getName());
		}

	}

	/**
	 * 
	 * @param header
	 * @param type
	 * @param objClass
	 * @return
	 * @throws NoSuchMethodException
	 */
	private List<DataSaver> mapMethods(String header, String type, Class<?> objClass) throws NoSuchMethodException {
		List<DataSaver> rtn = new ArrayList<DataSaver>();

		Map<String, Method> methodMap = getClassMethodMap().get(objClass.getName());

		int ndx = type.indexOf(".");
		List<String> order = new ArrayList<String>();
		processHeader(header, type.substring(0, ndx), order);

		for (String name : order) {

			String setMethodName = name;
			if (name.compareTo(DefaultFileHandler.LATITUDE) == 0 || name.compareTo(DefaultFileHandler.LONGITUDE) == 0) {
				setMethodName = DefaultFileHandler.LOCATION;
			}
			
			if (!methodMap.containsKey(setMethodName)) {
				throw new NoSuchMethodException(setMethodName + " class: " + objClass.getName() + " " + name);
			}

			rtn.add(new DataSaver(methodMap.get(setMethodName), setMethodName, this.getBlackboard()));

		}
		return rtn;
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	private String getType(String filePath) {
		int ndx = filePath.lastIndexOf("/");
		return filePath.substring(ndx + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.parser.data.FileHandler#parse(java.lang.String)
	 */
	@Override
	public void parse(String filePath) {
		String line = null;
		try {

			File fp = new File(filePath);
			if (!fp.exists()) {
				return;
			}

			BufferedReader inStream = new BufferedReader(new FileReader(filePath));
			if (!inStream.ready()) {
				inStream.close();
				return;
			}

			line = inStream.readLine();
			@SuppressWarnings("rawtypes")
			Class objClass = Class.forName(this.getProperties().getProperty(getType(filePath)));
			List<DataSaver> header = mapMethods(line, getType(filePath), objClass);

			while (inStream.ready()) {
				line = inStream.readLine();
				List<String> data = this.breakLine(line);

				Object obj = objClass.newInstance();
				int fldNdx = 0;
				int headerNdx = 0;
				String lat = null;
				String lon = null;

				try {
					while (fldNdx < data.size()) {
						DataSaver saver = header.get(headerNdx++);

						String outData = data.get(fldNdx).trim();

						if (outData.length() > 0) {
							if (saver.getField().compareTo(DefaultFileHandler.LATITUDE) == 0) {
								lat = outData;
								if (lat != null && lon != null) {
									handleCoordinate(lat, lon, saver, obj);
									lat = null;
									lon = null;
								}
							} else if (saver.getField().compareTo(DefaultFileHandler.LONGITUDE) == 0) {
								lon = outData;
								if (lat != null && lon != null) {
									handleCoordinate(lat, lon, saver, obj);
									lat = null;
									lon = null;
								}
							} else if (saver.getField().compareTo(DefaultFileHandler.AGENCYID) == 0) {
								saver.save(obj, getBlackboard().getAgencyName());
							} else {
								saver.save(obj, outData);
							}
						}
						fldNdx++;
					}

					boolean valid = true;
					if (TransitData.class.isAssignableFrom(obj.getClass())) {
						TransitData td = TransitData.class.cast(obj);
						td.setAgency(getBlackboard().getAgency());
						if (!td.valid()) {
							valid = false;
							log.error("Invalid : " + obj.getClass().getSimpleName() + " >" + line);
						}
					}

					if (valid) {
						save(obj);
					}

				} catch (Exception e) {
					log.error(e.getLocalizedMessage() + " >> " + line, e);
				}
			}
			inStream.close();

		} catch (Exception e) {
			log.error(e.getLocalizedMessage() + " >> " + line, e);
		}
	}

}
