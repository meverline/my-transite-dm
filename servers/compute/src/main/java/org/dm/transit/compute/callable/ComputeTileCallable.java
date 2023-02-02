package org.dm.transit.compute.callable;

import me.datamining.ComputeTile;
import me.datamining.TileJob;
import me.datamining.mapreduce.TiledNonAdaptiveKDE;

import java.util.Objects;
import java.util.concurrent.Callable;

public class ComputeTileCallable implements Callable<TileJob> {

    private final TiledNonAdaptiveKDE tiledNonAdaptiveKDE;
    private final ComputeTile job;

    public ComputeTileCallable(TiledNonAdaptiveKDE tiledNonAdaptiveKDE, ComputeTile computeTile) {
        this.tiledNonAdaptiveKDE = Objects.requireNonNull(tiledNonAdaptiveKDE,"tiledNonAdaptiveKDE can not be null");
        this.job = Objects.requireNonNull(computeTile,"computeTile can not be null");
    }

    @Override
    public TileJob call() throws Exception {
        //this.tiledNonAdaptiveKDE.kernalDensityEstimate(job.getTarget(), 
        											  // job.getGridPoints());
        return this.job;
    }
}
