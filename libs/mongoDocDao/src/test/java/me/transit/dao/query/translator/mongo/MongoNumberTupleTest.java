package me.transit.dao.query.translator.mongo;

import me.transit.dao.query.tuple.NumberTuple;
import org.bson.Document;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MongoNumberTupleTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    @Test
    public void testGetDoucmentQuery() {

        Document mongo = new Document();
        for ( NumberTuple.LOGIC logic : NumberTuple.LOGIC.values()) {
            NumberTuple obj = new NumberTuple( "field", 100, logic);
            MongoNumberTuple translator = new MongoNumberTuple(obj);
            translator.getDoucmentQuery(mongo);
        }

        try {
            NumberTuple obj = new NumberTuple( "field", 100, 10);
            MongoNumberTuple translator = new MongoNumberTuple(obj);
            translator.getDoucmentQuery(mongo);
            fail("not here");
        } catch (Exception ex) {
            assertTrue(true);
        }

    }

}