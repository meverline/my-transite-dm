package me.datamining.densityEstimate;

import java.util.List;

import me.datamining.AbstractDensityEstimateAlgorithm;
import me.datamining.SpatialSamplePoint;
import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.sample.DefaultSample;
import me.math.grid.array.SpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;

public class NonAdaptiveKDE extends AbstractDensityEstimateAlgorithm {
	
	/**
	 * 
	 */
	public NonAdaptiveKDE()
	{
	}
	
	/**
	 * 
	 * @param samples
	 * @param grid
	 */
	public NonAdaptiveKDE(List<SpatialSamplePoint> samples, UniformSpatialGrid grid)
	{
		this.init(grid, samples);
	}
			
    /*
     * (non-Javadoc)
     * @see me.datamining.densityEstimate.DensityEstimate#kernalDensityEstimate(me.datamining.Kernel.IDensityKernel, me.datamining.bandwidth.IBandwidth, me.datamining.bandwidth.IBandwidth)
     */
    public void kernalDensityEstimate(IDensityKernel kernel, IBandwidth xsmothParm, IBandwidth ysmothParm )
    {
    	
    	double hparm = xsmothParm.bandWidth(this.getXstats().getVariance(), 2, this.getXstats());
    	double hGeoParm = ysmothParm.bandWidth( this.getCrossCovariance(), 2, this.getYstats());
           
        for (int r = 0; r < this.getGrid().getRows(); r++) {
            for (int c = 0; c < this.getGrid().getCols(); c++) {
                double estitmate = 0.0;
                SpatialGridPoint gridPt =  this.getGrid().get(r, c);

                for (SpatialSamplePoint cnt : this.getSampleValues()) {
                	    double t = 0;
                	    if ( gridPt.getData() != null ) {
                	    	t = gridPt.getData().getValue() - cnt.getValue();
                	    } else {
                	    	gridPt.setData( new DefaultSample());
                	    }
                        double X = (1.0 / hparm)* kernel.kernelValue(t / hparm);

                        double dist = gridPt.getVertex().distanceFrom(cnt.toVertex());
                        double Y = (1.0 / hGeoParm) * kernel.kernelValue(dist / hGeoParm);

                        estitmate += (Math.sqrt(Math.abs(Math.pow(X, 2)) + Math.abs(Math.pow(Y, 2))));
                }

                gridPt.getData().setInterpolationValue( (1.0 / this.getSampleValues().size())* estitmate);
            }
        }

        return;
    }






}
