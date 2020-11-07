package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.TimeTuple;
import me.transit.dao.query.tuple.Tuple;

public class TimeTupleTranslator implements IOrmQueryTranslator{

    private final TimeTuple tuple;

    public TimeTupleTranslator(IQueryTuple tuple) {
        this.tuple = TimeTuple.class.cast(tuple);
    }

    public Tuple getCriterion() {
        StringBuilder builder = new StringBuilder();
        if ( tuple.getAlias() != null ) {
            builder.append(tuple.getAlias().getSimpleName());
            builder.append(".");
        }
        builder.append(tuple.getField());
        builder.append(" between ");
        builder.append(" :time_start ");
        builder.append(" and ");
        builder.append(" :time_end");

        Tuple rtn = new Tuple(builder.toString());
        rtn.add("time_start", tuple.getStartTime());
        rtn.add("time_end", tuple.getEndTime());
        return rtn;
    }
}
