package me.transit.dao.query.translator;

import static org.easymock.EasyMock.expect;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import me.transit.dao.query.tuple.NumberTuple;

public class NumberTupleTranslatorTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    @Test
    public void testGetCriterion() {

        Criteria mongo = this.createNiceMock(Criteria.class);

        expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
        replayAll();

        for ( NumberTuple.LOGIC logic : NumberTuple.LOGIC.values()) {
            NumberTuple obj = new NumberTuple( String.class, "field", 100, logic);
            NumberTupleTranslator translator = new NumberTupleTranslator(obj);
            translator.getCriterion();
        }

        NumberTuple obj = new NumberTuple( "field", 100, 10);
        NumberTupleTranslator translator = new NumberTupleTranslator(obj);
        translator.getCriterion();
        obj = new NumberTuple( String.class, "field", 100, 10);
        translator = new NumberTupleTranslator(obj);
        translator.getCriterion();

    }

}