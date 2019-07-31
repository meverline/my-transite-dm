package me.transit.dao.query;

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Rule;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import me.transit.database.Agency;
import me.utils.TransiteEnums;

public class TestStopQueryConstraint extends EasyMockSupport {

	@Rule
	public EasyMockRule rule = new EasyMockRule(this);

	@Mock
	public Agency agency;

	@Test
	public void test() {

		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(1.1);

		GeometryFactory factory = new GeometryFactory();
		StopQueryConstraint query = new StopQueryConstraint();

		Point ur = factory.createPoint(new Coordinate(-77.095, 38.89));
		Point ll = factory.createPoint(new Coordinate(-77.078, 38.871));

		query.addRectangleConstraint(ur, ll);
		query.addCircleConstriant(ll, distance);

		List<Point> ptList = new ArrayList<Point>();
		ptList.add(ur);
		ptList.add(ll);

		query.addPolygonConstraint(ptList);

		expect(agency.getUUID()).andReturn(100L);
		expect(agency.getName()).andReturn("name");
		replayAll();

		query.addAgency(agency);

		verifyAll();
		resetAll();

	}

}
