package me.datamining.mapreduce;

import lombok.extern.apachecommons.CommonsLog;
import me.math.Vertex;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.PrintStream;

@CommonsLog
public class QueryResults {

	private static final String LAT = "lat";
	private static final String LON = "lon";
	private static final String VALUE = "value";
	
	private int maxNumber = 550;
	private StringBuffer buffer_ = new StringBuffer();
	private DescriptiveStatistics xstats_ = new DescriptiveStatistics();

	/**
	 * 
	 * @param stream
	 */
	public void startWrite(PrintStream stream)
	{
		stream.println("<?xml version='1.0' encoding='UTF-8'?>");
		stream.println();
		stream.println("<QueryResults>");
	}
	
	public int getMaxNumber()
	{
		return maxNumber;
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
		xstats_.addValue(value);
		buffer_.delete(0, buffer_.length());
		buffer_.append("   <");
		buffer_.append(DataResult.class.getSimpleName());
		buffer_.append(" ");
		buffer_.append(QueryResults.LAT);
		buffer_.append("='");
		buffer_.append(pt.getLatitudeDegress());
		buffer_.append("' ");
		buffer_.append(QueryResults.LON);
		buffer_.append("='");
		buffer_.append(pt.getLongitudeDegress());
		buffer_.append("' ");
		buffer_.append(QueryResults.VALUE);
		buffer_.append("='");
		buffer_.append(value);
		buffer_.append("' />");
		
		stream.println(buffer_.toString());
	}
	
	/**
	 * 
	 * @return
	 */
	public double getVariance()
	{
		return xstats_.getVariance();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getN()
	{
		return xstats_.getN();
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
			log.error("error parsing data result file", e);
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
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			
			if ( qName.equals( DataResult.class.getSimpleName() )) {
				
				double latDeg = Double.parseDouble(attributes.getValue(QueryResults.LAT));
				double lonDeg = Double.parseDouble(attributes.getValue(QueryResults.LON));
				double value = Double.parseDouble(attributes.getValue(QueryResults.VALUE));
				
				point_.setLatitudeDegress(latDeg);
				point_.setLongitudeDegress(lonDeg);
				current_.setPoint(point_);
				current_.setMetric(value);
				
				this.callback_.handleResult(current_);
				current_.reset();
			}
			
		}
		
	}
	
	
}
