package me.datamining.jobs;

import java.util.Iterator;
import java.util.List;

import me.datamining.DensityEstimateAlgorithm;
import me.datamining.metric.AbstractSpatialMetric;
import me.datamining.metric.IDataProvider;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.data.DensityEstimateDataSample;

public class DensityEstimateLocalJob extends AbstractDMJob {
	
	private final DensityEstimateAlgorithm densityEstimateAlgorithm;
	
	public DensityEstimateLocalJob(IPopulateGrid pg, DensityEstimateAlgorithm densityEstimateAlgorithm) {
		super(pg);
		this.densityEstimateAlgorithm = densityEstimateAlgorithm;
	}
	
	/* (non-Javadoc)
	 * @see me.datamining.DMJob#getDataSample()
	 */
	public AbstractDataSample getDataSample() {
		 return new DensityEstimateDataSample();
	 }

	/* (non-Javadoc)
	 * @see me.datamining.DMJob#process(java.util.Iterator, me.datamining.metric.AbstractSpatialMetric)
	 */
	@Override
	public boolean process(Iterator<IDataProvider> dataList, AbstractSpatialMetric metric) {
		
		gridPopulator.populate(dataList, metric, this.grid_, this);
		densityEstimateAlgorithm.kernalDensityEstimate(grid_);
		return true;
	}

	/* (non-Javadoc)
	 * @see me.datamining.DMJob#getResults(double)
	 */
	@Override
	public Iterator<AbstractSpatialGridPoint> getResults(double minValue) {
		return new KDEUniformGridIterator(minValue);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////
	
	public class KDEUniformGridIterator implements Iterator<AbstractSpatialGridPoint>
	{
		private List<AbstractSpatialGridPoint> pointsWithValues = null;
		
		public KDEUniformGridIterator(double minValue) {
			
			for ( AbstractSpatialGridPoint pt : grid_.getGridPoints()) {
				if( pt.getData().getInterpolationValue() >= minValue ) {
					pointsWithValues.add(pt);
				}
			}
		}

		public boolean hasNext() {
			return pointsWithValues.isEmpty();
		}

		public AbstractSpatialGridPoint next() {
			return pointsWithValues.remove(0);
		}

		public void remove() {
		}
		
	}

}
