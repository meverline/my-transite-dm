package me.math.grid.datastore;

import me.database.nsstore.AbstractDocumentDao;
import me.database.nsstore.DocumentSession;
import me.math.grid.tiled.SpatialTile;
import me.math.grid.tiled.TiledSpatialGrid;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.NumberTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository(value="tileSpatialGridDao")
@Scope("singleton")
public class TileSpatialGridDao extends AbstractDocumentDao<TiledSpatialGrid> {

    private static final String COLLECTION = "DM_TileSpatialGrid";

    private final SpatialTileDao spatialTileDao;

    @Autowired
    protected TileSpatialGridDao( DocumentSession documentDatabase, SpatialTileDao spatialTileDao) {
        super(TileSpatialGridDao.COLLECTION, documentDatabase, TiledSpatialGrid.class);
        this.spatialTileDao = Objects.requireNonNull(spatialTileDao, "spatialTileDao can't be null");
    }

    @Override
    public void add(TiledSpatialGrid document) {
        super.add(document);
        for (SpatialTile tile : document.getTiles()) {
            spatialTileDao.add(tile);
        }
    }

    @Override
    public void update(TiledSpatialGrid document) {
        super.update(document);
        for (SpatialTile tile : document.getTiles()) {
            spatialTileDao.add(tile);
        }
    }

    @Override
    public void delete(TiledSpatialGrid document) {
        super.delete(document);
        for (SpatialTile tile : document.getTiles()) {
            spatialTileDao.add(tile);
        }
    }

    @Override
    public List<TiledSpatialGrid> find(List<IQueryTuple> query) {
        final List<TiledSpatialGrid> grids = super.find(query);
        final List<IQueryTuple> tileQuery = new ArrayList<>();
        for ( TiledSpatialGrid item : grids) {
            tileQuery.add( new NumberTuple("gridUUID", item.getDocId(), NumberTuple.LOGIC.EQ));
            item.getTiles().clear();
            item.getTiles().addAll(spatialTileDao.find(tileQuery));
            tileQuery.clear();
        }

        return  grids;
    }
}
