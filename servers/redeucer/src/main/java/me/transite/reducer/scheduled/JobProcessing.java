package me.transite.reducer.scheduled;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.database.mongo.MongoDocumentSession;
import me.datamining.ComputeTile;
import me.datamining.PopulateTile;
import me.datamining.TileJob;
import me.transite.reducer.callable.ComputeReduceCallable;
import me.transite.reducer.callable.PopulateReduceCallable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class JobProcessing {
	
	private final Log log = LogFactory.getLog(getClass().getName());
	private final ExecutorService executor;
	private final AmazonSQS sqs;
	private final String reducerUrl;
	private final ObjectMapper decoder = new ObjectMapper();
	private final ComputeReduceCallable computeReduce;
	private final PopulateReduceCallable populateReduce;
	private final MongoDocumentSession documentDao;
	
	@Autowired
	public JobProcessing(Environment env, ComputeReduceCallable computeReduce, PopulateReduceCallable populateReduce, MongoDocumentSession documentDao) {
		this.computeReduce = Objects.requireNonNull(computeReduce,"computeReduce can not be null");
		this.populateReduce = Objects.requireNonNull(populateReduce,"populateReduce can not be null");
		this.documentDao = Objects.requireNonNull(documentDao,"documentDao can not be null");
		this.executor = Executors.newFixedThreadPool(Integer.parseInt(env.getProperty("jobs.maxThreads")));
		this.sqs = AmazonSQSClientBuilder.defaultClient();
		this.reducerUrl = env.getProperty("jobs.sqs.reduceUrl");
	}

	@Scheduled(fixedRate = 10000) // Time in milliseconds 10 seconds
	public void scheduleFixedRateTask() {
		final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(reducerUrl);
        final List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		final List<Future<TileJob>> list = new ArrayList<>();

        for (final Message message : messages) {
        	try {
        		String body = message.getBody();
        		Object obj = decoder.readValue(body, TileJob.class);
				Callable<TileJob> callable;

				if ( obj instanceof PopulateTile) {
					callable = new PopulateReduceCallable(populateReduce, PopulateTile.class.cast(obj), documentDao);
					list.add( this.executor.submit(callable));
				} else if ( obj instanceof ComputeTile) {
					callable = new ComputeReduceCallable(computeReduce, ComputeTile.class.cast(obj), documentDao);
					list.add( this.executor.submit(callable));
				} else {
					log.warn("unkown tila job type: " + body);
				}

        	} catch ( Exception ex) {
        		log.error(ex.getLocalizedMessage(), ex);
        	}
        }

	}

}
