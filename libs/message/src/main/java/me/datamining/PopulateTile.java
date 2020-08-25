package me.datamining;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.grid.tiled.SpatialTile;

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

	/**
	 * @return the tile
	 */
	@JsonGetter("tile")
	public SpatialTile getTile() {
		return tile;
	}

	/**
	 * @param tile the tile to set
	 */
	@JsonSetter("tile")
	public void setTile(SpatialTile tile) {
		this.tile = tile;
	}

	/**
	 * @return the data
	 */
	@JsonGetter("data")
	public List<DataItem> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	@JsonSetter("data")
	public void setData(List<DataItem> data) {
		this.data = data;
	}
	 
}
