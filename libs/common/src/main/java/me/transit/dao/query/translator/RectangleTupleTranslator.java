package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.RectangleTuple;
import me.transit.dao.query.tuple.Tuple;
import org.locationtech.jts.geom.Polygon;

public class RectangleTupleTranslator implements IOrmQueryTranslator {

    private final  RectangleTuple tuple;

    public RectangleTupleTranslator(IQueryTuple tuple) {
        this.tuple = RectangleTuple.class.cast(tuple);
    }

    public  Tuple getCriterion() {

        Polygon range = tuple.makeRectangle(tuple.getUl(), tuple.getLr());
        StringBuilder builder = new StringBuilder("within( ");

        if ( tuple.getAlias() != null ) {
            builder.append(tuple.getAlias().getSimpleName());
            builder.append(".");
        }
        builder.append(tuple.getField());
        builder.append(", :rect");

        Tuple rtn = new Tuple(builder.toString());
        rtn.add("rect", range);
        return rtn;

    }
}
