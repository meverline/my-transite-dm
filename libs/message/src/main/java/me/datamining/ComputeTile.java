package me.datamining;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.datamining.types.DataMiningTypes;
import me.math.grid.SpatialGridPoint;
import me.math.grid.tiled.SpatialTile;

@Data
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
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
}
