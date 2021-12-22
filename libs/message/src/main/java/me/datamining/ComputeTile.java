package me.datamining;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import me.datamining.types.DataMiningTypes;
import me.math.grid.SpatialGridPoint;
import me.math.grid.tiled.SpatialTile;

import java.util.List;

@Jacksonized
@Data
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
