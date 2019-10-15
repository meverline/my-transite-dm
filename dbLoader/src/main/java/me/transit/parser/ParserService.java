package me.transit.parser;

import java.util.List;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.transit.parser.message.MessageAgency;
import me.transit.parser.message.ParserMessage;

public class ParserService extends AbstractGTFSParser {
	
	/**
	 * 
	 */
	public void poll(AmazonSQS sqs, String myQueueUrl) {
		final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
        final List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        
        for (final Message message : messages) {
        	try {
	        	ParserMessage fr = new ObjectMapper().readValue(message.getMD5OfBody(), ParserMessage.class);
				for ( MessageAgency agency : fr.getAgencys()) {
					parseFeeds(agency);
				}
        	} catch ( Exception ex) {
        		this.getLog().error(ex.getLocalizedMessage(), ex);
        	}
        }
        
        final String messageReceiptHandle = messages.get(0).getReceiptHandle();
        sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl,
                messageReceiptHandle));
	}
	
	/**
	 * 
	 */
	@Override
	public void start() {
		AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	}
}
