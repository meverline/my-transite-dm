package me.datamining.mapreduce;

import java.util.List;

import me.math.grid.tiled.SpatialTile;
import me.math.grid.tiled.TiledSpatialGridPoint;
import me.math.grid.data.AbstractDataSample;

public class TiledFinilizeKDE {

	private long numSamples_ = 0;
	
	/**
	 * 
	 * @return
	 */
	public long getNumSamples() {
		return numSamples_;
	}
	
	/**
	 * 
	 * @param crossCovariance
	 */
	public void setNumSamples(long crossCovariance) {
		this.numSamples_ = crossCovariance;
	}
	
	public void finishKDETile(SpatialTile zeroTile, List<SpatialTile> aList)
	{
	 	int index = 0;
    	for ( TiledSpatialGridPoint cnt : zeroTile.getGrid()) {    	   
     	   AbstractDataSample sample = cnt.getData();
     	   double total = sample.getInterpolationValue();
       	   for ( SpatialTile tile : aList ) {
               TiledSpatialGridPoint gridPt = tile.getGrid().get(index);
               
               total += gridPt.getData().getInterpolationValue();
       	   }   
       	   sample.setInterpolationValue( (1.0 / getNumSamples())* total);
    	}
	}

}
