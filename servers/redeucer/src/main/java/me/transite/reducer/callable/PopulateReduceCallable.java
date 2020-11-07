package me.transite.reducer.callable;

import me.database.mongo.DocumentDao;
import me.datamining.ComputeTile;
import me.datamining.PopulateTile;
import me.datamining.TileJob;

import java.util.Objects;
import java.util.concurrent.Callable;

public class PopulateReduceCallable implements Callable<TileJob> {

    private final PopulateReduceCallable computeReduce;
    private final PopulateTile message;
    private final DocumentDao documentDao;

    public PopulateReduceCallable(PopulateReduceCallable computeReduce, PopulateTile message, DocumentDao documentDao) {
        this.computeReduce = Objects.requireNonNull(computeReduce,"computeReduce can not be null");
        this.message = Objects.requireNonNull(message,"computeReduce can not be null");
        this.documentDao = Objects.requireNonNull(documentDao,"documentDao can not be null");
    }

    @Override
    public TileJob call() throws Exception {
        return null;
    }
}
