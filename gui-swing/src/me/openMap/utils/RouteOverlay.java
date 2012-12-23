package me.openMap.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import me.openMap.mapUtils.AbstractMapPainter;
import me.transit.database.Agency;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class RouteOverlay extends AbstractMapPainter<JXMapViewer> {
	
	private List<GeoPosition> shape = new ArrayList<GeoPosition>();
	private String routeShortName;
	private Agency agency;
	private Color color = Color.BLACK;
	
	public RouteOverlay(Geometry shape, Agency agency, String routeShortName)
	{
		for ( Coordinate c : shape.getCoordinates() ) {
			this.shape.add(new GeoPosition(c.y,c.x));
		}
		this.routeShortName = routeShortName;
		this.agency = agency;
	}
	
	public int getPointWidth(JXMapViewer map) {
		return 2;
	}

	public int getPointHeight(JXMapViewer map) {
		return 2;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public Agency getAgency() {
		// TODO Auto-generated method stub
		return this.agency;
	}
	
	/**
	 * @return the routeShortName
	 */
	public String getRouteShortName() {
		return routeShortName;
	}

	@Override
	protected void doPaint(Graphics2D g, JXMapViewer map, int w, int h) {
		
		if ( isDisplay() ) {
		    g = (Graphics2D) g.create();
	        //convert from viewport to world bitmap
	        Rectangle rect = map.getViewportBounds();
	        g.translate(-rect.x, -rect.y);
	        
	        int zoom = map.getZoom();
	        Dimension sizeInTiles = map.getTileFactory().getMapSize(zoom);
	        int tileSize = map.getTileFactory().getTileSize(zoom);
	        Dimension sizeInPixels = new Dimension(sizeInTiles.width*tileSize, sizeInTiles.height*tileSize);

	        double vpx = rect.getX();

	        // normalize the left edge of the viewport to be positive
	        while(vpx < 0) {
	            vpx += sizeInPixels.getWidth();
	        }

	        // normalize the left edge of the viewport to no wrap around the world
	        while(vpx > sizeInPixels.getWidth()) {
	            vpx -= sizeInPixels.getWidth();
	        }
	        
	        Rectangle2D vp2 = new Rectangle2D.Double(vpx, rect.getY(),
	        										 rect.getWidth(), rect.getHeight());
			Rectangle2D vp3 = new Rectangle2D.Double(vpx - sizeInPixels.getWidth(),
													 rect.getY(), rect.getWidth(),
													 rect.getHeight());
	        
	        //create a polygon
	        GeneralPath path = new GeneralPath();
	        for ( int ndx = 1; ndx < shape.size(); ndx++ ) {
	            //convert geo to world bitmap pixel
	            Point2D start = map.getTileFactory().geoToPixel(shape.get(ndx-1), 
	            												map.getZoom());
	            
	            Point2D end = map.getTileFactory().geoToPixel(shape.get(ndx), 
															  map.getZoom());
	            
	            if ( vp2.contains(start) || vp2.contains(end)) {
	            	path.append(new Line2D.Double(start, end), false);
	            } else if ( vp3.contains(start) || vp3.contains(end)) {
	            	path.append(new Line2D.Double(start, end), false);
	            }
	        }
	        
	        //do the drawing
	        g.setColor(this.getColor());
	        g.draw(path);
	        g.dispose();
		}
	}
	
}
