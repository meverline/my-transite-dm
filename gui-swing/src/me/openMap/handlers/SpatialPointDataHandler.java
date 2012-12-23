package me.openMap.handlers;

import java.util.List;

import me.math.grid.SpatialGridPoint;
import me.openMap.OpenTransitMap;
import me.openMap.utils.SpatialPointOverlay;

public class SpatialPointDataHandler implements DataDisplayHandler {

	List<SpatialGridPoint> data = null;
	
	public SpatialPointDataHandler(List<SpatialGridPoint>  results)
	{
		data = results;
	}
	
	/**
	 * @return the data
	 */
	public List<SpatialGridPoint> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<SpatialGridPoint> data) {
		this.data = data;
	}

	@Override
	public void processDataToGui(OpenTransitMap gui) {
		
		for ( SpatialGridPoint gp : data) {
			gui.getMap().addOverlay( new SpatialPointOverlay(gp, gp.Grid().getGridSpacingMeters()));
		}
	}
	
	

}
