package me.transit.dao.query.tuple;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.spatial.criterion.SpatialRestrictions;

import com.mongodb.BasicDBObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

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
	public void getCriterion(Criteria crit) {

		Polygon range = makeRectangle(this.ul, this.lr);
		
		if ( getAlias() != null ) {
			String name =  getAlias().getSimpleName();
			crit.createAlias( name, name);
			
			StringBuilder builder = new StringBuilder(name);
			builder.append(".");
			builder.append(getField());
			crit.add(SpatialRestrictions.filter( builder.toString(), range));
			crit.add(SpatialRestrictions.within( builder.toString(), range));
			
		} else {
			
			crit.add(SpatialRestrictions.filter( getField(), range));
			crit.add(SpatialRestrictions.within( getField(), range));
		}
		
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
