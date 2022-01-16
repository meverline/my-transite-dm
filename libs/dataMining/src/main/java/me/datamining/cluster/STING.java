//CIRAS: Crime Information Retrieval and Analysis System
//Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.datamining.cluster;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import me.datamining.ClusteringAlgorithm;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGrid;
import me.math.grid.SpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.math.grid.data.STINGDataSample;
import me.math.kdtree.IKDSearch;
import me.math.kdtree.INode;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@CommonsLog
@Getter
@Setter
public class STING implements ClusteringAlgorithm {

	private AbstractSpatialGrid grid_ = null;
	private int rangeHi = Integer.MAX_VALUE;
	private int rangeLow = 0;
	private double confidence = 0.5;
	private double density = 1;
	private KDTree tree_ = null;

	/**
	 * 
	 */
	public STING() {
	}

	/**
	 * 
	 * @param aGrid
	 */
	public STING(UniformSpatialGrid aGrid) {
		this.init(aGrid);
	}
	
	/**
	 * 
	 */
	public STING(int hi, int low, double confidence, double density) {
		this.rangeHi = hi;
		this.rangeLow = low;
		this.confidence = confidence;
		this.density = density;
	}

	/**
	 * 
	 * @param ul
	 * @param lr
	 * @param gridSize
	 */
	public STING(Point ul, Point lr, double gridSize) {
		this.init(new UniformSpatialGrid(ul, lr, gridSize));
	}
	
	/**
	 * 
	 * @param aGrid
	 */
	public void init(AbstractSpatialGrid aGrid)
	{
		grid_ = aGrid;
		tree_ = aGrid.getTree();
	}

	/**
	 * 
	 * @param value
	 * @param mean
	 * @param std
	 * @return
	 */
	public static double standardPDF(double value, double mean, double std) {
		if (std == 0) {
			return 0;
		}
		NormalDistribution sdf = new NormalDistribution(mean, std);
		try {
			return sdf.cumulativeProbability(value);
		} catch (NumberIsTooLargeException e) {
			return 0;
		}
	}

	/**
	 * 
	 * @param value
	 * @param lambda
	 * @return
	 */
	public static double pissonPDF(double value, double lambda) {
		if (lambda == 0) {
			return 0;
		}
		PoissonDistribution pdf = new PoissonDistribution(lambda);
		return pdf.probability((int)value);
	}

	/**
	 * 
	 * @param value
	 * @param number
	 * @param probabilty
	 * @return
	 */
	public static double binomialPDF(int value, double number, double probabilty) {
		BinomialDistribution bdf = new BinomialDistribution(
				(int) value, probabilty);

		return bdf.probability(value);
	}

	/**
	 * 
	 * @return
	 */
	public double getGridSizeInMeters() {
		return grid_.getGridSpacingMeters();
	}
	
	/* (non-Javadoc)
	 * @see me.datamining.ClusteringAlgorithm#findClusters()
	 */
	public List<SpatialGridPoint> findClusters(AbstractSpatialGrid aGrid) {
		this.init(aGrid);
		return this.findClusters();
	}

	/* (non-Javadoc)
	 * @see me.datamining.ClusteringAlgorithm#findClusters()
	 */
	public List<SpatialGridPoint> findClusters() {

		findReleventNodes nodeFinder = new findReleventNodes(
				this.getRangeLow(), this.getRangeHi(), this.getConfidence());

		tree_.searchStats(nodeFinder);

		double factor = Math.sqrt(1 / (Math.PI * this.getDensity()));
		double distance = Math.max((double) this.getGridSizeInMeters(), factor);

		HashSet<SpatialGridPoint> clusterPoints = new HashSet<>();
		List<SpatialGridPoint> possiableNodes = nodeFinder.getResults();

		ClusterNodeEvaluation evaluator = new ClusterNodeEvaluation( this.getRangeLow(), 
																	 this.getRangeHi(), 
																	 this.getConfidence());

		log.debug("STING Relvent: " + nodeFinder.getResults().size());
		log.debug("STING distance: " + distance);

		while (!possiableNodes.isEmpty()) {
			SpatialGridPoint pt = possiableNodes.remove(0);
			if (!clusterPoints.contains(pt)) {
				clusterPoints.add(pt);
			}

			List<SpatialGridPoint> check = null;
			if (distance != this.getGridSizeInMeters()) {
				check = tree_.find(new RangeSearch(pt.getVertex(), distance));
			
				for (SpatialGridPoint n : check) {
					if (n.getData() instanceof STINGDataSample) {
						STINGDataSample sample = STINGDataSample.class.cast(n);
						if (!sample.isChecked()) {
							sample.setChecked(true);
							if (evaluator.isRelevent(sample.average(),
													 sample.standardDeviation(), 
													 sample.getSampleNumber())) {
								possiableNodes.add(n);
							}
						}
					}
				}
				
			}
		}

		List<SpatialGridPoint> rtn = new ArrayList<>();
		rtn.addAll(clusterPoints);
		return rtn;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////

	@Getter
	@Setter
	public static class ClusterNodeEvaluation {

		private double min;
		private double max;
		private double confThresshold;

		public ClusterNodeEvaluation(double min, double max, double threshold) {
			this.setMax(max);
			this.setMin(min);
			this.setConfThresshold(threshold);
		}

		public double probablit(double density, double mean, double std) {
			if (std == 0) {
				return 0;
			}
			return (density * ((max - mean) / std))
					- (density * ((min - mean) / std));
		}

		public boolean isRelevent(double mean, double std, double number) {

			if (number == 0.0 && mean == 0.0 && std == 0.0) {
				return false;
			}

			double density = STING.standardPDF(number, mean, std);

			if (density < 2) {
				if (number < this.getMin() || number > this.getMax()) {
					return false;
				} else {
					return true;
				}
			}
			double prob = this.probablit(density, mean, std);
			double nci = (STING.binomialPDF((int) Math.ceil(number + 0.5f),
											 number, 
											 density)) / number;

			if (number <= 30 && nci > this.getConfThresshold()) {
				return true;
			} else if (number > 30) {
				if (prob >= 5 && (number * (1 - prob)) > 5) {
					double z = ((this.getMin() / 2) - mean) / std;
					double equ = Math.sqrt((prob * (1 - prob)) / number);

					if (z >= this.getMin()) {
						prob = prob + (z * equ);
					} else {
						prob = prob - (z * equ);
					}

				} else {
					prob = STING.pissonPDF(number, number * prob);
					if ((number * (1 - prob)) < 5) {
						prob = 1 - prob;
					}
				}
			}

			if (prob >= this.getConfThresshold()) {
				return true;
			}
			return false;
		}

	}
	
	// /////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////

	@Getter
	@Setter
	public class findReleventNodes implements IKDSearch {

		public int relevantCount_ = 0;
		private ClusterNodeEvaluation evaluator = null;
		private List<SpatialGridPoint> results = new ArrayList<>();

		public findReleventNodes(double min, double max, double threshold) {
			this.setEvaluator(new ClusterNodeEvaluation(min, max, threshold));
		}

		public boolean endSearch(INode node) {
			return false;
		}

		public Vertex getVertex() {
			return null;
		}

		public void compare(INode node) {

			SpatialGridPoint pt = node.getPoint();
			if (pt.getData() != null ) {
				STINGDataSample data = STINGDataSample.class.cast(pt.getData());
				if (getEvaluator().isRelevent(data.average(),
											  data.standardDeviation(),
											  data.getSampleNumber())) {
					data.setChecked(true);
					getResults().add(node.getPoint());
				}
			}
		}

	}

}