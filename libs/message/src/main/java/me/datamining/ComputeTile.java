package me.datamining;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import me.math.grid.tiled.SpatialTile;

@JsonRootName("ComputeTile")
public class ComputeTile extends TileJob {

    private SpatialTile target;
    private SpatialTile compute;

    public ComputeTile() {}

    public ComputeTile(String jobNumber, int  tileNumber, SpatialTile target, SpatialTile compute) {
        super(jobNumber, tileNumber);
        this.setTarget(target);
        this.setCompute(compute);
    }

    @JsonGetter("target")
    public SpatialTile getTarget() {
        return target;
    }

    @JsonSetter("target")
    public void setTarget(SpatialTile target) {
        this.target = target;
    }

    @JsonGetter("compute")
    public SpatialTile getCompute() {
        return compute;
    }

    @JsonSetter("compute")
    public void setCompute(SpatialTile compute) {
        this.compute = compute;
    }
}
