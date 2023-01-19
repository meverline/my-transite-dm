package me.transit.dao.query.translator.mongo;

import java.util.Calendar;

import org.bson.Document;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;

import me.transit.dao.query.tuple.TimeTuple;

public class MongoTimeTupleTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    @Test
    public void testGetDoucmentQuery() {

        Document mongo = new Document();
        TimeTuple obj = new TimeTuple("field", Calendar.getInstance(), Calendar.getInstance());
        MongoTimeTuple translator = new MongoTimeTuple(obj);
        translator.getDoucmentQuery(mongo);
    }


}