package me.transit.dao.query.translator.mongo;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.PolygonBoxTuple;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import com.mongodb.BasicDBObject;

public class MongoPolygonTuple implements IMongoQueryTranslator {

    private final PolygonBoxTuple tuple;

    public  MongoPolygonTuple(IQueryTuple query) {
        tuple = PolygonBoxTuple.class.cast(query);
    }

    public void getDoucmentQuery(Document query) {

        List<Double[]> list = new ArrayList<>();

        for ( Point pt : tuple.getPointLine()) {
            Double [] data = new Double[2];
            data[0] = pt.getCoordinate().x;
            data[1] = pt.getCoordinate().y;
            list.add(data);
        }

        Double [] data = new Double[2];
        data[0] = tuple.getPointLine().get(0).getCoordinate().x;
        data[1] = tuple.getPointLine().get(0).getCoordinate().y;

        query.put(tuple.getField(), new BasicDBObject("$geoWithin", list));

    }

}
