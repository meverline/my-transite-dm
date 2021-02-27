package me.crime.loader;

import lombok.extern.apachecommons.CommonsLog;
import me.crime.dao.AddressDao;
import me.crime.dao.CrimeDao;
import me.crime.dao.URCCatagoriesDAO;
import me.crime.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component(value="parseCrimeXml")
@CommonsLog
public class ParseCrimeXml extends DefaultHandler implements XmlTags {

	private StringBuffer 	   buffer_ = new StringBuffer();
	private String       	   current_ = null;
	private XmlReadable        curObject_ = null;
	private Stack<XmlReadable> stack_ = new Stack<XmlReadable>();
	private int                count_ = 0;
	private Map<String,String> classMap_ = new HashMap<String,String>();
	
	private final AddressDao addressDao;
	private final CrimeDao crimeDao;
	private final URCCatagoriesDAO urcCatagoriesDAO;

	@Autowired
	public ParseCrimeXml(AddressDao addressDao, CrimeDao crimeDao, URCCatagoriesDAO urcCatagoriesDAO) {
		this.addressDao = addressDao;
		this.crimeDao = crimeDao;
		this.urcCatagoriesDAO = urcCatagoriesDAO;
		
		classMap_.put("ciras.db.Crime", Crime.class.getName());
		classMap_.put("ciras.db.Address", Address.class.getName());
		classMap_.put("ciras.db.Arrested", Arrested.class.getName());
		classMap_.put("ciras.db.URCCatagories", URCCatagories.class.getName());
		classMap_.put("ciras.db.GeoPoint", GeoPoint.class.getName());
	}
	
	public int numberOfRecoredsSaved()
	{
		return count_;
	}

	public void reset()
	{
		count_ = 0;
	}
	
	/**
	 * 
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		for (int ndx = 0; ndx < length; ndx++) {
			buffer_.append(ch[start + ndx]);
		}
	}


	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {

		if ( name.compareTo(XmlTags.ELEMENT) == 0 ) {
			saveData(current_, buffer_.toString().trim(), curObject_);
			buffer_.delete(0, buffer_.length());
		}
	    else if ( name.compareTo(XmlTags.CLASS ) == 0) {
			if (stack_.empty()) {
				//( curObject_ instanceof me.crime.database.Crime ) {
				//	Crime cr = Crime.class.cast(curObject_);
				//}
				try {
					
					if ( curObject_ instanceof Address ) {
						Address old = (Address) curObject_;
						this.addressDao.save(old);
					} else if ( curObject_ instanceof URCCatagories ) {
						URCCatagories cat = (URCCatagories) curObject_;
						this.urcCatagoriesDAO.save(cat);
					} else if ( curObject_ instanceof Crime ) {
						Crime crime = Crime.class.cast(curObject_);
						this.crimeDao.save(crime);
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				count_++;
				curObject_ = null;
			} else {
				XmlReadable tmp = curObject_;
				curObject_ = stack_.pop();
				try {
					
					if ( curObject_ instanceof Address ) {
						Address old = (Address) curObject_;
						Address location = addressDao.loadAddress(old.getLocation());
						if ( location == null ) {
							addressDao.save(old);
						} else {
							curObject_ = location;
						}
						
					} else if ( curObject_ instanceof URCCatagories ) {
						URCCatagories cat = (URCCatagories) curObject_;
						URCCatagories dbcat = urcCatagoriesDAO.findURCbyCatagory(cat.getCatagorie());
						if (dbcat == null) {
							urcCatagoriesDAO.save(cat);
							cat = urcCatagoriesDAO.findURCbyCatagory(cat.getCatagorie());
						}
						curObject_ = cat;
					}
					curObject_.handleObject(tmp);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 
	 * @param methodName
	 * @param obj
	 * @return
	 * @throws SAXException
	 */
	private Method getMethod(String methodName, Object obj) throws SAXException
	{
		Method [] methods = obj.getClass().getMethods();

		for ( Method m : methods ) {
			if ( m.getName().compareTo(methodName) == 0) {
				return m;
			}
		}

		throw new SAXException("invalid method: "+ methodName);
	}

	/**
	 * 
	 * @param method
	 * @param data
	 * @param obj
	 * @throws SAXException
	 */
	private void saveData(String method, String data, Object obj) throws SAXException
	{
		Method getMethod = getMethod("get" + method, obj);
		Method setMethod = getMethod("set" + method, obj);

		try {

			Object args[] = new Object[1];
			if (getMethod.getGenericReturnType() == Integer.TYPE) {
				args[0] = new Integer(data);
			} else if (getMethod.getGenericReturnType() == String.class) {
				args[0] = data;
			} else if (getMethod.getGenericReturnType() == Double.TYPE) {
				args[0] = new Double(data);
			} else if (getMethod.getGenericReturnType() == Long.TYPE) {
				args[0] = new Long(data);
			}  else if (getMethod.getGenericReturnType() == Calendar.class) {
				String [] dt = data.split(":");
				Calendar cal = Calendar.getInstance();

				cal.set(Calendar.YEAR, Integer.parseInt(dt[0]));
				cal.set(Calendar.MONTH, Integer.parseInt(dt[1]));
				cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dt[2]));
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dt[3]));
				cal.set(Calendar.MINUTE, Integer.parseInt(dt[4]));
				cal.set(Calendar.SECOND, Integer.parseInt(dt[5]));
				cal.set(Calendar.MILLISECOND, Integer.parseInt(dt[5]));

				if ( cal.get(Calendar.YEAR) < 10 ) {
					cal.set(Calendar.YEAR, 2000 + cal.get(Calendar.YEAR));
				}

				args[0] = cal;

			}
			setMethod.invoke(obj, args);

		} catch (Exception e) {
			log.error("saveData: " + method, e);
			throw new SAXException("saveData:", e);
		}

	}

	@Override
	public void startElement(String uri, String localName, String name,
							 Attributes attributes) throws SAXException {

		if ( name.compareTo(XmlTags.ELEMENT) == 0 ) {
			current_ = attributes.getValue(XmlTags.NAME);
		}
	    else if ( name.compareTo(XmlTags.CLASS ) == 0) {
	    	String className  = attributes.getValue(XmlTags.NAME).trim();
	    	if ( this.classMap_.containsKey(className)) {
	    		className = classMap_.get(className);
	    	}
	    	try {
	    		Class<?> cls = this.getClass().getClassLoader().loadClass(className);
				Object obj = cls.newInstance();
				XmlReadable reader = XmlReadable.class.cast(obj);

				if ( curObject_ != null ) {
					stack_.push(curObject_);
				}
				curObject_ = reader;

			} catch (InstantiationException e) {
				log.error("startElement: " + className,e);
			} catch (IllegalAccessException e) {
				log.error("startElement: " + className,e);
			} catch (ClassNotFoundException e) {
				log.error("startElement: " + className,e);
			}
		}

	}

}
