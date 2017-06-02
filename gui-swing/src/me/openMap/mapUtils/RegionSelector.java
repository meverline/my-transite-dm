package me.openMap.mapUtils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Rectangle;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.RectanglePainter;

public class RegionSelector implements MouseListener, MouseMotionListener {

	private JXMapViewer map;
	private MouseListener[] oldMouseListeners;
	private MouseMotionListener[] oldMouseMotionListeners;
	
	@SuppressWarnings("rawtypes")
	private Painter oldPainter;

	private RegionSelectListener listener;

	private int topY, botY, rightX, leftX, origX, origY;
	private Rectangle viewRect;
	private static final Color fillColor = new Color(0,0,0,50);
	private boolean startDrag = false;

	private static final int strokeWidth = 3;

	public interface RegionSelectListener
	{
		public void regionSelected(GeoPosition nw, GeoPosition se);
	};

	public void startSelect(JXMapKit mapKit, RegionSelectListener listener)
	{
		this.map = mapKit.getMainMap();
		this.listener = listener;
		this.oldMouseListeners = map.getMouseListeners();
		for(MouseListener ml : oldMouseListeners)
		{
			map.removeMouseListener(ml);
		}
		map.addMouseListener(this);

		this.oldMouseMotionListeners = map.getMouseMotionListeners();
		for(MouseMotionListener mml : oldMouseMotionListeners)
		{
			map.removeMouseMotionListener(mml);
		}
		map.addMouseMotionListener(this);
		

		viewRect = map.getViewportBounds();
		oldPainter = map.getOverlayPainter();
	}

	public void stopSelect()
	{
		map.setOverlayPainter(oldPainter);
		map.removeMouseListener(this);
		map.removeMouseMotionListener(this);
		for(MouseListener ml : oldMouseListeners)
		{
			map.addMouseListener(ml);
		}
		for(MouseMotionListener mml : oldMouseMotionListeners)
		{
			map.addMouseMotionListener(mml);
		}
		startDrag = false;
	}

	private void setCoords(int x, int y)
	{
		if(x > origX)
		{
			rightX = x;
			leftX = origX;
		}
		else
		{
			rightX = origX;
			leftX = x;
		}

		if(y < origY)
		{
			topY = y;
			botY = origY;
		}
		else
		{
			topY = origY;
			botY = y;
		}
		
		listener.regionSelected(map.convertPointToGeoPosition(new Point2D.Double( leftX, topY)),
								map.convertPointToGeoPosition(new Point2D.Double( rightX, botY)));

		drawRect();
	}

	private void drawRect()
	{
		RectanglePainter rectPainter = new RectanglePainter(topY,
																				leftX,
																				(int)viewRect.getHeight() - botY,
																				(int)viewRect.getWidth() - rightX,
																				0, 0, false, fillColor, strokeWidth, Color.RED);
		map.setOverlayPainter(new CompoundPainter<JXMapKit>(oldPainter, rectPainter));
	}

	private void finishSelect()
	{
		stopSelect();
		
		listener.regionSelected(map.convertPointToGeoPosition(new Point2D.Double( leftX, topY)),
							    map.convertPointToGeoPosition(new Point2D.Double( rightX, botY)));
	}

	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			origY =e.getY()-strokeWidth;
			origX = e.getX()-strokeWidth;
			this.startDrag = true;
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			setCoords(e.getX(), e.getY());
			//finish
			finishSelect();
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		//if(e.getButton() == MouseEvent.BUTTON1)
		//{
		//	setCoords(e.getX(), e.getY());
			//continue to drag
		//}
		if ( this.startDrag ) {
			setCoords(e.getX(), e.getY());
		}
	}

	public void mouseMoved(MouseEvent e)
	{

	}

}
