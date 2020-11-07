package me.transit.dao.query.translator.mongo;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.NumberTuple;
import org.bson.Document;

import com.mongodb.QueryOperators;

public class MongoNumberTuple implements IMongoQueryTranslator {

    private final NumberTuple tuple;

    public  MongoNumberTuple(IQueryTuple query) {
        tuple = NumberTuple.class.cast(query);
    }

    public  void getDoucmentQuery(Document query) {

        if (tuple.getLo() == null) {
            switch (tuple.getLogic()) {
                case EQ:
                default:
                    query.put(tuple.getField(), tuple.getHi());
                    break;
                case GT:
                    query.put(tuple.getField(), new Document(QueryOperators.GT, tuple.getHi()));
                    break;
                case LT:
                    query.put(tuple.getField(), new Document(QueryOperators.LT, tuple.getHi()));
                    break;
                case GEQ:
                    query.put(tuple.getField(), new Document(QueryOperators.GTE, tuple.getHi()));
                    break;
                case LEQ:
                    query.put(tuple.getField(), new Document(QueryOperators.LTE, tuple.getHi()));
                    break;
                case NEQ:
                    query.put(tuple.getField(), new Document(QueryOperators.NE, tuple.getHi()));
                    break;
                case NGT:
                    query.put(tuple.getField(), new Document("$not", new Document(QueryOperators.GT, tuple.getHi())));
                    break;
                case NLT:
                    query.put(tuple.getField(), new Document("$not", new Document(QueryOperators.LT, tuple.getHi())));
                    break;
                case NGEQ:
                    query.put(tuple.getField(), new Document("$not", new Document(QueryOperators.GTE, tuple.getHi())));
                    break;
                case NLEQ:
                    query.put(tuple.getField(), new Document("$not", new Document(QueryOperators.LTE, tuple.getHi())));
                    break;
            }
        } else {
            throw new IllegalArgumentException("Between operator not supported");
        }
    }

}
