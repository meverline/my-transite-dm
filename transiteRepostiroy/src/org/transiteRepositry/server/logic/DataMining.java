package org.transiteRepositry.server.logic;

import java.util.List;

import org.transiteRepositry.server.request.ClusterQueryRequest;
import org.transiteRepositry.server.request.GenerateHeatMapRequest;

import me.math.grid.SpatialGridPoint;;

public interface DataMining {

	public String createHeatMap(GenerateHeatMapRequest request);
	
	public List<SpatialGridPoint> findClusters(ClusterQueryRequest request);
}
