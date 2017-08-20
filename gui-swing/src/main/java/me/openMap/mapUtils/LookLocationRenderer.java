package me.openMap.mapUtils;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

public class LookLocationRenderer implements WaypointRenderer {

	private static final int size = 5;

	public static int getPointSize(int zoom) {
		return size;
	}

	public boolean paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint wp) {
		int halfSize = LookLocationRenderer.getPointSize(map.getZoom());

		g.setColor(Color.black);
		halfSize -= 1;
		g.drawOval(-halfSize, -halfSize, halfSize * 2, halfSize * 2);
		halfSize += 4;
		g.drawOval(-halfSize, -halfSize, halfSize * 2, halfSize * 2);

		g.setColor(Color.red);
		halfSize -= 1;
		g.drawLine(0, -halfSize, 0, halfSize);
		g.drawLine(-halfSize, 0, halfSize, 0);

		g.drawLine(1, -halfSize, -1, halfSize);
		g.drawLine(-halfSize, 1, halfSize, -1);

		g.drawLine(-1, -halfSize, 1, halfSize);
		g.drawLine(-halfSize, -1, halfSize, 1);

		g.setColor(Color.black);
		g.drawLine(2, -halfSize, -2, halfSize);
		g.drawLine(-halfSize, 2, halfSize, -2);

		g.drawLine(-2, -halfSize, 2, halfSize);
		g.drawLine(-halfSize, -2, halfSize, 2);

		g.setColor(Color.red);
		g.drawOval(-halfSize, -halfSize, halfSize * 2, halfSize * 2);
		halfSize -= 1;
		g.drawOval(-halfSize, -halfSize, halfSize * 2, halfSize * 2);
		halfSize -= 1;
		g.drawOval(-halfSize, -halfSize, halfSize * 2, halfSize * 2);

		return true;
	}

}
