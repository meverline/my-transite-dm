package me.datamining.mapreduce;

import java.io.InputStream;
import java.io.PrintStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.math.Vertex;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class QueryResults {
	
	protected static Log log_ = LogFactory.getLog(QueryResults.class);
	
	private static final String LAT = "lat";
	private static final String LON = "lon";
	private static final String VALUE = "value";
	private static final String METRIC = "metric";

	/**
	 * 
	 * @param stream
	 */
	public void startWrite(PrintStream stream)
	{
		stream.println("<QueryResults>");
	}
	
	/**
	 * 
	 * @param stream
	 */
	public void endWrite(PrintStream stream)
	{
		stream.println("</QueryResults>");
	}
	
	/**
	 * 
	 * @param stream
	 * @param pt
	 * @param value
	 */
	public void write(PrintStream stream, Vertex pt, double value)
	{
		stream.println("   <" + DataResult.class.getSimpleName() + ">");
		stream.print("        <" + Vertex.class.getSimpleName() + " " + QueryResults.LAT + "='" + pt.getLatitudeDegress());
		stream.println("' " + QueryResults.LON + "='" + pt.getLongitudeDegress() + "' />");
		stream.println("      <" + QueryResults.METRIC + " " + QueryResults.VALUE + "='" + value  + "' />");
		stream.println("   </" + DataResult.class.getSimpleName() + ">");
	}
	
	/**
	 * 
	 * @param stream
	 */
	public boolean read(InputStream stream, ResultsHandler callback)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		boolean rtn = false;
		try {
			SAXParser  xml = factory.newSAXParser();
			xml.parse( new InputSource(stream), new ParserHandler(callback));
			rtn = true;
		} catch (Exception e) {
			log_.error("error parsing data result file", e);
			e.printStackTrace();
		} 
		return rtn;
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	protected class ParserHandler extends DefaultHandler {
		
		private DataResult current_ = new DataResult();
		private Vertex point_ = new Vertex();
		private ResultsHandler callback_ = null;
		
		public ParserHandler(ResultsHandler handler)
		{
			this.callback_ = handler;
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			
			if ( qName.equals( DataResult.class.getSimpleName() )) {
				
				this.callback_.handleResult(current_);
				current_.reset();
			}
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			
			if ( qName.equals( Vertex.class.getSimpleName() )) {
				
				double latDeg = Double.parseDouble(attributes.getValue(QueryResults.LAT));
				double lonDeg = Double.parseDouble(attributes.getValue(QueryResults.LON));
				
				point_.setLatitudeDegress(latDeg);
				point_.setLongitudeDegress(lonDeg);
				current_.setPoint(point_);
				
			} else if ( qName.equals( QueryResults.METRIC )) {
				
				double value = Double.parseDouble(attributes.getValue(QueryResults.VALUE));
				current_.setMetric(value);
			}
			
		}
		
	}
	
	
}
