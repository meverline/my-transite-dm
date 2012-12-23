//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright © 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

/**
 *
 */
package me.openMap.models;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import me.openMap.mapUtils.AbstractMapOverlay;
import me.openMap.mapUtils.AbstractMapPainter;
import me.openMap.mapUtils.OverlayWayPointPainter;
import me.openMap.mapUtils.TransiteWayPoint;
import me.openMap.mapUtils.TransiteWayPointerRender;
import me.openMap.utils.IOverlay;
import me.openMap.utils.MapRender;
import me.openMap.utils.OveralyChangedEvent;
import me.openMap.utils.OverlayChangedListener;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;

/**
 * @author markeverline
 *
 */
public class MapViewer extends JPanel implements MapRender, OverlayChangedListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JXMapKit map_;
	private JLabel lat_;
	private JLabel lon_;
	private JLabel level_;

	private List<Waypoint> overlays_ = new ArrayList<Waypoint>();
	private OverlayWayPointPainter<JXMapViewer> painter_ = null;
	private List<AbstractPainter<JXMapViewer>> painters_ = new ArrayList<AbstractPainter<JXMapViewer>>();
	private TransiteWayPointerRender render_ = null;

	public MapViewer(JFrame frame) {
		setLayout(new BorderLayout());
		build();
	}

	public void build() {
			
		map_ = new JXMapKit();
		map_.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		map_.setMiniMapVisible(false);
		map_.setZoomButtonsVisible(true);
		map_.setZoomSliderVisible(true);
		map_.getMainMap().setZoomEnabled(true);
		map_.setDefaultProvider(JXMapKit.DefaultProviders.OpenStreetMaps);

		map_.setCenterPosition(new GeoPosition(38.883903, -77.173694));
		map_.setZoom(4);
		
		map_.getMainMap().addMouseMotionListener(new CursorMove() );
		map_.getMainMap().addMouseListener(new PointAndClick() );

		add(map_, BorderLayout.CENTER);

		JPanel pane = new JPanel();
		
		add(pane, BorderLayout.NORTH);
		pane = new JPanel();
				
		pane.add( new JLabel("Latitude"));
		lat_ = new JLabel(" ");
		pane.add( lat_);

		pane.add( new JLabel("Longitude"));
		lon_ = new JLabel(" ");
		pane.add( lon_);

		pane.add( new JLabel("Zoom Level"));
		level_ = new JLabel(" ");
		pane.add( level_);

		add(pane, BorderLayout.SOUTH);

	}

	/**
	 * 
	 * @return
	 */
	public JXMapKit getMapKit()
	{
		return map_;
	}

	/**
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public GeoPosition convertToGeoPosition(double X, double Y)
	{
		return map_.getMainMap().convertPointToGeoPosition(new Point2D.Double( X, Y));
	}

	/**
	 * 
	 */
	public void clearOverlay()
	{
		overlays_.clear();
		painters_.clear();
		if ( this.painter_ == null ) {
		    painter_ = new OverlayWayPointPainter<JXMapViewer>();
		}
		painters_.add(painter_);
	}

	/**
	 * Add a map overlay to a map instance.
	 * @param data The mapping overlay data.
	 */
	public void addOverlay(IOverlay data) {
		data.addOverlayChangedStateListener(this);
		overlays_.add( new TransiteWayPoint( AbstractMapOverlay.class.cast(data) ));
	}
	
	public void addPainter(AbstractMapPainter<JXMapViewer> data)
	{
		data.addOverlayChangedStateListener(this);
		painters_.add(data);	
	}
	
	/**
	 * Add a map overlay to a map instance.
	 * @param data The mapping overlay data.
	 */
	public void addWaypoint(Waypoint data) {
		overlays_.add( data);
	}

	/**
	 * Called when all overlays are done.
	 */

	public void done()
	{
		if ( painter_ == null ) {
		   painter_ = new OverlayWayPointPainter<JXMapViewer>();
		   this.painters_.add(painter_);
		}
		 
		Painter paint[] = new Painter[painters_.size()];
		painters_.toArray(paint);
	
	    map_.getMainMap().setOverlayPainter(new CompoundPainter<JXMapKit>(paint));
	    
		painter_.clearCache();
		painter_.setWaypoints(overlays_);
		
		render_ = new TransiteWayPointerRender();
		painter_.setRenderer(render_);
	    map_.repaint();
	}
	
	/**
	 * Called when all overlays are done.
	 */
	public void redraw()
	{
		map_.repaint();
	}
	
	/**
	 * 
	 */
	@Override
	public void overlayChangedEvent(OveralyChangedEvent event) {
		map_.repaint();
	}
	
	/**
	 * 
	 * @param type
	 */
	public void removeWaypointOverlayType(Class<?> type) 
	{
		List<Waypoint> toRemove = new ArrayList<Waypoint>();
		
		for ( Waypoint wp : this.overlays_ ) {
			if ( wp.getClass().equals(type) ) {
				toRemove.add(wp);
			}
		}
		
		for ( Waypoint wp : toRemove) {
			this.overlays_.remove(wp);
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	private class CursorMove implements MouseMotionListener {

		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stubs
		}

		public void mouseMoved(MouseEvent e) {

			GeoPosition location = map_.getMainMap().convertPointToGeoPosition(new Point2D.Double( e.getPoint().x, e.getPoint().y));

			lat_.setText( String.format("%3.6f", location.getLatitude()));
			lon_.setText( String.format("%3.6f", location.getLongitude()));

			level_.setText( Integer.toString(map_.getMainMap().getZoom()));
		}

	}

	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	
	private class PointAndClick extends java.awt.event.MouseAdapter
	{
		public void mouseClicked(java.awt.event.MouseEvent e)
		{
			if(e.getButton() == MouseEvent.BUTTON1)
			{
				if(e.getClickCount() == 1)
				{
				    //getCrimeInfo(e.getX(), e.getY());
				}
				else if(e.getClickCount() == 2)
				{
					GeoPosition location = map_.getMainMap().convertPointToGeoPosition(new Point2D.Double( e.getPoint().x, e.getPoint().y));
					map_.setCenterPosition(location);
				}
			}

		}
	}

}
