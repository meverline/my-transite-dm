package org.transiteRepositry.server.request;

import java.util.ArrayList;
import java.util.List;

import org.transiteRepositry.server.request.utils.Distance;
import org.transiteRepositry.server.request.utils.Metric;

import me.utils.GradientParameters;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("GenerateHeatMapRequest")
public class GenerateHeatMapRequest extends AbstractSpatialQuery {

	@XStreamAlias("Metric")
	private Metric metric = null;
	@XStreamAlias("Distance")
	private Distance distance = null;
	@XStreamAlias("Grandiante")
	private List<GradientParameters> gradianteParameters = new ArrayList<GradientParameters>();
	
	/**
	 * @return the metric
	 */
	public Metric getMetric() {
		return metric;
	}
	/**
	 * @param metric the metric to set
	 */
	public void setMetric(Metric metric) {
		this.metric = metric;
	}
	/**
	 * @return the distance
	 */
	public Distance getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Distance distance) {
		this.distance = distance;
	}
	/**
	 * @return the gradianteParameters
	 */
	public List<GradientParameters> getGradianteParameters() {
		return gradianteParameters;
	}
	/**
	 * @param gradianteParameters the gradianteParameters to set
	 */
	public void setGradianteParameters(List<GradientParameters> gradianteParameters) {
		this.gradianteParameters = gradianteParameters;
	}
	
}
