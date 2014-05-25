package me.datamining.mapreduce;

import java.util.List;

import me.datamining.Kernel.Epanechnikov;
import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.bandwidth.SlivermanRule;
import me.math.grid.tiled.SpatialTile;
import me.math.grid.tiled.TiledSpatialGridPoint;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TiledNonAdaptiveKDE {

	private  IDensityKernel denstiyKernel = new Epanechnikov();
	private  IBandwidth xBandWidth = new SlivermanRule();
	private  IBandwidth yBandWidth = new SlivermanRule();
	private  double crossCovariance_ = 0.0;
	private  double variance_ = 0.0;
	
	public TiledNonAdaptiveKDE(double crossCov, double var)
	{
		crossCovariance_ = crossCov;
		variance_ = var;
	}
	
	/**
	 * 
	 * @return
	 */
	public IDensityKernel getDenstiyKernel() {
		return denstiyKernel;
	}

	/**
	 * 
	 * @param denstiyKernel
	 */
	public void setDenstiyKernel(IDensityKernel denstiyKernel) {
		this.denstiyKernel = denstiyKernel;
	}

	/**
	 * @return the xBandWidth
	 */
	public IBandwidth getXBandWidth() {
		return xBandWidth;
	}

	/**
	 * 
	 * @param xBandWidth
	 */
	public void setXBandWidth(IBandwidth xBandWidth) {
		this.xBandWidth = xBandWidth;
	}

	/**
	 * @return the yBandWidth
	 */
	public IBandwidth getYBandWidth() {
		return yBandWidth;
	}

	/**
	 * 
	 * @param yBandWidth
	 */
	public void setYBandWidth(IBandwidth yBandWidth) {
		this.yBandWidth = yBandWidth;
	}
	
	/**
	 * @return the crossCovariance
	 */
	public double getCrossCovariance() {
		return crossCovariance_;
	}

	/**
	 * @param crossCovariance the crossCovariance to set
	 */
	public void setCrossCovariance(double crossCovariance) {
		this.crossCovariance_ = crossCovariance;
	}
	
	/**
	 * @return the crossCovariance
	 */
	public double getVariance() {
		return variance_;
	}

	/**
	 * @param crossCovariance the crossCovariance to set
	 */
	public void setVariance(double crossCovariance) {
		this.variance_ = crossCovariance;
	}
	
	/**
	 * 
	 * @param gridTile
	 * @param aList
	 */
    public void kernalDensityEstimate(SpatialTile gridTile, List<SpatialTile> aList)
    {
    	DescriptiveStatistics data = new DescriptiveStatistics();
    	double hparm = getXBandWidth().bandWidth(this.getVariance(), 2, data);
    	double hGeoParm = getYBandWidth().bandWidth( this.getCrossCovariance(), 2, data);
           
    	for ( TiledSpatialGridPoint cnt : gridTile.getGrid()) {
    	   double estitmate = 0.0;
    	   for ( SpatialTile tile : aList ) {
                for (TiledSpatialGridPoint gridPt : tile.getGrid() ) {
                	    double t = 0;
                	    if ( gridPt.getData() != null ) {
                	    	t = gridPt.getData().getValue() - cnt.getData().getValue();
                	    } 
                        double X = (1.0 / hparm)* getDenstiyKernel().kernelValue(t / hparm);

                        double dist = gridPt.getVertex().distanceFrom(cnt.getPoint().getPointVertex());
                        double Y = (1.0 / hGeoParm) * getDenstiyKernel().kernelValue(dist / hGeoParm);

                        estitmate += (Math.sqrt(Math.abs(Math.pow(X, 2)) + Math.abs(Math.pow(Y, 2))));
                }
            }
    	   
    	    cnt.getData().setInterpolationValue(estitmate);
        }

        return;
    }
	
}
