package me.transit.dao;

import me.database.nsstore.AbstractDocumentDao;
import me.database.nsstore.DocumentSession;
import me.transit.database.RouteDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository(value="routeDocumentDao")
@Scope("singleton")
public class RouteDocumentDao extends AbstractDocumentDao<RouteDocument> {

    private final static String COLLECTION = "schedules";

    /**
     *
     * @param documentDatabase
     */
    @Autowired
    public RouteDocumentDao(DocumentSession documentDatabase) {
        super(RouteDocumentDao.COLLECTION, documentDatabase, RouteDocument.class);
    }

}
