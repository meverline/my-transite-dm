 package me.transit.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import me.factory.DaoBeanFactory;
import me.transit.dao.AgencyDao;
import me.transit.dao.RouteDao;
import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


/**
 * Method which holds the method which are called to save data.
 * @author markeverline
 *
 */
public class DataSaver {
	
	public enum DataType {
		LONG {
			@Override
			protected Object convert(String data) {
				Long rtn = null;
				if ( data.contains(":") ) {
				  String time[] = data.split(":");
				  String str = time[0] + time[1];
				  rtn = new Long(str);
				} else {
				  rtn =  new Long( data);
				}
				return rtn;
			}
		},
		INT {
			@Override
			protected Object convert( String data) {
				return new Integer(data);
			}
		},
		DOUBLE {
			@Override
			protected Object convert( String data) {
				return new Double(data);
			}
		},
		BOOLEAN {
			@Override
			protected Object convert( String data) {
				return new Boolean(data);
			}
		},
		STRING {
			@Override
			protected Object convert( String data) {
				String rtn = data;
				if ( rtn.contains("\"")) {
					rtn = rtn.replace('"', ' ').trim();
				}
				return rtn;
			}
		},
		ROUTE {
			@Override
			public Object convert(String data) {
				String id = data.trim();
				RouteDao dao = RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(RouteDao.class));
				return dao.loadById(id, getParser().getAgencyName());
			}
			
		},
		SERVICEDATE {
			@Override
			public Object convert(String data) {
				long id = Long.parseLong(data.trim());
				return this.getParser().getServiceDate(id);
			}
			
		},
		AGENCY {
			@Override
			public Object convert(String data) {
				AgencyDao dao = AgencyDao.class.cast(DaoBeanFactory.create().getDaoBean(AgencyDao.class));
				return dao.findByName(data);
			}
			
		},
		ENUM {
			@Override
			public Object convert(String data) {
				String rtn = data.trim();
				Object obj = null;
				if ( rtn.contains("\"")) {
					rtn = rtn.replace('"', ' ').trim();
				}
				if ( rtn.isEmpty() || rtn.length() < 1) {
					rtn = "UNKOWN";
					for ( Object str : getReturnType().getEnumConstants()) {
						if ( rtn.compareTo( str.toString()) == 0 ) {
							obj = str;
						}
					}
				} else {
					int index = Integer.parseInt(rtn);
					obj = getReturnType().getEnumConstants()[index];
				}
				return obj;
			}
			
		},
		POINT {
			@Override
			protected Object convert( String data) {
				GeometryFactory factory = new GeometryFactory();
				String world[] = data.split(",");
				double lat = Double.parseDouble(world[0]);
				double lon = Double.parseDouble(world[1]);
				// TODO: Switch
				return factory.createPoint( new Coordinate(lat, lon));
			}
		},
		GEOMETRY {
			@Override
			protected Object convert( String data) {
				long id = Long.parseLong(data.trim());
				return this.getParser().getShape(id);
			}
		},
		CALENDAR {
			@Override
			protected Object convert( String data) {
				Calendar cal = Calendar.getInstance();	
				cal.set(Calendar.HOUR, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.set(Calendar.YEAR, Integer.parseInt(data.substring(0,4).replace('"', ' ').trim()));
				cal.set(Calendar.MONTH, Integer.parseInt(data.substring(4,6).replace('"', ' ').trim()) - 1);
				cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data.substring(6).replace('"', ' ').trim()));
				return cal;
			}
		},
		OBJECT {
			@Override
			protected Object convert( String data) {
				return data;
			}
		};
		
		private Class<?> returnType = null;
		private TransitFeedParser parser = null;
		
		public void setReturnType(Class<?> aClass) {
			returnType = aClass;
		}
		
		public Class<?> getReturnType() {
			return returnType;
		}
		
		public void setParser(TransitFeedParser aClass) {
			parser = aClass;
		}
		
		public TransitFeedParser getParser() {
			return parser;
		}
		
		protected abstract Object convert(String  data);
		public void save(Object obj, Method meth, String data ) 
		    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			Object parameters[] = new Object[1];
			
			parameters[0] = this.convert(data);
			meth.invoke(obj, parameters);
		}
		
	}
	
	private Method method = null;
	private Class<?> returnType = null;
	private DataType type;
	private String fieldName = null;	
	private TransitFeedParser parser = null;
	private Log log = LogFactory.getLog(DataSaver.class);


	/**
	 * 
	 * @param method
	 * @throws NoSuchMethodException 
	 */
	public DataSaver(Method getMethod, Method setMethod, String field, TransitFeedParser parser) throws NoSuchMethodException
	{
		setMethod(setMethod);
		fieldName = field;
		setParser( parser);
		initilize(getMethod);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getField()
	{
		return fieldName;
	}
		
	/**
	 * @throws NoSuchMethodException 
	 * 
	 */
	private void initilize(Method getMethod) throws NoSuchMethodException {

		if ( getMethod.getReturnType().isPrimitive() ) {
			if (getMethod.getReturnType() == Long.TYPE ) {
				setType(DataType.LONG);
			} else if  (getMethod.getReturnType() == Integer.TYPE ) {
				setType(DataType.INT);
			} else if  (getMethod.getReturnType() == Double.TYPE ) {
				setType(DataType.DOUBLE);
			} else if  (getMethod.getReturnType() == Boolean.TYPE ) {
				setType(DataType.BOOLEAN);
			} else {
				throw new NoSuchMethodException("Unkown type: " + getMethod.getReturnType());
			}
	    } else if  (getMethod.getReturnType() == String.class ) {
				setType(DataType.STRING);
	    } else if  (getMethod.getReturnType() == Calendar.class ) {
			setType(DataType.CALENDAR);
		} else if  (getMethod.getReturnType() == Point.class ) {
			setType(DataType.POINT);
		} else if  (getMethod.getReturnType() == RouteGeometry.class ) {
			setType(DataType.GEOMETRY);
		} else if  (getMethod.getReturnType() == Route.class ) {
			setType(DataType.ROUTE);
		} else if  (getMethod.getReturnType() == Agency.class ) {
			setType(DataType.AGENCY);
		} else if  (getMethod.getReturnType() == ServiceDate.class ) {
			setType(DataType.SERVICEDATE);
		} else {
			if ( getMethod.getReturnType().isEnum() ) {
				setType(DataType.ENUM);
			} else {
				log.info("OBJECT: " + getMethod.getName() + " "+ getMethod.getReturnType().getName() );
				setType(DataType.OBJECT);
			}
		}
		this.returnType = getMethod.getReturnType();
		
	}
			
	/**
	 * @return the method
	 */
	protected Method getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	protected void setMethod(Method method) {
		this.method = method;
	}

	/**
	 * @return the type
	 */
	protected DataType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	protected void setType(DataType type) {
		this.type = type;
	}
	
	/**
	 * @return the parser
	 */
	protected TransitFeedParser getParser() {
		return parser;
	}

	/**
	 * @param parser the parser to set
	 */
	protected void setParser(TransitFeedParser parser) {
		this.parser = parser;
	}

	/**
	 * Save the data to a given object.
	 * @param obj
	 * @param data
	 */
	public void save(Object obj, String data) {
		try {
			getType().setReturnType(returnType);
			getType().setParser(getParser());
			getType().save(obj, getMethod(), data);
		} catch (Exception e) {
			StringBuilder str = new StringBuilder();
			str.append(e.getLocalizedMessage());
			str.append(": ");
			str.append(this.getMethod().getName());
			str.append("( ");
			str.append(getType());
			str.append(") ");
			str.append(this.returnType.getName());
			str.append(" ");
			str.append(data);
			log.error(str.toString() , e);
		}
	}

}