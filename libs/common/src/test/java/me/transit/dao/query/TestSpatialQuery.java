package me.transit.dao.query;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;

import static org.easymock.EasyMock.expect;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import me.utils.TransiteEnums;

public class TestSpatialQuery extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
    @TestSubject 
    private SpatialQuery testSubject = new SpatialQueryTest();
    
    @Mock(type=MockType.NICE)
    private Criteria mongo;
    
    @Mock
    private Session session;
	private double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
	
	private static final GeometryFactory factory_  = new GeometryFactory();
	
	@SuppressWarnings("deprecation")
	@Test
	public void test() {

		Point ul = factory_.createPoint(new Coordinate(38.941, -77.286));
		Point lr = factory_.createPoint(new Coordinate(38.827, -77.078));

		testSubject.clear();
		testSubject.addCircle("field", ul, distance);
		testSubject.addCircle(String.class, "field", ul, distance);

		Coordinate [] coords = new Coordinate[5];

		coords[0] = new Coordinate(38.941, 77.078);
		coords[1] = new Coordinate(38.941, -77.286);
		coords[2] = new Coordinate(38.827, -77.286);
		coords[3] = new Coordinate(38.827, -77.078);
		coords[4] = coords[0];
		Polygon poly = factory_.createPolygon(factory_.createLinearRing(coords), null);

		List<Point> box = new ArrayList<Point>();
		for ( Coordinate cord : poly.getCoordinates()) {
			box.add( factory_.createPoint(cord));
		}
		
		testSubject.addPolygon("field", box);
		testSubject.addPolygon(String.class, "field", box);
		
		testSubject.addRectangle("field", lr, ul);
		testSubject.addRectangle(String.class, "field", lr, ul);
		
		testSubject.addOrderBy("name");
		testSubject.addOrderBy(null);
		
		expect(session.createCriteria(EasyMock.anyObject(Class.class))).andReturn(mongo);
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
		
		assertNotNull(testSubject.getCirtera());
		
		resetAll();
	}
	
	private class SpatialQueryTest extends SpatialQuery {
		
		public SpatialQueryTest() {
			super(TestSpatialQuery.class);
		}
	}

}
