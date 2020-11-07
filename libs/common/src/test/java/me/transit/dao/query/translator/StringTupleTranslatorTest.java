package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.StringTuple;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

public class StringTupleTranslatorTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    @Test
    public void testGetCriterion() {

        Criteria mongo = this.createNiceMock(Criteria.class);

        expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
        expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
        replayAll();

        for ( StringTuple.MATCH logic : StringTuple.MATCH.values()) {
            StringTuple obj = new StringTuple( String.class, "field", "value", logic);
            StringTupleTranslator translator = new StringTupleTranslator(obj);
            translator.getCriterion();
        }

        StringTuple obj = new StringTuple( "field", "100", StringTuple.MATCH.CONTAINS);
        StringTupleTranslator translator = new StringTupleTranslator(obj);
        translator.getCriterion();

    }
}