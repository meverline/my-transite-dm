package org.transiteRepositry.server.response;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import me.transit.database.RouteGeometry;

@XStreamAlias("RouteGeometryResponse")
public class RouteGeometryResponse {
	
	@XStreamImplicit(itemFieldName="Shapes")
	private List<RouteGeometry> shapes = null;

	public RouteGeometryResponse() {}
	
	public RouteGeometryResponse(List<RouteGeometry> data) {
		shapes = data;
	}

	/**
	 * @return the shapes
	 */
	public List<RouteGeometry> getShapes() {
		return shapes;
	}

	/**
	 * @param shapes the shapes to set
	 */
	public void setShapes(List<RouteGeometry> shapes) {
		this.shapes = shapes;
	}
	
	
}
