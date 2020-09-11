package org.dm.transit.scheduled;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

@Service
@PropertySource({ "classpath:persistence-${envTarget:dev}.hmj.properties" })
public class JobProcessing {
	
	private final Log log = LogFactory.getLog(getClass().getName());
	private final ExecutorService executor;
	private final AmazonSQS sqs;
	private final String url;
	
	@Autowired
	public JobProcessing(Environment env) {
		this.executor = Executors.newFixedThreadPool(Integer.parseInt(env.getProperty("jobs.maxThreads")));
		this.sqs = AmazonSQSClientBuilder.defaultClient();
		this.url = env.getProperty("jobs.sqs.url");
	}

	@Scheduled(fixedRate = 10000) // Time in milliseconds 10 seconds
	public void scheduleFixedRateTask() {
		final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(url);
        final List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        for (final Message message : messages) {
        	try {
        		String body = message.getBody();
        		
        	} catch ( Exception ex) {
        		log.error(ex.getLocalizedMessage(), ex);
        	}
        }
        
	}
	
}