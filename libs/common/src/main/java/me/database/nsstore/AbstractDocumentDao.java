package me.database.nsstore;

import me.transit.dao.query.tuple.IQueryTuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AbstractDocumentDao<T extends IDocument> {

    private final DocumentSession session;
    private final String collection;
    private final Class<T> type;

    protected AbstractDocumentDao(String collection, DocumentSession documentDatabase, Class<T> type)
    {
        this.session = Objects.requireNonNull(documentDatabase, "documentDatabase can't be null");
        this.collection = Objects.requireNonNull(collection, "collection can't be null");
        this.type = Objects.requireNonNull(type, "type can't be null");
    }

    protected String getCollection() {
        return collection;
    }

    /**
     * @param document
     */
    public void add(T document) {
        if ( document.getDocId() != -1 ) {
            session.update(document, this.getCollection());
        } else {
            document.setDocId(session.size(this.getCollection()) + 1);
            session.add(document, this.getCollection());
        }
    }

    /**
     * @param document
     */
    public void update(T document) {
        session.add(document, this.getCollection());

    }

    /**
     * @param document
     */
    public void delete(T document) {
        session.delete(document, this.getCollection());
    }

    /**
     * @param query
     * @return
     */
    public List<T> find(List<IQueryTuple> query) {
        List<T> rtn = new ArrayList<>();
        for (IDocument item : session.find(query, this.getCollection())) {
            rtn.add(type.cast(item));
        }

        return rtn;
    }
}
