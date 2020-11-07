package me.transit.dao.query.translator.mongo;

import java.text.SimpleDateFormat;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.TimeTuple;
import org.bson.Document;

public class MongoTimeTuple implements IMongoQueryTranslator  {

    public static final String SDF_DATE_FORMAT = "MMM dd yyyy";
    private final TimeTuple tuple;

    public MongoTimeTuple(IQueryTuple query) {
        tuple = TimeTuple.class.cast(query);
    }

    public  void getDoucmentQuery(Document query) {
        SimpleDateFormat sdf = new SimpleDateFormat( MongoTimeTuple.SDF_DATE_FORMAT);

        String start = sdf.format(tuple.getStartTime().getTime());
        String end = sdf.format(tuple.getEndTime().getTime());
        query.put(tuple.getField(), new BasicDBObject(QueryOperators.GTE,
                                                      start).append(QueryOperators.LTE, end) );
    }
}
