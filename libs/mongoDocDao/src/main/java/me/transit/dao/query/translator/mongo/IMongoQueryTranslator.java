package me.transit.dao.query.translator.mongo;
import org.bson.Document;
public interface IMongoQueryTranslator {

    void getDoucmentQuery(Document query);
}
