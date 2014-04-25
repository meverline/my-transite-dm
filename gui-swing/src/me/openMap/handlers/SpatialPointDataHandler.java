package me.openMap.handlers;

import java.util.List;

import me.math.grid.AbstractSpatialGridPoint;
import me.openMap.OpenTransitMap;
import me.openMap.utils.SpatialPointOverlay;

public class SpatialPointDataHandler implements DataDisplayHandler {

	List<AbstractSpatialGridPoint> data = null;
	double gridSpacingInMeters = 0;
	
	public SpatialPointDataHandler(List<AbstractSpatialGridPoint>  results, double gridSpacingInMeters)
	{
		this.gridSpacingInMeters = gridSpacingInMeters;
		data = results;
	}
	
	/**
	 */
	public List<AbstractSpatialGridPoint> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<AbstractSpatialGridPoint> data) {
		this.data = data;
	}

	@Override
	public void processDataToGui(OpenTransitMap gui) {
		
		for ( AbstractSpatialGridPoint gp : data) {
			gui.getMap().addOverlay( new SpatialPointOverlay(gp, this.gridSpacingInMeters));
		}
	}
	
	

}
