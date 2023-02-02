package org.dm.transit.compute.dataMine;

import me.datamining.DataItem;
import me.math.grid.SpatialGridPoint;
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
        RangeSearch search = new RangeSearch(list.get(0).getLocation(), gridTile.getGridSpacingMeters());

        for (DataItem item : list) {
            if (gridTile.getMbr().contains(item.getLocation())) {
                search.reset();
                search.setPoint(item.getLocation());
                search.setDistanceInMeters(gridTile.getGridSpacingMeters());

                List<SpatialGridPoint> results = tree.find(search);
                for (SpatialGridPoint pt : results) {
                    pt.getData().addValue(item.getValue());
                }
            }
        }
    }

}
