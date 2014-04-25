/**
 * 
 */
package me.datamining.jobs;

import java.util.Iterator;
import java.util.List;

import me.datamining.ClusteringAlgorithm;
import me.datamining.metric.AbstractSpatialMetric;
import me.factory.DaoBeanFactory;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.data.STINGDataSample;
import me.transit.database.TransitStop;

/**
 * @author markeverline
 *
 */
public class ClusteringLocalJob extends AbstractLocalJob {
	
	private List<AbstractSpatialGridPoint> clusters_ = null;
	/* (non-Javadoc)
	 * @see me.datamining.DMJob#getDataSample()
	 */
	protected AbstractDataSample getDataSample() {
		 return new STINGDataSample();
	 }

	/* (non-Javadoc)
	 * @see me.datamining.DMJob#process(java.util.Iterator, me.datamining.metric.AbstractSpatialMetric)
	 */
	@Override
	public boolean process(Iterator<TransitStop> dataList,
			AbstractSpatialMetric metric) {
		super.process(dataList, metric);
		ClusteringAlgorithm alog =
				ClusteringAlgorithm.class.cast(DaoBeanFactory.create().getBean(ClusteringAlgorithm.class));
		clusters_ = alog.findClusters(grid_);
		return true;
	}

	/* (non-Javadoc)
	 * @see me.datamining.DMJob#getResults(double)
	 */
	@Override
	public Iterator<AbstractSpatialGridPoint> getResults(double minValue) {
		return new STINGUniformGridIterator(minValue);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////
	
	public class STINGUniformGridIterator implements Iterator<AbstractSpatialGridPoint>
	{
		private List<AbstractSpatialGridPoint> pointsWithValues = null;
		
		public STINGUniformGridIterator(double minValue) {
			
			for ( AbstractSpatialGridPoint pt : clusters_) {
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
