package me.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class AddressToCoordinate {

	private static Log log = LogFactory.getLog(AddressToCoordinate.class);
	private GeometryFactory factory_  = new GeometryFactory();
	private GeoXmlParser parser = null;
	
	/**
	 * 
	 */
	public AddressToCoordinate()
	{
		parser = new GeoXmlParser();
	}
	
	/**
	 * 
	 * @param http
	 * @return
	 */
	private String getURLData( String http)
	{
		StringBuffer request = new StringBuffer();
		try {

			URL url = new URL(http.toString());
			InputStreamReader stream = new InputStreamReader(url.openStream());

			for ( int cnt = 0; cnt < 2; cnt++) {
				char c = ' ';
				while (stream.ready() && c != '\n') {
					c = (char) stream.read();
					if ( c != '\n') {
					  request.append(c);
					}
				}
			}
		
		} catch (Exception e) {
			AddressToCoordinate.log.error("getURLData: " + e.getLocalizedMessage(), e);
		}

		return request.toString();
	}
	
	/**
	 * 
	 * @param houseNumber
	 * @param street
	 * @param city
	 * @param state
	 * @param zipCode
	 * @return
	 */
	public Point geoCode(int houseNumber, String street, String city, String state, int zipCode)
	{
		StringBuilder urlString = new StringBuilder(YahooIds.PLACE_FINDER_URL);
		
		urlString.append("house=");
		urlString.append(houseNumber);
		urlString.append("&street=");
		urlString.append(street.replace(' ', '+'));
		urlString.append("&city=");
		urlString.append(city);
		urlString.append("&state=");
		urlString.append(state);
		urlString.append("&postal=");
		urlString.append(zipCode);
		urlString.append("&flags=CS");
		
		String response = this.getURLData(urlString.toString());
		
		Point rtn = null;
		
		if ( ! response.isEmpty() ) {
			parser.parse(response);
			if ( parser.getReturnCode() == 0 ) {
				rtn = factory_.createPoint( new Coordinate( parser.getLat(), parser.getLon() ));
			}
		}
		
		return rtn;

	}
	
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	
	protected class GeoXmlParser extends DefaultHandler {

		private double lat_;
		private double lon_;
		private int returnCode_;
		private StringBuffer buffer_ = new StringBuffer();
		private SAXParser parser_ = null;

		private static final String LATITUDE = "latitude";
		private static final String LONGITUDE = "longitude";
		private static final String ERROR = "error";

		public GeoXmlParser() {
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				parser_ = factory.newSAXParser();
			} catch (ParserConfigurationException e) {
				e.printStackTrace(System.err);
			} catch (SAXException e) {
				e.printStackTrace(System.err);
			}
		}

		public boolean parse(String data) {
			boolean rtn = true;
			try {

				setLat(0.0);
				setLon(0.0);

				buffer_.delete(0, buffer_.length());

				parser_.parse(new InputSource(new StringReader(data)), this);
			} catch (SAXException e) {
				e.printStackTrace(System.err);
				rtn = false;
			} catch (IOException e) {
				e.printStackTrace(System.err);
				rtn = false;
			}
			return rtn;
		}

		/**
		 * @return the lat_
		 */
		public double getLat() {
			return lat_;
		}

		/**
		 * @param lat_
		 *            the lat_ to set
		 */
		public void setLat(double lat_) {
			this.lat_ = lat_;
		}

		/**
		 * @return the lon_
		 */
		public double getLon() {
			return lon_;
		}

		/**
		 * @param lon_
		 *            the lon_ to set
		 */
		public void setLon(double lon_) {
			this.lon_ = lon_;
		}
		
		/**
		 * @return the returnCode_
		 */
		public int getReturnCode() {
			return returnCode_;
		}

		/**
		 * @param returnCode_ the returnCode_ to set
		 */
		public void setReturnCode(int returnCode_) {
			this.returnCode_ = returnCode_;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
		 */
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			for (int ndx = 0; ndx < length; ndx++) {
				buffer_.append(ch[start + ndx]);
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
		 *      java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (name.compareTo(LATITUDE) == 0) {
				this.setLat(Double.parseDouble(buffer_.toString().trim()));
			} else if (name.compareTo(LONGITUDE) == 0) {
				this.setLon(Double.parseDouble(buffer_.toString().trim()));
			} else if (name.compareTo(ERROR) == 0) {
				this.setReturnCode(Integer.parseInt(buffer_.toString().trim()));
			} 
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
		 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {;
				buffer_.delete(0, buffer_.length());
		}

	}


}
