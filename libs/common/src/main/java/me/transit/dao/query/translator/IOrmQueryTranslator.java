package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.Tuple;

public interface IOrmQueryTranslator {

    Tuple getCriterion();
}
