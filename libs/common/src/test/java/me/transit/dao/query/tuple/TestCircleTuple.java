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

import me.utils.TransiteEnums;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class TestCircleTuple extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    private double distance = TransiteEnums.DistanceUnitType.MI.toMeters(1.1);
	private static final GeometryFactory factory_  = new GeometryFactory();
	private Point ul;

	@Before
	public void setUp() {
		ul = factory_.createPoint(new Coordinate(38.941, -77.286));
	}
	@Test
	public void testConstructor() {
		
		CircleTuple obj = new CircleTuple("field", ul, this.distance);
		obj = new CircleTuple(String.class, "field", ul, this.distance);
		
		assertTrue(obj.hasMultipleCriterion());
			
	}
	
	@Test
	public void testGetDoucmentQuery() {
		
		Document mongo = new Document();
		CircleTuple obj = new CircleTuple("field", ul, this.distance);
		obj.getDoucmentQuery(mongo);			
	}
	
	@Test
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
		
		CircleTuple obj = new CircleTuple("field", ul, this.distance);
		obj.getCriterion();
		
		obj = new CircleTuple(String.class, "field", ul, this.distance);
		obj.getCriterion();
		
		obj.getMultipeRestriction(mongo);
		obj.getCriterion();
	}

}
