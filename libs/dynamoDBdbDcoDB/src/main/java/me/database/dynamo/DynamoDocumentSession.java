package me.database.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import me.database.nsstore.IDocument;
import me.database.nsstore.DocumentSession;
import me.transit.dao.query.tuple.IQueryTuple;

import java.util.List;
import java.util.Map;

public class DynamoDocumentSession extends DocumentSession {

    private final AmazonDynamoDB ddb;
    @SuppressWarnings("unused")
    private final DynamoDBMapper mapper;

    public DynamoDocumentSession(Map<String, String> map) {
        ddb = AmazonDynamoDBClientBuilder.defaultClient();
        mapper = new DynamoDBMapper(ddb);
    }

    @Override
    public void add(IDocument document, String collection) {

    }

    @Override
    public void update(IDocument document, String collection) {

    }

    @Override
    public void delete(IDocument document, String collection) {

    }

    @Override
    public List<IDocument> find(List<IQueryTuple> tupleList, String collection) {
        return null;
    }

    @Override
    public long size(String collection) {
        return 0;
    }
}
