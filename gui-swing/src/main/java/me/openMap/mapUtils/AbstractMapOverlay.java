package me.openMap.mapUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.openMap.utils.IOverlay;
import me.openMap.utils.OveralyChangedEvent;
import me.openMap.utils.OverlayChangedListener;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;

public abstract class AbstractMapOverlay extends GeoPosition implements IOverlay {
	
	public static final String ROUTE = "Route";
	public static final String STOPS = "Stop";
	
	private List<OverlayChangedListener> listeners_ = new ArrayList<OverlayChangedListener>();
	private boolean display = false;
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public AbstractMapOverlay(double latitude, double longitude) {
		super(latitude, longitude);
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#getLatDegress()
	 */
	@Override
	public double getLatDegress()
	{
		return this.getLatitude();
	}

	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#getLonDegress()
	 */
	@Override
	public double getLonDegress()
	{
		return this.getLongitude();
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#isDisplay()
	 */
	@Override
	public boolean isDisplay() {
		return display;
	}

	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#setDisplay(boolean)
	 */
	@Override
	public void setDisplay(boolean display) {
		this.display = display;
	}

	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#addOverlayChangedStateListener(OverlayChangedListener)
	 */
	@Override
	public void addOverlayChangedStateListener(OverlayChangedListener listener)
	{
		if ( listener != null ) {
			listeners_.add(listener);
		}
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#removeOverlayChangedStateListener(OverlayChangedListener)
	 */
	@Override
	public void removeOverlayChangedStateListener(OverlayChangedListener listener)
	{
		if ( listener != null ) {
			listeners_.remove(listener);
		}
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#fireOverlayChangeEvent()
	 */
	@Override
	public void fireOverlayChangeEvent()
	{
		OveralyChangedEvent event = new OveralyChangedEvent(this);
		for ( OverlayChangedListener handler : listeners_)
		{
			handler.overlayChangedEvent(event);
		}
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#getPointWidth(JXMapViewer)
	 */
	public abstract int getPointWidth(JXMapViewer map);
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#getPointHeight(JXMapViewer)
	 */
	public abstract int getPointHeight(JXMapViewer map);
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#getColor()
	 */
	public abstract Color getColor();
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#render(Graphics2D, JXMapViewer, HashSet<String>)
	 */
	public abstract void render(Graphics2D g, JXMapViewer map, HashSet<String> flags);
	
	

}
