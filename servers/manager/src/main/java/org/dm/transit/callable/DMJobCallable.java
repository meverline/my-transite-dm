package org.dm.transit.callable;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.datamining.DataMiningJob;
import me.datamining.mapreduce.DataResult;
import me.math.Vertex;
import me.math.grid.tiled.TiledSpatialGrid;
import org.dm.transit.metric.DataMiningMetric;
import org.dm.transit.metric.MetricFactory;

import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unused")
public class DMJobCallable implements Callable<Void> {

    private final DataMiningJob job;
    private final MetricFactory metricFactory;
    
    private final AmazonSQS sqs;
    private final String reduceUrl;
    private final ObjectMapper decoder = new ObjectMapper();

    public DMJobCallable(String url, DataMiningJob job, MetricFactory metricFactory)
    {
        this.sqs = AmazonSQSClientBuilder.defaultClient();
        this.reduceUrl = url;
        this.job = job;
        this.metricFactory = metricFactory;
    }

    @Override
    public Void call() throws Exception {

        DataMiningMetric metric = this.metricFactory.create(job);

        List<DataResult> resultList = metric.findDataResults();
        Vertex upperLeft = job.getShape().getUpperLeft();
        Vertex lowerRight = job.getShape().getLowerRight();

        TiledSpatialGrid grid = new TiledSpatialGrid(upperLeft, lowerRight, job.getGridSpaceInMeters());

        /// populate grid
        ///
        ///
        return null;
    }
}
