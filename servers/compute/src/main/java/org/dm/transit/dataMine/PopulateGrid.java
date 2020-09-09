package org.dm.transit.dataMine;

import me.datamining.DataItem;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.tiled.SpatialTile;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;

import java.util.List;

public class PopulateGrid {

    public PopulateGrid() {
    }

    /**
     * @param gridTile
     * @param list
     * @return
     */
    public void populate(SpatialTile gridTile, List<DataItem> list) {
        KDTree tree = gridTile.getTree();
        RangeSearch search = new RangeSearch(list.get(0).getLocation(), gridTile.getGridSizeInMeters());

        for (DataItem item : list) {
            if (gridTile.getMbr().contains(item.getLocation())) {
                search.reset();
                search.setPoint(item.getLocation());
                search.setDistanceInMeters(gridTile.getGridSizeInMeters());

                tree.find(search);
                List<AbstractSpatialGridPoint> results = search.getResults();

                for (AbstractSpatialGridPoint pt : results) {
                    pt.getData().addValue(item.getValue());
                }
            }
        }
    }

}
