package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.PolygonBoxTuple;
import me.transit.dao.query.tuple.Tuple;
import org.locationtech.jts.geom.Polygon;

public class PolygonTupleTranslator implements IOrmQueryTranslator {

    private final PolygonBoxTuple tuple;

    public PolygonTupleTranslator(IQueryTuple tuple) {
        this.tuple = PolygonBoxTuple.class.cast(tuple);
    }

    public Tuple getCriterion() {

        Polygon range = tuple.makePolygon(tuple.getPointLine());
        StringBuilder builder = new StringBuilder("within( ");

        if ( tuple.getAlias() != null ) {
            builder.append(tuple.getAlias().getSimpleName());
            builder.append(".");
        }
        builder.append(tuple.getField());
        builder.append(", :polygon)");

        Tuple rtn = new Tuple(builder.toString());
        rtn.add("polygon", range);

        return rtn;
    }
}
