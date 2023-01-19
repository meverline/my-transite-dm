package me.transit.dao.query.translator.mongo;

import org.bson.Document;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;

import me.transit.dao.query.tuple.StringTuple;

public class MongoStringTupleTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);


    @Test
    public void testGetDoucmentQuery() {

        Document mongo = new Document();
        for ( StringTuple.MATCH logic : StringTuple.MATCH.values()) {
            StringTuple obj = new StringTuple( "field", "value", logic);
            MongoStringTuple translator = new MongoStringTuple(obj);
            translator.getDoucmentQuery(mongo);
        }

    }

}