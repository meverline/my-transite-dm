/**
 * 
 */
package me.datamining.jobs;

import java.util.Iterator;
import java.util.List;

import me.datamining.ClusteringAlgorithm;
import me.datamining.metric.AbstractSpatialMetric;
import me.datamining.metric.IDataProvider;
import me.math.grid.SpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.data.STINGDataSample;
/**
 * @author markeverline
 *
 */
public class ClusteringLocalJob extends AbstractDMJob {
	
	private List<SpatialGridPoint> clusters_ = null;
	private final ClusteringAlgorithm clusteringAlgorithm;
	
	public ClusteringLocalJob(IPopulateGrid pg, ClusteringAlgorithm clusteringAlgorithm) {
		super(pg);
		this.clusteringAlgorithm = clusteringAlgorithm;
	}
	
	/* (non-Javadoc)
	 * @see me.datamining.DMJob#getDataSample()
	 */
	public AbstractDataSample getDataSample() {
		 return new STINGDataSample();
	 }

	/* (non-Javadoc)
	 * @see me.datamining.DMJob#process(java.util.Iterator, me.datamining.metric.AbstractSpatialMetric)
	 */
	@Override
	public boolean process(Iterator<IDataProvider> dataList, AbstractSpatialMetric metric) {
		gridPopulator.populate(dataList, metric, this.grid_, this);
		clusters_ = clusteringAlgorithm.findClusters(grid_);
		return true;
	}

	/* (non-Javadoc)
	 * @see me.datamining.DMJob#getResults(double)
	 */
	@Override
	public Iterator<SpatialGridPoint> getResults(double minValue) {
		return new STINGUniformGridIterator(minValue);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////
	
	public class STINGUniformGridIterator implements Iterator<SpatialGridPoint>
	{
		private List<SpatialGridPoint> pointsWithValues = null;
		
		public STINGUniformGridIterator(double minValue) {
			
			for ( SpatialGridPoint pt : clusters_) {
				if( pt.getData().getInterpolationValue() >= minValue ) {
					pointsWithValues.add(pt);
				}
			}
		}

		public boolean hasNext() {
			return pointsWithValues.isEmpty();
		}

		public SpatialGridPoint next() {
			return pointsWithValues.remove(0);
		}

		public void remove() {
		}
		
	}


}
