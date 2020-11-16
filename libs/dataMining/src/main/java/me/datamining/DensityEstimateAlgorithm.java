package me.datamining;

import java.io.FileNotFoundException;

import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.math.grid.AbstractSpatialGrid;

public interface DensityEstimateAlgorithm {
	
	/**
	 * 
	 * @param kernel
	 */
	public void setDenstiyKernel(IDensityKernel kernel);
	
	/**
	 * 
	 * @param smothParm
	 */
	public void setXBandWidth(IBandwidth smothParm);
	
	/**
	 * 
	 * @param smothParm
	 */
	public void setYBandWidth(IBandwidth smothParm);
	
	/**
	 * 
	 * @param fileName
	 * @param dir
	 * @throws FileNotFoundException
	 */
	public void saveGird(String fileName, String dir) throws FileNotFoundException;

	/**
	 *
	 * @param theGrid
	 */
	public void init(AbstractSpatialGrid theGrid);

	/**
	 *
	 * @param theGrid
	 */
	public void kernalDensityEstimate(AbstractSpatialGrid theGrid);

	/**
	 * 
	 * @param kernel
	 * @param smothParm
	 */
	public void kernalDensityEstimate(IDensityKernel kernel,
			IBandwidth smothParm);

	/**
	 * 
	 * @param kernel
	 * @param xsmothParm
	 * @param ysmothParm
	 */
	public void kernalDensityEstimate(IDensityKernel kernel,
			IBandwidth xsmothParm, IBandwidth ysmothParm);

}