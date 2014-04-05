package me.openMap.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.HashSet;

import me.math.grid.AbstractSpatialGridOverlay;
import me.math.grid.AbstractSpatialGridPoint;
import me.openMap.ApplicationSettings;
import me.openMap.mapUtils.AbstractMapOverlay;
import me.transit.database.Agency;
import me.utils.ColorGradient;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;

public class SpatialPointOverlay extends AbstractMapOverlay {
	
	private AbstractSpatialGridPoint gp_ = null;
	private AbstractSpatialGridOverlay grid_ = null;

	public SpatialPointOverlay(AbstractSpatialGridPoint gp, double spacingMeters, AbstractSpatialGridOverlay grid)
	{
		super( gp.getVertex().getLatitudeDegress(), gp.getVertex().getLongitudeDegress());
		gp_ = gp;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public int getPointSize(JXMapViewer map)
	{
		
		AbstractSpatialGridPoint pt = grid_.getNextGridPoint(gp_);
		GeoPosition aPoint = new GeoPosition(pt.getVertex().getLatitudeDegress(),
											 pt.getVertex().getLongitudeDegress());

		Point2D start = map.convertGeoPositionToPoint(this);
		Point2D end = map.convertGeoPositionToPoint(aPoint);

		int value = (int) Math.abs(start.getX() - end.getX());
		if ( value == 0 ) {
			return 1;
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see me.openMap.mapUtils.AbstractMapOverlay#getColor()
	 */
	public Color getColor()
	{
		return Color.black;
	}

	/*
	 * (non-Javadoc)
	 * @see me.openMap.mapUtils.AbstractMapOverlay#getPointHeight(org.jdesktop.swingx.JXMapViewer)
	 */
	@Override
	public int getPointHeight(JXMapViewer map) {
		return getPointSize(map);
	}

	/*
	 * (non-Javadoc)
	 * @see me.openMap.mapUtils.AbstractMapOverlay#getPointWidth(org.jdesktop.swingx.JXMapViewer)
	 */
	@Override
	public int getPointWidth(JXMapViewer map) {
		return getPointSize(map);
	}

	/*
	 * (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#getAgency()
	 */
	@Override
	public Agency getAgency() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see me.openMap.mapUtils.AbstractMapOverlay#render(java.awt.Graphics2D, org.jdesktop.swingx.JXMapViewer, java.util.HashSet)
	 */
	@Override
	public void render(Graphics2D g, JXMapViewer map, HashSet<String> flags) {
		
    		int width = this.getPointWidth(map);
    		int height = this.getPointHeight(map);
		    ColorGradient cg = 
		    	ApplicationSettings.create().findColorGradiantByValue(gp_.getData().getInterpolationValue());
		    
		    if ( cg != null ) {
		    	Color c = cg.findHeatMapColor((int)gp_.getData().getInterpolationValue());
			
		    	g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), cg.getAlhpaValue()));
		    	g.fillRect(-(width/2), -(height/2), width, height);
		    	g.drawRect(-width/2, -height/2, width, height);
		    } 
		    else {
		    	Color c = Color.BLACK;
		    	g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
		    	g.drawRect(-width/2, -height/2, width, height);
		    }
			
	}

}
