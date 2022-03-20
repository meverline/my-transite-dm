

package org.dm.transit.scheduled;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import me.datamining.ComputeTile;
import me.datamining.PopulateTile;
import me.datamining.TileJob;
import org.dm.transit.callable.ComputeTileCallable;
import org.dm.transit.callable.PopulateJobCallable;
import org.dm.transit.callable.ReaduceHandler;
import org.dm.transit.dataMine.PopulateGrid;
import org.dm.transit.dataMine.TiledNonAdaptiveKDE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@PropertySource({ "classpath:persistence-${envTarget:dev}.hmj.properties" })
@CommonsLog
public class JobProcessing {

	private final ExecutorService executor;
	private final AmazonSQS sqs;
	private final String computeUrl;
	private final String reduceUrl;
	private final ObjectMapper decoder = new ObjectMapper();
	private final PopulateGrid populateGrid;
	private final TiledNonAdaptiveKDE tiledNonAdaptiveKDE;
	
	@Autowired
	public JobProcessing(Environment env, PopulateGrid populateGrid, TiledNonAdaptiveKDE tiledNonAdaptiveKDE) {
		this.populateGrid = Objects.requireNonNull(populateGrid,"populateGrid can not be null");
		this.tiledNonAdaptiveKDE = Objects.requireNonNull(tiledNonAdaptiveKDE,"tiledNonAdaptiveKDE can not be null");
		this.executor = Executors.newFixedThreadPool(Integer.parseInt(env.getProperty("jobs.maxThreads")));
		this.sqs = AmazonSQSClientBuilder.defaultClient();
		this.computeUrl = env.getProperty("jobs.sqs.compute");
		this.reduceUrl = env.getProperty("jobs.sqs.redeucer");
	}

	@Scheduled(fixedRate = 10000) // Time in milliseconds 10 seconds
	public void scheduleFixedRateTask() {
		final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(computeUrl);
        final List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		final List<Future<TileJob>> list = new ArrayList<>();
        for (final Message message : messages) {
        	try {
        		String body = message.getBody();
        		Object obj = decoder.readValue(body, TileJob.class);
				Callable<TileJob> callable;

        		if ( obj instanceof PopulateTile) {
        			callable = new PopulateJobCallable(populateGrid, PopulateTile.class.cast(obj));
					list.add( this.executor.submit(callable));
        		} else if ( obj instanceof ComputeTile ) {
        			callable = new ComputeTileCallable(tiledNonAdaptiveKDE, ComputeTile.class.cast(obj));
					list.add( this.executor.submit(callable));
        		} else {
        			log.warn("unkown tila job type: " + body);
				}

        	} catch ( Exception ex) {
        		log.error(ex.getLocalizedMessage(), ex);
        	}
        }
        this.executor.submit(new ReaduceHandler(this.reduceUrl, list));
	}

}
