package me.datamining;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.datamining.Kernel.Epanechnikov;
import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.bandwidth.SlivermanRule;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGrid;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

@Getter
@Setter
public abstract class AbstractDensityEstimateAlgorithm implements DensityEstimateAlgorithm {

	private  IDensityKernel denstiyKernel = new Epanechnikov();
	private  IBandwidth xBandWidth = new SlivermanRule();
	private  IBandwidth yBandWidth = new SlivermanRule();
    private  final DescriptiveStatistics xstats = new DescriptiveStatistics();
    private  final DescriptiveStatistics ystats = new DescriptiveStatistics();
	private AbstractSpatialGrid grid = null;
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
	public void init(AbstractSpatialGrid theGrid) {
		this.setGrid(theGrid);
		setCrossCovariance(this.crossCovariance(getSampleValues()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.datamining.DensityEstimateAlgorithm#kernalDensityEstimate(me.math.grid.UniformSpatialGrid, java.util.List)
	 */
	public void kernalDensityEstimate(AbstractSpatialGrid theGrid) {
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

}
