package me.math.kdtree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Point;

import me.math.Vertex;
import me.math.grid.array.UniformSpatialGrid;
import me.utils.TransiteEnums;

public class TestMinBoundingRectangle {
	
	private Vertex ul = new Vertex(38.941, -77.286);
	private Vertex lr = new Vertex(38.827, -77.078);
	private double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
	UniformSpatialGrid grid;
	
	@Before
	public void setUp()
	{
		grid = new UniformSpatialGrid(ul, lr, distance);
	}

	@Test
	public void testConstructors() {
		
		MinBoundingRectangle obj = new MinBoundingRectangle();
		obj = new MinBoundingRectangle(grid.get(10, 10));
		obj = new MinBoundingRectangle(ul);
		obj.extend(lr);
		
		assertNotNull(obj.center());
		assertNotNull(obj.getLowerRight());
		assertNotNull(obj.getUpperLeft());
		
		obj = new MinBoundingRectangle(obj.toPolygon());
		assertTrue(! obj.isRectangle());
		
	}
	
	@Test
	public void testContains() {
		
		MinBoundingRectangle obj = new MinBoundingRectangle(ul);
		obj.extend(lr);
		
		assertTrue(obj.contains(ul));
		assertTrue(obj.contains(ul.toPoint()));
		assertTrue(obj.contains(grid.get(10, 10)));
	}
	
	@Test
	public void testExtend() {
		
		MinBoundingRectangle obj = new MinBoundingRectangle(grid.get(10, 10));
		
		obj.extend(ul);
		obj.extend((Point)null);
		obj.extend(grid.get(20, 20));
		obj.extend((Vertex)null);
		obj.extend(lr.toPoint());	

	}
	
	@Test
	public void testString() {
		
		MinBoundingRectangle obj = new MinBoundingRectangle(ul);
		
		obj.extend(lr);
		assertNotNull(obj.toString());
		
		Map<String, Object> doc = obj.toDocument();
		assertNotNull(doc);
		assertTrue(doc.containsKey("top"));
		assertTrue(doc.containsKey("bottom"));
		

	}
	
	@Test
	public void testListSet() {
		MinBoundingRectangle obj = new MinBoundingRectangle();
		
		List<Double> ld = new ArrayList<>();
		ld.add(ul.getLatitudeDegress());
		ld.add(ul.getLongitudeDegress());
		obj.setTop(ld);
		
		ld.clear();
		ld.add(lr.getLatitudeDegress());
		ld.add(lr.getLongitudeDegress());
		obj.setBottom(ld);
		
		obj.handleEnum("x", ld);
		
	}

}
