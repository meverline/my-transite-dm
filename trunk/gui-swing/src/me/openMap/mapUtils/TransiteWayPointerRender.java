package me.openMap.mapUtils;

import java.awt.Graphics2D;
import java.util.HashSet;


import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

public class TransiteWayPointerRender implements WaypointRenderer {
	
	private HashSet<String> flags_ = new HashSet<String>();
	
	public TransiteWayPointerRender() 
	{
	}
	
	public TransiteWayPointerRender(boolean stops, boolean route) 
	{
		this.setShowStops(stops);
		this.setShowRoutes(route);
	}
	
	/**
	 * @param showStops_ the showStops_ to set
	 */
	public void setShowStops(boolean showStops) {
		
		if ( showStops ) {
			flags_.add(AbstractMapOverlay.STOPS);
		} else {
			flags_.remove(AbstractMapOverlay.STOPS);
		}
	}

	/**
	 * @param showRoutes_ the showRoutes_ to set
	 */
	public void setShowRoutes(boolean showRoutes) {
		if ( showRoutes ) {
			flags_.add(AbstractMapOverlay.ROUTE);
		} else {
			flags_.remove(AbstractMapOverlay.ROUTE);
		}
	}

	/**
	 * 
	 */
	public boolean paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint wp)
    {
    	if (TransiteWayPoint.class.isAssignableFrom(wp.getClass())) {
    		TransiteWayPoint cwp = (TransiteWayPoint) wp;
  
    		cwp.getMapOverlay().render(g, map, flags_);
		}

		return true;
    }


}
