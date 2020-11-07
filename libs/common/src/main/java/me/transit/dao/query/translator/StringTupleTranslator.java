package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.StringTuple;
import me.transit.dao.query.tuple.Tuple;

public class StringTupleTranslator  implements IOrmQueryTranslator {

    private StringTuple tuple;

    public StringTupleTranslator(IQueryTuple tuple) {
        this.tuple = StringTuple.class.cast(tuple);
    }

    public  Tuple getCriterion() {

        StringBuilder builder = new StringBuilder();
        if ( tuple.getAlias() != null ) {
            builder.append(tuple.getAlias().getSimpleName().toLowerCase());
            builder.append(".");
        }

        builder.append(tuple.getField());
        builder.append(" like ");

        switch ( tuple.getMatchType() ) {
            case END:
                builder.append(tuple.getValue() + "%");
                break;
            case EXACT:
                builder.append(tuple.getValue());
                break;
            case CONTAINS:
                builder.append("%" +  tuple.getValue() + "%");
                break;
            case START:
                builder.append("%" + tuple.getValue());
                break;
        }

        return new Tuple(builder.toString());
    }
}
