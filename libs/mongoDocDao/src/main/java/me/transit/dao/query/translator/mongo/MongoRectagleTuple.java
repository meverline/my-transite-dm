package me.transit.dao.query.translator.mongo;

import com.mongodb.BasicDBObject;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.RectangleTuple;
import org.bson.Document;

public class MongoRectagleTuple implements IMongoQueryTranslator {

    private final RectangleTuple tuple;

    public MongoRectagleTuple(IQueryTuple query) {
        tuple = RectangleTuple.class.cast(query);
    }

    public void getDoucmentQuery(Document query) {

        Double [] ulc = new Double[2];

        ulc[0] = tuple.getUl().getCoordinate().x;
        ulc[1] = tuple.getLr().getCoordinate().y;

        Double [] urc = new Double[2];
        urc[0] = tuple.getLr().getCoordinate().x;
        urc[1] = tuple.getUl().getCoordinate().y;

        query.put(tuple.getField(), new BasicDBObject("$geoWithin", new Object[] { ulc, urc } ));

    }

}
