package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.NumberTuple;
import me.transit.dao.query.tuple.Tuple;

public class NumberTupleTranslator implements IOrmQueryTranslator {

    private final NumberTuple tuple;

    public NumberTupleTranslator(IQueryTuple tuple) {
        this.tuple = NumberTuple.class.cast(tuple);
    }

    public Tuple getCriterion() {

        StringBuilder builder = new StringBuilder();
        StringBuilder field = new StringBuilder();

        if ( tuple.getAlias() != null ) {

            field.append(tuple.getAlias().getSimpleName());
            field.append(".");
            field.append(tuple.getField());
        } else {
            field.append(tuple.getField());
        }

        if ( tuple.getLo() != null ) {
            builder.append(field.toString());
            builder.append(" between ");
            builder.append( tuple.getHi() );
            builder.append( " and ");
            builder.append( tuple.getLo());
        } else {
            Number value = tuple.getHi();
            switch (tuple.getLogic()) {
                case EQ:
                default:
                    builder.append(tuple.getField() + " = " + value.toString());
                    break;
                case NLEQ:
                    builder.append("not " + tuple.getField() + " <=" + value.toString());
                    break;
                case NGEQ:
                    builder.append("not " + tuple.getField() + " >= " + value.toString());
                    break;
                case NLT:
                    builder.append("not " + tuple.getField() + " < " + value.toString());
                    break;
                case NGT:
                    builder.append("not " + tuple.getField() + " > " + value.toString());
                    break;
                case NEQ:
                    builder.append(tuple.getField()  + " != " + value.toString());
                    break;
                case LEQ:
                    builder.append(tuple.getField() + " <=" + value.toString());
                    break;
                case GEQ:
                    builder.append(tuple.getField() + " >= " + value.toString());
                    break;
                case LT:
                    builder.append(tuple.getField() + " < " + value.toString());
                    break;
                case GT:
                    builder.append(tuple.getField()  + " > " + value.toString());
                    break;
            }
        }

        return new Tuple(builder.toString());

    }
}
