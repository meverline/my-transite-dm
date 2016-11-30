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

package me.crime.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.crime.dao.URCCatagoriesDAO;
import me.crime.database.Address;
import me.crime.database.Arrested;
import me.crime.database.Crime;
import me.crime.database.GeoPoint;
import me.crime.database.URCCatagories;
import me.crime.database.XmlReadable;
import me.crime.database.XmlTags;
import me.factory.DaoBeanFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class DataBaseLoader extends DefaultHandler implements XmlTags {

	private StringBuffer 	   buffer_ = new StringBuffer();
	private SAXParser   	   parser_ = null;
	private String       	   current_ = null;
	private XmlReadable        curObject_ = null;
	private Stack<XmlReadable> stack_ = new Stack<XmlReadable>();
	private int                count_ = 0;
	private Map<String,String> classMap_ = new HashMap<String,String>();

	protected static Log log_ = LogFactory.getLog(DataBaseLoader.class);

	public DataBaseLoader() {
		
		classMap_.put("ciras.db.Crime", Crime.class.getName());
		classMap_.put("ciras.db.Address", Address.class.getName());
		classMap_.put("ciras.db.Arrested", Arrested.class.getName());
		classMap_.put("ciras.db.URCCatagories", URCCatagories.class.getName());
		classMap_.put("ciras.db.GeoPoint", GeoPoint.class.getName());
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			parser_ = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			DataBaseLoader.log_.error(e);
		} catch (SAXException e) {
			DataBaseLoader.log_.error(e);
		}
	}

	public void reset()
	{
		count_ = 0;
	}

	public int parse(String data) {

		try {

			buffer_.delete(0, buffer_.length());
			parser_.parse(new InputSource(new FileReader(data)), this);

		} catch (SAXException e) {
			DataBaseLoader.log_.error(e,e);
		} catch (IOException e) {
			DataBaseLoader.log_.error(e,e);
		}
		return numberOfRecoredsSaved();
	}

	public int parseDir(File dir) {

		File files[]  = dir.listFiles();

		int total = 0;
		for ( int ndx = 0; ndx < files.length; ndx++ ) {
		     if ( files[ndx].isFile()  && (! files[ndx].isHidden()) ) {
		    	 DataBaseLoader.log_.info("parsing: " + files[ndx].toString() + "...");
		    	 parse( files[ndx].toString() );
			     total += this.numberOfRecoredsSaved();
			     DataBaseLoader.log_.info(files[ndx].toString() + " saved: " + numberOfRecoredsSaved() + "...");
			     reset();
		     }
		}
		return total;
	}

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
					curObject_.save();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count_++;
				curObject_ = null;
			} else {
				XmlReadable tmp = curObject_;
				curObject_ = stack_.pop();
				try {
					curObject_.handleObject(tmp);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void loadURCTable() throws SQLException, ClassNotFoundException
	{
		URCCatagoriesDAO dao = 
				URCCatagoriesDAO.class.cast(DaoBeanFactory.create().getDaoBean(URCCatagoriesDAO.class));
		
		// Read in the URC Codes.
		InputStream s = ClassLoader.getSystemResourceAsStream("me/crime/loader/CrimeData.txt");
		if (s == null) {
			 log_.error("unable to find me/crime/loader/CrimeData.txt");
		} else {

			try {
				BufferedReader bf = new BufferedReader(new InputStreamReader(s));

				while (bf.ready()) {
					String word = bf.readLine().trim().toUpperCase();
					if (!word.startsWith("#")) {
						if (word.length() > 0) {
							String[] info = word.split(",");
							URCCatagories urc = dao.findURCbyCatagory(info[0]);
							if (urc == null) {

								urc = new URCCatagories();

								int rank = Integer.parseInt(info[1].trim());

								if (rank == 1) {
									urc.setCatagorie(info[0].trim());
									urc.setCrimeGroup(info[3].trim());
									dao.save(urc);
								}
							}
						}
					}

				}
				bf.close();

			} catch (IOException e) {
				log_.error(e.getLocalizedMessage(), e);
			}

		}

	}

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
			DataBaseLoader.log_.error("saveData: " + method, e);
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
				DataBaseLoader.log_.error("startElement: " + className,e);
			} catch (IllegalAccessException e) {
				DataBaseLoader.log_.error("startElement: " + className,e);
			} catch (ClassNotFoundException e) {
				DataBaseLoader.log_.error("startElement: " + className,e);
			}
		}

	}

	public int numberOfRecoredsSaved()
	{
		return count_;
	}

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) {

		DaoBeanFactory.initilize();
		DataBaseLoader  loader = new DataBaseLoader();
		
		try {
			loader.loadURCTable();
		} catch (SQLException e) {
			log_.error(e);
		} catch (ClassNotFoundException e) {
			log_.error(e);
		}

		int total = 0;
		for ( String s : args) {
			try {
			   System.out.println("loading " + s + "... ");
			   File fp = new File(s);
			   if ( fp.isDirectory() ) {
				   total = loader.parseDir(fp);
			   } else {
				   total = loader.parse(s);
			   }
			   
			   System.out.println(s + " done, saved:" + total);
			} catch ( Exception ex ) {
				log_.error(ex);
			} catch ( java.lang.Error ex ) {
				log_.error(ex);
			}
		}
	}

}
