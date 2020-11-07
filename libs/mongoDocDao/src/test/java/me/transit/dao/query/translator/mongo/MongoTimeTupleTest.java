package me.transit.dao.query.translator.mongo;

import me.transit.dao.query.tuple.TimeTuple;
import org.bson.Document;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

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