package org.dm.transit.scheduled;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dm.transit.callable.DMJobCallable;
import org.dm.transit.metric.MetricFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.apachecommons.CommonsLog;
import me.datamining.DataMiningJob;

@Service
@PropertySource({ "classpath:persistence-${envTarget:dev}.hmj.properties" })
@CommonsLog
public class JobProcessing {

	private final ExecutorService executor;
	@SuppressWarnings("unused")
	private final Environment env;
	private final AmazonSQS sqs;
	private final String url;
	private final ObjectMapper mapper = new ObjectMapper();
	private final MetricFactory metricFactory;
	private final String populateUrl;
	
	@Autowired
	public JobProcessing(Environment env, MetricFactory factory) {
		this.env = env;
		this.executor = Executors.newFixedThreadPool(Integer.parseInt(env.getProperty("jobs.maxThreads")));
		this.sqs = AmazonSQSClientBuilder.defaultClient();
		this.url = env.getProperty("jobs.sqs.url");
		this.metricFactory = factory;
		this.populateUrl = env.getProperty("jobs.sqs.populate");
	}

	@Scheduled(fixedRate = 1000)
	public void scheduleFixedRateTask() {
		final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(url);
        final List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        for (final Message message : messages) {
        	try {
				DataMiningJob job = this.mapper.readValue(message.getBody(), DataMiningJob.class);
				this.executor.submit(new DMJobCallable(this.populateUrl, job, metricFactory));
        	} catch ( Exception ex) {
        		log.error(ex.getLocalizedMessage(), ex);
        	}
        }
        
	}
	
}
