package me.openMap.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.HashSet;

import me.math.LocalDownFrame;
import me.math.VectorMath;
import me.math.Vertex;
import me.openMap.ApplicationSettings;
import me.openMap.mapUtils.AbstractMapOverlay;
import me.transit.database.Agency;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;

import com.vividsolutions.jts.geom.Point;

public class StopOverlay extends AbstractMapOverlay {

	private static final int SIZE = 5;

	private String stop  = null;
	private Agency agency = null;
	private boolean adaLimits = false;
	private double adaRangeMeters = 0.75* 1609.344;
	
	/**
	 * 
	 * @param lat
	 * @param lon
	 * @param name
	 * @param agency
	 */
	public StopOverlay(double lat, double lon, String name, Agency agency)
	{
		super(lat, lon);
		this.setDisplay(true);
		stop = name;
		this.agency = agency;
	}
	
	/**
	 * 
	 * @param location
	 * @param name
	 * @param agency
	 */
	public StopOverlay(Point location, String name, Agency agency)
	{
		super(location.getCoordinate().y, location.getCoordinate().x);
		stop = name;
		this.agency = agency;
		this.setDisplay(true);
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#getAgency()
	 */
	@Override
	public Agency getAgency() {
		return this.agency;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
	
		builder.append(this.agency.getName());
		builder.append("\n");
		builder.append(this.stop);
		return builder.toString();
	}

	private int getPointSize(int zoom)
	{
		if ( zoom == 2 | zoom == 1) {
			return 10;
		} else if ( zoom > 5 ) {
			return 3;
		}
		return StopOverlay.SIZE;
	}
	
	/**
	 * @return the adaLimits
	 */
	public boolean isAdaLimits() {
		return adaLimits;
	}

	/**
	 * @param adaLimits the adaLimits to set
	 */
	public void setAdaLimits(boolean adaLimits) {
		this.adaLimits = adaLimits;
	}
	
	/**
	 * @return the adaRange
	 */
	public double getAdaRangeMeters() {
		return adaRangeMeters;
	}

	/**
	 * @param adaRange the adaRange to set
	 */
	public void setAdaRangeMeters(double adaRange) {
		this.adaRangeMeters = adaRange;
	}

	@Override
	public int getPointHeight(JXMapViewer map) {
		return getPointSize(map.getZoom());
	}

	@Override
	public int getPointWidth(JXMapViewer map) {
		// TODO Auto-generated method stub
		return getPointSize(map.getZoom());
	}
	
	@Override
	public Color getColor() {
		return ApplicationSettings.create().getAgencyColorByUrl(this.agency.getUrl());
	}

	@Override
	public void render(Graphics2D g, JXMapViewer map, HashSet<String> flags) {
		
		if ( isDisplay() ) {
	
			if ( this.isAdaLimits() ) {
				
				Color c = ApplicationSettings.create().getADDRangeColor();
				g.setColor(new Color(c.getRed(), 
									 c.getGreen(), 
									 c.getBlue(), 
									 ApplicationSettings.create().getADDAlphaValue() ));
				
				Vertex center = new Vertex(this.getLatitude(), this.getLongitude());
				LocalDownFrame southWestFrame = new LocalDownFrame(center.getEcfFromLatLon());
				
				VectorMath newPos = southWestFrame.getRelativePosition(
						this.getAdaRangeMeters(),
						0.0,
						LocalDownFrame.RelativePositionOrder.NORTH_THEN_EAST);
				
				Vertex pt = Vertex.getLatLonFromEcf(newPos);
				
				Point2D start = map.getTileFactory().geoToPixel(this, map.getZoom());
				Point2D end = map.getTileFactory().geoToPixel(new GeoPosition(pt.getLatitudeDegress(),
																				pt.getLongitudeDegress()), 
																   map.getZoom());
				
				int distanc = (int) Math.rint(Math.abs(start.distance(end)));
				
				g.fillOval(-distanc, -distanc, distanc*2, distanc*2);

			}
			
			Color c = this.getColor();
			g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
	
			int width = this.getPointWidth(map);
			int height = this.getPointHeight(map);
			g.fillRect(-(width/2), -(height/2), width, height);
			g.drawRect(-width/2, -height/2, width, height);
			
		}
	}

}