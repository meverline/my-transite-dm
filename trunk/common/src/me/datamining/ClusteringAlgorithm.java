package me.datamining;

import java.util.List;

import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;

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
	public List<AbstractSpatialGridPoint> findClusters(UniformSpatialGrid aGrid);
	
	/**
	 * 
	 * @return
	 */
	public abstract List<AbstractSpatialGridPoint> findClusters();

}