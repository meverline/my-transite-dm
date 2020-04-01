package me.transit.parser.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import me.transit.database.Agency;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;
import me.transit.database.TransitData;
import me.transit.parser.data.converters.DataConverterFactory;
import me.transit.parser.data.savers.DataSaver;

public abstract class AbstractDefaultFileHandler extends AbstractFileHandler {

	private static final String LOCATION = "location";
	private static final String LATITUDE = "stop_lat";
	private static final String LONGITUDE = "stop_lon";
	private static final String AGENCYID = "agency_id";

	protected Log log = LogFactory.getLog(getClass().getName());
	private final Map<String, Class<?>> properties = new HashMap<>();
	private final Map<String, Map<String, Method>> classMethodMap = new HashMap<>();
	protected final IGraphDatabaseDAO graphDatabase;
	protected final DataConverterFactory dataConverterFactory;

	@Autowired
	public AbstractDefaultFileHandler(Blackboard blackboard, IGraphDatabaseDAO graphDatabase, DataConverterFactory dataConverterFactory) {
		super(blackboard);
		initilize();
		this.graphDatabase = Objects.requireNonNull(graphDatabase,"graphDatabase can not be null");
		this.dataConverterFactory = Objects.requireNonNull(dataConverterFactory,"dataConverterFactory can not be null");
	}
		
	/**
	 * @return the graphDatabase
	 */
	protected IGraphDatabaseDAO getGraphDatabase() {
		return graphDatabase;
	}

	/**
	 * @return the properties
	 */
	public Map<String, Class<?>> getProperties() {
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
	 */
	public void endProcess() {
		
		getBlackboard().getAgency().setMBR(getBlackboard().getMBR().toPolygon());
		log.info(getBlackboard().getMBR().toString());
		try {
			save(getBlackboard().getAgency());
		} catch (SQLException e) {
			log.error(e);
		}
	}

	private void initilize() {
	
		try {

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
		saver.save(obj, lat + "," + lon);
	}

	/**
	 * 
	 * @param aLine
	 * @return
	 */
	private List<String> breakLine(String aLine) {
		List<String> array = new ArrayList<>();
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
	public abstract void save(Object obj) throws SQLException;

	/**
	 * 
	 * @param header
	 * @param type
	 * @param objClass
	 * @return
	 * @throws NoSuchMethodException
	 */
	private List<DataSaver> mapMethods(String header, String type, Class<?> objClass) throws NoSuchMethodException {
		List<DataSaver> rtn = new ArrayList<>();

		Map<String, Method> methodMap = getClassMethodMap().get(objClass.getName());

		List<String> order = new ArrayList<>();
		processHeader(header, order);

		for (String name : order) {

			String setMethodName = name.trim();
			if (name.compareTo(AbstractDefaultFileHandler.LATITUDE) == 0 || name.compareTo(AbstractDefaultFileHandler.LONGITUDE) == 0) {
				setMethodName = AbstractDefaultFileHandler.LOCATION;
			}
			
			if (!methodMap.containsKey(setMethodName)) {
				throw new NoSuchMethodException(setMethodName + " class: " + objClass.getName() + " |" + name  + "| have " + methodMap.keySet().toString());
			}

			rtn.add(new DataSaver(methodMap.get(setMethodName), setMethodName, this.getBlackboard(), name, dataConverterFactory));

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
	
	protected abstract void setAgency(Object obj, Agency agency);

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.parser.data.FileHandler#parse(java.lang.String)
	 */
	@Override
	public boolean parse(String filePath) throws Exception {
		boolean rtn = false;
		String line = null;
		BufferedReader inStream = null;
		
		try {

			File fp = new File(filePath);
			if (!fp.exists()) {
				log.error("file path does not exit: " + filePath);
				return rtn;
			}

			inStream = new BufferedReader(new FileReader(filePath));
			if (!inStream.ready()) {
				inStream.close();
				return rtn;
			}

			line = inStream.readLine();
			String type = getType(filePath);
			@SuppressWarnings("rawtypes")
			Class objClass = this.getProperties().get(type);
			if ( objClass == null ) {
				log.error("unable to find: " + type + " " + this.getProperties());
				return false;
			}
			List<DataSaver> header = mapMethods(line, getType(filePath), objClass);

			while (inStream.ready()) {
				line = inStream.readLine();
				List<String> data = this.breakLine(line);

				Object obj = objClass.newInstance();
				int fldNdx = 0;
				int headerNdx = 0;
				String lat = null;
				String lon = null;

				
					while (fldNdx < data.size()) {
						DataSaver saver = header.get(headerNdx++);

						String outData = data.get(fldNdx).trim();

						if (outData.length() > 0) {
							if (saver.getField().compareTo(AbstractDefaultFileHandler.LATITUDE) == 0) {
								lat = outData;
								if (lon != null) {
									handleCoordinate(lat, lon, saver, obj);
								}
							} else if (saver.getField().compareTo(AbstractDefaultFileHandler.LOCATION) == 0) {
								if (saver.getOrgHeader().compareTo(AbstractDefaultFileHandler.LONGITUDE) == 0) {
								   lat = outData;
								} else if (saver.getOrgHeader().compareTo(AbstractDefaultFileHandler.LATITUDE) == 0) {
								   lon = outData;
								}
								if (lat != null && lon != null) {
									handleCoordinate(lat, lon, saver, obj);
									lat = null;
									lon = null;
								}
								
							} else if (saver.getField().compareTo(AbstractDefaultFileHandler.AGENCYID) != 0 ) {
								saver.save(obj, outData);
								
							}
						}
						fldNdx++;
					}

					boolean valid = true;
					if (TransitData.class.isAssignableFrom(obj.getClass())) {
						TransitData td = TransitData.class.cast(obj);
						this.setAgency(obj, getBlackboard().getAgency());
						if (!td.valid()) {
							valid = false;
							log.error("Invalid : " + obj.getClass().getSimpleName() + " >" + line);
						}
					}

					if (valid) {
						save(obj);
						rtn = true;
					}

			}
			inStream.close();

		} catch (Exception e) {
			log.error(e.getLocalizedMessage() + " >> " + line, e);
			throw e;
		} finally {
			if ( inStream != null ) {
				inStream.close();
			}
		}
		return rtn;
	}

}
