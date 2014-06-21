package me.datamining.mapreduce;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.datamining.Kernel.Epanechnikov;
import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.bandwidth.SlivermanRule;
import me.math.grid.tiled.SpatialTile;
import me.math.grid.tiled.TiledSpatialGridPoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TiledNonAdaptiveKDE implements ResultsHandler {

	public static Log log = LogFactory.getLog(TiledNonAdaptiveKDE.class);

	private  IDensityKernel denstiyKernel = new Epanechnikov();
	private  IBandwidth xBandWidth = new SlivermanRule();
	private  IBandwidth yBandWidth = new SlivermanRule();
	private  double crossCovariance_ = 0.0;
	private  double variance_ = 0.0;
	private  long number_ = 0;
	private  List<DataResult> dataPoints_ = new ArrayList<DataResult>();
	
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
	 * @return the crossCovariance
	 */
	public long getN() {
		return number_;
	}

	/**
	 * @param crossCovariance the crossCovariance to set
	 */
	public void setN(long value) {
		this.number_ = value;
	}
	
	public void handleResult(DataResult result) {
		if (result != null ) {
			this.dataPoints_.add( new DataResult(result));
		}
	}
	
	/**
	 * 
	 * @param gridTile
	 * @param aList
	 */
    public void kernalDensityEstimate(SpatialTile gridTile, InputStream dataStream)
    {
    	
    	QueryResults qr = new QueryResults();
    	qr.read(dataStream, this);
    	
    	double hparm = getXBandWidth().bandWidth(this.getVariance(), 2, this.getN());
    	double hGeoParm = getYBandWidth().bandWidth( this.getCrossCovariance(), 2, this.getN());
         
    	for ( TiledSpatialGridPoint cnt : gridTile.getGrid()) {
    	   double estitmate = 0.0;
    	   for ( DataResult data : this.dataPoints_) {
        	    double t = 0;
        	    if ( cnt.getData() != null ) {
        	    	t = cnt.getData().getValue() - data.getMetric();
        	    } 
                double X = (1.0 / hparm)* getDenstiyKernel().kernelValue(t / hparm);

                double dist = cnt.getVertex().distanceFrom(data.getPoint());
                double Y = (1.0 / hGeoParm) * getDenstiyKernel().kernelValue(dist / hGeoParm);

                estitmate += (Math.sqrt(Math.abs(Math.pow(X, 2)) + Math.abs(Math.pow(Y, 2))));
            }
    	   
    	    cnt.getData().setInterpolationValue(estitmate);
        }
    	
        return;
    }
	
}
