package me.database.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import me.database.nsstore.AbstractDocument;
import me.database.nsstore.IDocument;
import me.database.nsstore.IDocumentSession;
import me.transit.dao.query.tuple.IQueryTuple;

import java.util.List;
import java.util.Map;

public class DynamoDocumentSession extends IDocumentSession {

    private final AmazonDynamoDB ddb;
    private final DynamoDBMapper mapper;

    public DynamoDocumentSession(Map<String, String> map) {
        ddb = AmazonDynamoDBClientBuilder.defaultClient();
        mapper = new DynamoDBMapper(ddb);
    }

    @Override
    public void add(IDocument document, String collection) {

    }

    @Override
    public void add(IDocument document) {

    }

    @Override
    public List<AbstractDocument> find(List<IQueryTuple> tupleList) {
        return null;
    }

    @Override
    public List<AbstractDocument> find(List<IQueryTuple> tupleList, String collection) {
        return null;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public long size(String collection) {
        return 0;
    }
}
