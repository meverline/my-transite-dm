package me.datamining;

import java.util.List;

import me.math.grid.AbstractSpatialGrid;
import me.math.grid.SpatialGridPoint;

public interface ClusteringAlgorithm {

	/**
	 * 
	 * @param rangeHi_
	 */
	public void setRangeHi(int rangeHi_);
	
	/**
	 * 
	 * @param rangeLow_
	 */
	public void setRangeLow(int rangeLow_);
	
	/**
	 * 
	 * @param aGrid
	 */
	public void init(AbstractSpatialGrid aGrid);
	
	/**
	 * 
	 * @param aGrid
	 * @return
	 */
	public List<SpatialGridPoint> findClusters(AbstractSpatialGrid aGrid);
	
	/**
	 * 
	 * @return
	 */
	public abstract List<SpatialGridPoint> findClusters();

}