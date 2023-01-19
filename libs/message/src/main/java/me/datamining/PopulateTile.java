package me.datamining;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.math.grid.tiled.SpatialTile;


@Data
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("PopulateTile")
public class PopulateTile extends TileJob {

	private SpatialTile  tile;
	private List<DataItem> data = new ArrayList<>();
	
	public PopulateTile() {
	}

	public PopulateTile(String jobNumber, int  tileNumber, SpatialTile tile, List<DataItem> data) {
		super(jobNumber, tileNumber);
		this.setTile(tile);
		this.setData(data);
	}

}
