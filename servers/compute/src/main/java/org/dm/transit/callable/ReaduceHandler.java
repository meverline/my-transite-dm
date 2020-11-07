package org.dm.transit.callable;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.datamining.TileJob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ReaduceHandler implements Callable<Void> {

    private final Log log = LogFactory.getLog(getClass().getName());
    private final List<Future<TileJob>> process;
    private final AmazonSQS sqs;
    private final String reduceUrl;
    private final ObjectMapper decoder = new ObjectMapper();

    public ReaduceHandler(String url,  List<Future<TileJob>> process) {
        this.sqs = AmazonSQSClientBuilder.defaultClient();
        this.process = Objects.requireNonNull(process,"process can not be null");
        this.reduceUrl = Objects.requireNonNull(url,"url can not be null");
    }

    @Override
    public Void call() throws Exception {
        for ( Future<TileJob> job : process) {
            try {
                TileJob rtn = job.get(10, TimeUnit.MICROSECONDS);
                if ( rtn != null ) {
                    sqs.sendMessage(reduceUrl, decoder.writeValueAsString(rtn));
                }
            } catch (ExecutionException ex) {
                log.error(ex);
            }
        }
        return null;
    }
}
