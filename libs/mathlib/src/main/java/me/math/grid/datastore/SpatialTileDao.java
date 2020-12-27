package me.math.grid.datastore;

import me.database.nsstore.AbstractDocumentDao;
import me.database.nsstore.DocumentSession;
import me.math.grid.tiled.SpatialTile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository(value="spatialTileDao")
@Scope("singleton")
public class SpatialTileDao  extends AbstractDocumentDao<SpatialTile> {

    private static final String COLLECTION = "DM_SpatialTile";

    @Autowired
    protected SpatialTileDao(DocumentSession documentDatabase) {
        super(SpatialTileDao.COLLECTION, documentDatabase, SpatialTile.class);
    }
}
