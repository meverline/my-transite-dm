package me.datamining;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import me.datamining.Kernel.Epanechnikov;
import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.bandwidth.SlivermanRule;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridOverlay;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public abstract class AbstractDensityEstimateAlgorithm implements DensityEstimateAlgorithm {

	private  IDensityKernel denstiyKernel = new Epanechnikov();
	private  IBandwidth xBandWidth = new SlivermanRule();
	private  IBandwidth yBandWidth = new SlivermanRule();
    private  DescriptiveStatistics xstats_ = new DescriptiveStatistics();
    private  DescriptiveStatistics ystats_ = new DescriptiveStatistics();
	private  AbstractSpatialGridOverlay grid = null;
	private  List<SpatialSamplePoint> sampleValues = null;
	private  double crossCovariance = 0.0;

	/**
	 * 
	 */
	protected AbstractDensityEstimateAlgorithm() {
		
	}
	
	/**
	 * 
	 * @param kernel
	 * @param xsmothParm
	 * @param ysmothParm
	 */
	protected AbstractDensityEstimateAlgorithm(IDensityKernel kernel, IBandwidth xsmothParm, IBandwidth ysmothParm ) {
		denstiyKernel = kernel;
		xBandWidth = xsmothParm;
		yBandWidth = ysmothParm;
	}
	
	/**
	 * 
	 * @return
	 */
	public IDensityKernel getDenstiyKernel() {
		return denstiyKernel;
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.DensityEstimateAlgorithm#setDenstiyKernel(me.datamining.Kernel.IDensityKernel)
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

	/*
	 * (non-Javadoc)
	 * @see me.datamining.DensityEstimateAlgorithm#setXBandWidth(me.datamining.bandwidth.IBandwidth)
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

	/*
	 * (non-Javadoc)
	 * @see me.datamining.DensityEstimateAlgorithm#setYBandWidth(me.datamining.bandwidth.IBandwidth)
	 */
	public void setYBandWidth(IBandwidth yBandWidth) {
		this.yBandWidth = yBandWidth;
	}

	/**
	 * @return the theGrid
	 */
	public AbstractSpatialGridOverlay getGrid() {
		return grid;
	}
	
	/**
	 * @param theGrid the theGrid to set
	 */
	public void setGrid(AbstractSpatialGridOverlay theGrid) {
		this.grid = theGrid;
	}

	/**
	 * @return the sampleValues
	 */
	public List<SpatialSamplePoint> getSampleValues() {
		return sampleValues;
	}

	/**
	 * @param sampleValues the sampleValues to set
	 */
	public void setSampleValues(List<SpatialSamplePoint> sampleValues) {
		this.sampleValues = sampleValues;
	}
	
	/**
	 * @return the crossCovariance
	 */
	public double getCrossCovariance() {
		return crossCovariance;
	}

	/**
	 * @param crossCovariance the crossCovariance to set
	 */
	public void setCrossCovariance(double crossCovariance) {
		this.crossCovariance = crossCovariance;
	}
	
	/**
	 * 
	 * @param fileName
	 */
	public void dumpGrid(String fileName) throws FileNotFoundException 
	{
		PrintStream ps = new PrintStream(new FileOutputStream(fileName));

		for ( int  r = 0; r < grid.getRows(); r++) {
			for ( int c = 0; c< grid.getCols(); c++) {
				if ( c != 0 ) { ps.print(","); }
				if ( grid.get(r,c).getData() != null ) {
					ps.print( grid.get(r,c).getData().getInterpolationValue());
				}
			}
			ps.println();
		}
		ps.close();
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.datamining.DensityEstimateAlgorithm#saveGird(java.lang.String, java.lang.String)
	 */
    public void saveGird(String fileName, String dir) throws FileNotFoundException
    {
        //if (dir == null) {
        //   dumpGrid(grid.getClass().getSimpleName() + "_"+ fileName);
        //} else {
        //   dumpGrid(dir + "/" + grid.getClass().getSimpleName() + "_"+ fileName);
        // }
    }
    
	/*
	 * (non-Javadoc)
	 * @see me.datamining.DensityEstimateAlgorithm#init(me.math.grid.UniformSpatialGrid, java.util.List)
	 */
	public void init(AbstractSpatialGridOverlay theGrid) {
		this.setGrid(theGrid);
		setCrossCovariance(this.crossCovariance(getSampleValues()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.datamining.DensityEstimateAlgorithm#kernalDensityEstimate(me.math.grid.UniformSpatialGrid, java.util.List)
	 */
	public void kernalDensityEstimate(AbstractSpatialGridOverlay theGrid) {
		this.init(theGrid);
		this.kernalDensityEstimate(getDenstiyKernel(), getXBandWidth(), getYBandWidth());
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.datamining.densityEstimate.DensityEstimate#kernalDensityEstimate(me.datamining.Kernel.IDensityKernel, me.datamining.bandwidth.IBandwidth)
	 */
    public void kernalDensityEstimate(IDensityKernel kernel, IBandwidth smothParm)
    {
    		kernalDensityEstimate(kernel, smothParm, new SlivermanRule() );
    }
    
    /**
     * 
     */
    protected double crossCovariance(List<SpatialSamplePoint> samples)
    {
            double totalAvgLon = 0.0;
            double totalAvgLat = 0.0;
            
            for (SpatialSamplePoint data : samples) {
                    totalAvgLat += data.getGridPoint().getPointVertex().getLatitudeDegress();
                    totalAvgLon += data.getGridPoint().getPointVertex().getLongitudeDegress();
            }

            totalAvgLat = totalAvgLat / samples.size();
            totalAvgLon = totalAvgLon / samples.size();

            Vertex averagePoint = new Vertex( totalAvgLat, totalAvgLon);

            double d_latitude = 0.0;
            double d_longitude = 0.0;

            for (SpatialSamplePoint data : samples)  {
                    d_latitude += data.getGridPoint().getPointVertex().getLatitudeDegress() - totalAvgLat;
                    d_longitude += data.getGridPoint().getPointVertex().getLongitudeDegress() - totalAvgLat;
                    
                    getXstats().addValue(data.getValue());
                    getYstats().addValue( averagePoint.distanceFrom(data.getGridPoint().getPointVertex()));
            }

            d_latitude = d_latitude / ( samples.size() - 1.0);
            d_longitude = d_longitude / ( samples.size() - 1.0);

            return Math.abs((d_latitude * d_longitude) / ( samples.size() - 1.0));
    }

	/**
	 * @return the xstats_
	 */
	public DescriptiveStatistics getXstats() {
		return xstats_;
	}

	/**
	 * @return the ystats_
	 */
	public DescriptiveStatistics getYstats() {
		return ystats_;
	}
    
    

}
