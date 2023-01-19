package me.config;

import me.database.nsstore.DocumentSession;
import me.database.nsstore.IDocument;
import me.transit.dao.query.tuple.IQueryTuple;


import java.util.List;

public class FakeDocumentSession extends DocumentSession {
    @Override
    public void add(IDocument document, String collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(IDocument document, String collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(IDocument document, String collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<IDocument> find(List<IQueryTuple> tupleList, String collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long size(String collection) {
        throw new UnsupportedOperationException();
    }
}
