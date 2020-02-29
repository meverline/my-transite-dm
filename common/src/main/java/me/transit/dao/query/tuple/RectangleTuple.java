package me.transit.dao.query.tuple;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.mongodb.BasicDBObject;

public class RectangleTuple extends AbstractQueryTuple {

	private GeometryFactory factory_  = new GeometryFactory();
	private Point ul  = null;
	private Point lr  = null;

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

	/**
	 * 
	 * @param ul
	 * @param lr
	 * @return
	 */
	private Polygon makeRectangle( Point ul, Point lr ) {

		List<Coordinate> coords = new ArrayList<Coordinate>();
		
		coords.add(ul.getCoordinate());
		coords.add(new Coordinate( lr.getCoordinate().x, ul.getCoordinate().y));
		coords.add(lr.getCoordinate());
		coords.add(new Coordinate( ul.getCoordinate().x, lr.getCoordinate().y));
		coords.add(coords.get(0));
		
		Coordinate array[] = new Coordinate[coords.size()];
		coords.toArray(array);

        return factory_.createPolygon(factory_.createLinearRing(array), null);
	}

	/**
	 * 
	 */
	public Tuple getCriterion() {

		Polygon range = makeRectangle(this.ul, this.lr);
		StringBuilder builder = new StringBuilder("within( ");
		
		if ( getAlias() != null ) {
			builder.append(getAlias().getSimpleName());
			builder.append(".");
		}
		builder.append(getField());
		builder.append(", :rect");
		
		Tuple rtn = new Tuple(builder.toString());
		rtn.add("rect", range);
		return rtn;
		
	}
	
	public void getDoucmentQuery(BasicDBObject query) {
				
        Double [] ulc = new Double[2];
        
        ulc[0] = ul.getCoordinate().x;
        ulc[1] = lr.getCoordinate().y;

        Double [] urc = new Double[2];
        urc[0] = lr.getCoordinate().x;
        urc[1] = ul.getCoordinate().y;
        
        query.put(getField(), new BasicDBObject("$geoWithin", new Object[] { ulc, urc } ));
        
	}
	
}
