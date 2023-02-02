package org.dm.transit.compute.callable;

import me.datamining.PopulateTile;
import me.datamining.TileJob;
import me.datamining.mapreduce.PopulateGrid;

import java.util.Objects;
import java.util.concurrent.Callable;

public class PopulateJobCallable implements Callable<TileJob> {

    private final PopulateGrid populateGrid;
    private final PopulateTile job;

    public PopulateJobCallable(PopulateGrid populateGrid, PopulateTile populateTile) {
        this.populateGrid = Objects.requireNonNull(populateGrid,"populateGrid can not be null");
        this.job = Objects.requireNonNull(populateTile,"populateTile can not be null");
    }

    @Override
    public TileJob call() throws Exception {
       // this.populateGrid.populate(this.job.getTile(), this.job.getData());
        return this.job;
    }
}