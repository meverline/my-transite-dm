package me.datamining;

import java.util.List;

import me.math.grid.AbstractSpatialGrid;
import me.math.grid.AbstractSpatialGridPoint;

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
	public List<AbstractSpatialGridPoint> findClusters(AbstractSpatialGrid aGrid);
	
	/**
	 * 
	 * @return
	 */
	public abstract List<AbstractSpatialGridPoint> findClusters();

}