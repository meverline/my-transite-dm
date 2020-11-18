package me.datamining;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import me.datamining.types.DataMiningTypes;
import me.math.grid.SpatialGridPoint;
import me.math.grid.tiled.SpatialTile;

import java.util.List;

@JsonRootName("ComputeTile")
public class ComputeTile extends TileJob {

    private DataMiningTypes dmType;
    private long n;
    private SpatialTile target;
    private List<SpatialGridPoint> gridPoints;

    public ComputeTile() {}

    public ComputeTile(String jobNumber, int  tileNumber, SpatialTile target, List<SpatialGridPoint> gridPoints, DataMiningTypes type) {
        super(jobNumber, tileNumber);
        this.setTarget(target);
        this.setGridPoints(gridPoints);
        this.setDmType(type);
    }

    @JsonGetter("target")
    public SpatialTile getTarget() {
        return target;
    }

    @JsonSetter("target")
    public void setTarget(SpatialTile target) {
        this.target = target;
    }

    @JsonGetter("gridPoints")
    public List<SpatialGridPoint> getGridPoints() {
        return gridPoints;
    }

    @JsonSetter("gridPoints")
    public void setGridPoints(List<SpatialGridPoint> gridPoints) {
        this.gridPoints = gridPoints;
    }

    @JsonGetter("dmType")
    public DataMiningTypes getDmType() { return dmType; }

    @JsonSetter("dmType")
    public void setDmType(DataMiningTypes dmType) { this.dmType = dmType; }

}
