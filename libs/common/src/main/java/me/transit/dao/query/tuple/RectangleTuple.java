package me.transit.dao.query.tuple;

import java.util.ArrayList;
import java.util.List;


import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;


public class RectangleTuple extends AbstractQueryTuple {

	private final GeometryFactory factory_  = new GeometryFactory();
	private final Point ul;
	private final Point lr;

	/**
	 * 
	 * @param field
	 * @param ul
	 * @param lr
	 */
	public RectangleTuple(String field, Point ul, Point lr  )
	{
		super(null, field);
		this.ul = ul;
		this.lr = lr;
	}
	
	/**
	 * 
	 * @param alias
	 * @param field
	 * @param ul
	 * @param lr
	 */
	public RectangleTuple(Class<?> alias, String field, Point ul, Point lr )
	{
		super(alias, field);
		this.ul = ul;
		this.lr = lr;
	}

	/**
	 * 
	 */
	public boolean hasMultipleCriterion()
	{
		return true;
	}

	public Point getUl() {
		return ul;
	}

	public Point getLr() {
		return lr;
	}

	/**
	 * 
	 * @param ul
	 * @param lr
	 * @return
	 */
	public Polygon makeRectangle( Point ul, Point lr ) {

		List<Coordinate> coords = new ArrayList<>();
		
		coords.add(this.getUl().getCoordinate());
		coords.add(new Coordinate( this.getLr().getCoordinate().x, this.getUl().getCoordinate().y));
		coords.add(this.getLr().getCoordinate());
		coords.add(new Coordinate( this.getUl().getCoordinate().x, this.getLr().getCoordinate().y));
		coords.add(coords.get(0));
		
		Coordinate[] array = new Coordinate[coords.size()];
		coords.toArray(array);

        return factory_.createPolygon(factory_.createLinearRing(array), null);
	}

}
