package me.datamining;

import java.util.List;

import me.math.grid.SpatialGridPoint;
import me.math.grid.UniformSpatialGrid;

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
	public void init(UniformSpatialGrid aGrid);
	
	/**
	 * 
	 * @param aGrid
	 * @return
	 */
	public List<SpatialGridPoint> findClusters(UniformSpatialGrid aGrid);
	
	/**
	 * 
	 * @return
	 */
	public abstract List<SpatialGridPoint> findClusters();

}