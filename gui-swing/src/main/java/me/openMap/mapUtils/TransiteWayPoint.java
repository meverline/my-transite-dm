package me.openMap.mapUtils;

import java.awt.Color;


import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;

public class TransiteWayPoint extends Waypoint {

	private AbstractMapOverlay overlay_;

	public TransiteWayPoint(AbstractMapOverlay overlay)
	{
		super(overlay.getLatDegress(), overlay.getLonDegress() );
		overlay_ = overlay;
	}

	public Color getColor()
	{
		return overlay_.getColor();
	}

	public int getPointWidth(JXMapViewer map)
	{
		return overlay_.getPointWidth(map);
	}

	public int getPointHeight(JXMapViewer map)
	{
		return overlay_.getPointHeight(map);
	}
	
	public AbstractMapOverlay getMapOverlay()
	{
		return overlay_;
	}

}
