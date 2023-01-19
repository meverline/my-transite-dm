package org.dm.transit.dataMine;


import me.datamining.Kernel.Epanechnikov;
import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.bandwidth.SlivermanRule;
import me.math.grid.SpatialGridPoint;
import me.math.grid.tiled.SpatialTile;

import java.util.List;

public class TiledNonAdaptiveKDE {

    private IDensityKernel kernel = new Epanechnikov();
    private IBandwidth xBandWidth = new SlivermanRule();
    private IBandwidth yBandWidth = new SlivermanRule();
    private double variance;
    private long number;

    public TiledNonAdaptiveKDE(double variance, long number)
    {
       this.number = number;
       this.variance = variance;
    }

    /**
     *
     * @return
     */
    public IDensityKernel getDenstiyKernel() {
        return kernel;
    }

    /**
     *
     * @param kernel
     */
    public void setDenstiyKernel(IDensityKernel kernel) {
        this.kernel = kernel;
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
    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
         this.variance = variance;
    }

    /**
     * @return the crossCovariance
     */
    public long getN() {
        return number;
    }

    /**
     * @return the crossCovariance
     */
    public void setN(long number) {
        this.number = number;
    }

    /**
     *
     * @param gridTile
     * @param gridPoints
     */
    public void kernalDensityEstimate(SpatialTile gridTile, List<SpatialGridPoint> gridPoints)
    {
        double hparm = getXBandWidth().bandWidth(this.getVariance(), 2, this.getN());
        double hGeoParm = getYBandWidth().bandWidth( gridTile.getCcdata().crossCovariance(), 2, this.getN());

        for ( SpatialGridPoint cnt : gridTile.getGrid()) {
            double estitmate = 0.0;
            for ( SpatialGridPoint data : gridPoints) {
                double t = 0;
                if ( cnt.getData() != null ) {
                    t = cnt.getData().getValue() -  data.getData().getValue();
                }
                double X = (1.0 / hparm)* getDenstiyKernel().kernelValue(t / hparm);

                double dist = cnt.getVertex().distanceFrom(data.getVertex());
                double Y = (1.0 / hGeoParm) * getDenstiyKernel().kernelValue(dist / hGeoParm);

                estitmate += (Math.sqrt(Math.abs(Math.pow(X, 2)) + Math.abs(Math.pow(Y, 2))));
            }

            cnt.getData().setInterpolationValue(estitmate);
        }

        return;
    }
}
