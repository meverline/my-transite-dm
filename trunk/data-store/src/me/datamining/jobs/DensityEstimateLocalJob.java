package me.datamining.jobs;

import java.util.Iterator;
import java.util.List;

import me.datamining.DensityEstimateAlgorithm;
import me.datamining.metric.AbstractSpatialMetric;
import me.factory.DaoBeanFactory;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.data.DensityEstimateDataSample;
import me.transit.database.TransitStop;

public class DensityEstimateLocalJob extends AbstractLocalJob {
	
	/* (non-Javadoc)
	 * @see me.datamining.DMJob#getDataSample()
	 */
	protected AbstractDataSample getDataSample() {
		 return new DensityEstimateDataSample();
	 }

	/* (non-Javadoc)
	 * @see me.datamining.DMJob#process(java.util.Iterator, me.datamining.metric.AbstractSpatialMetric)
	 */
	@Override
	public boolean process(Iterator<TransitStop> dataList, AbstractSpatialMetric metric) {
		
		super.process(dataList, metric);
		DensityEstimateAlgorithm kde = 
				DensityEstimateAlgorithm.class.cast(DaoBeanFactory.create().getBean(DensityEstimateAlgorithm.class));
			
		kde.kernalDensityEstimate(grid_);
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

		@Override
		public boolean hasNext() {
			return pointsWithValues.isEmpty();
		}

		@Override
		public AbstractSpatialGridPoint next() {
			return pointsWithValues.remove(0);
		}

		@Override
		public void remove() {
		}
		
	}

}
