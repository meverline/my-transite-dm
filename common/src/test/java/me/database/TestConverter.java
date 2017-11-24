package me.database;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import me.math.Vertex;

import static org.easymock.EasyMock.expect;


public class TestConverter extends EasyMockSupport {

	private Vertex ul = new Vertex(38.941, -77.286);
	private Vertex lr = new Vertex(38.827, -77.078);
	
    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
   
	@Mock(type=MockType.NICE)
	private HierarchicalStreamWriter writer;
	
	@Mock(type=MockType.NICE)
	private HierarchicalStreamReader reader;
	
	@Mock
	private MarshallingContext marshalling;
	
	@Mock
	private UnmarshallingContext unmarshall;
	
	private GeometryFactory factory = new GeometryFactory();
	
	@Test
	public void testLineStringConverter() {
		
		Coordinate coords[] = new Coordinate[2];
		coords[0] = new Coordinate(ul.getLatitudeDegress(), ul.getLongitudeDegress());
		coords[1] = new Coordinate(lr.getLatitudeDegress(), lr.getLongitudeDegress());
		
		LineString ls = factory.createLineString(coords);
		LineStringConverter svc = new LineStringConverter();
		
		assertTrue(svc.canConvert(ls.getClass()));
		svc.marshal(ls, writer, marshalling);
	   
		Object obj = svc.unmarshal(reader, unmarshall);
		
		assertNull(obj);
	
	}
	
	@Test
	public void testPointConverter() {
		
		PointConverter svc = new PointConverter();
		
		assertTrue(svc.canConvert(ul.toPoint().getClass()));
		svc.marshal(ul.toPoint(), writer, marshalling);
	   
		Object obj = svc.unmarshal(reader, unmarshall);
		
		assertNull(obj);
	
	}
	
	@Test
	public void testSingleValueCalendarConverter()
	{
		Calendar cal = GregorianCalendar.getInstance();
		
		SingleValueCalendarConverter svc = new SingleValueCalendarConverter();
	
		expect(reader.getValue()).andReturn( Long.toString(cal.getTime().getTime()));
		
		replayAll();
		assertTrue(svc.canConvert(cal.getClass()));
		svc.marshal(cal, writer, marshalling);
		Object obj = svc.unmarshal(reader, unmarshall);
		
		assertNotNull(obj);
		verifyAll();
		resetAll();
		
	}
}
