package me.transit.dao.query.tuple;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;

import org.bson.Document;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class TestRectangleTuple extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
	private static final GeometryFactory factory_  = new GeometryFactory();
    private Point lr;
    private Point ul;

    @Before
	public void setUp() {
		ul = factory_.createPoint(new Coordinate(38.941, -77.286));
		lr = factory_.createPoint(new Coordinate(38.827, -77.078));
	}
    	
	@Test
	public void testConstructor() {
		
		RectangleTuple obj = new RectangleTuple("field", ul, lr);
		obj = new RectangleTuple(String.class, "field", ul, lr);
		
		assertTrue(obj.hasMultipleCriterion());
			
	}
	
	@Test
	public void testGetDoucmentQuery() {

		Document mongo = new Document();
		RectangleTuple obj = new RectangleTuple("field", ul, lr);
		obj.getDoucmentQuery(mongo);			
	}
	
	@Test
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
		
		RectangleTuple obj = new RectangleTuple("field", ul, lr);
		obj.getCriterion();
		
		obj = new RectangleTuple(String.class, "field", ul, lr);
		obj.getCriterion();
		
		obj.getMultipeRestriction(mongo);
		obj.getCriterion();
	}

}
