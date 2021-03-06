package me.transite.reducer.callable;

import me.database.mongo.MongoDocumentSession;
import me.datamining.ComputeTile;
import me.datamining.TileJob;

import java.util.Objects;
import java.util.concurrent.Callable;

public class ComputeReduceCallable implements Callable<TileJob> {

    private final ComputeReduceCallable computeReduce;
    private final ComputeTile message;
    private final MongoDocumentSession documentDao;

    public ComputeReduceCallable(ComputeReduceCallable computeReduce, ComputeTile message, MongoDocumentSession documentDao) {
        this.computeReduce = Objects.requireNonNull(computeReduce,"computeReduce can not be null");
        this.message = Objects.requireNonNull(message,"computeReduce can not be null");
        this.documentDao = Objects.requireNonNull(documentDao,"documentDao can not be null");
    }

    @Override
    public TileJob call() throws Exception {
        /*
        IDocument document = documentDao.find();
        DbTiledSpatialGrid baseGrid = DbTiledSpatialGrid.class.case(document);

        AbstractSpatialGridPoint zeroTile = baseGrid.get(message.getTileNumber(), message.getTarget().getIndex());

        int index = 0;
        for ( TiledSpatialGridPoint cnt : zeroTile.getGrid()) {
            AbstractDataSample sample = cnt.getData();
            double total = 0;
            for ( SpatialTile tile : aList ) {
                TiledSpatialGridPoint gridPt = tile.getGrid().get(index);

                total += gridPt.getData().getInterpolationValue();
            }
            sample.setInterpolationValue( (1.0 / getNumSamples())* total);
            index++;
        }
        */
        
        return null;
    }
}
