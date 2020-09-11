package me.transit.parser.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.omd.dao.LocationDao;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.FileHandlerFactory;
import me.transit.parser.message.ParserMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

public class ParserService extends AbstractGTFSParser {

    private final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    private final String url;
    private final IGraphDatabaseDAO graph;
    private boolean stopPolling = false;

    /**
     *
     * @param factory
     * @param locationDao
     * @param blackboard
     * @param graphDatabase
     * @param uri
     */
    @Autowired
    public ParserService(FileHandlerFactory factory, LocationDao locationDao, Blackboard blackboard,
                         IGraphDatabaseDAO graphDatabase, String uri) {
        super(factory, locationDao, blackboard);
        this.url = uri;
        this.graph = Objects.requireNonNull(graphDatabase, "graphDatabase can not be null");
    }

    /**
     *
     */
    @Override
    public void start() {
        final ReceiveMessageRequest request = new ReceiveMessageRequest(this.url).withWaitTimeSeconds(30);

        while ( ! this.stopPolling ) {
            final List<Message> messages = sqs.receiveMessage(request).getMessages();
            for (final Message message : messages) {
                try {
                    this.messageProcessor(new ObjectMapper().readValue(message.getMD5OfBody(), ParserMessage.class));
                } catch (JsonProcessingException ex) {
                    this.getLog().error(ex.getLocalizedMessage(), ex);
                }
            }

            final String messageReceiptHandle = messages.get(0).getReceiptHandle();
            sqs.deleteMessage(new DeleteMessageRequest(this.url, messageReceiptHandle));
        }
    }

    public void stop()
    {
        this.stopPolling = true;
    }



}
