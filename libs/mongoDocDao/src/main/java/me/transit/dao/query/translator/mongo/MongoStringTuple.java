package me.transit.dao.query.translator.mongo;

import java.util.regex.Pattern;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.StringTuple;
import org.bson.Document;

public class MongoStringTuple implements IMongoQueryTranslator {

    private final StringTuple tuple;

    public MongoStringTuple(IQueryTuple query) {
        tuple = StringTuple.class.cast(query);
    }

    public  void getDoucmentQuery(Document query) {

        if ( tuple.getMatchType() == StringTuple.MATCH.EXACT) {
            query.put(tuple.getField(), tuple.getValue() );
        } else {
            Pattern regex;

            switch (tuple.getMatchType()) {
                case START:
                    regex = Pattern.compile(".*" +tuple.getValue(), Pattern.CASE_INSENSITIVE);
                    break;
                case CONTAINS:
                default:
                    regex = Pattern.compile(".*" + tuple.getValue() + "*.", Pattern.CASE_INSENSITIVE);
                    break;
                case END:
                    regex = Pattern.compile(tuple.getValue() + ".*", Pattern.CASE_INSENSITIVE);
                    break;

            }

            query.put(tuple.getField(), regex.pattern() );
        }
    }
}
