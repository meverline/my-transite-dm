package me.transit.graph.nodes;

import org.locationtech.jts.geom.Point;

public class GeoLocation extends TransiteNode {
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	protected String makeCoordinateKey(Point location) {		
		StringBuffer key = new StringBuffer();
		key.append( location.getX() );
		key.append(",");
		key.append( location.getY() );
		return key.toString();
	}
	
}
