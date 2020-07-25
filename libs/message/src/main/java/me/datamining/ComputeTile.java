package me.datamining;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.grid.tiled.AbstractTiledSpatialGrid;

public class ComputeTile {

	private String jobNumber;
	private String tileId;
	private int  tileNumber;
	private AbstractTiledSpatialGrid  tile;
	
	public ComputeTile() {
		
	}

	/**
	 * @return the jobNumber
	 */
	@JsonGetter("jobNumber")
	public String getJobNumber() {
		return jobNumber;
	}

	/**
	 * @param jobNumber the jobNumber to set
	 */
	@JsonSetter("jobNumber")
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	/**
	 * @return the tileId
	 */
	@JsonGetter("tileId")
	public String getTileId() {
		return tileId;
	}

	/**
	 * @param tileId the tileId to set
	 */
	@JsonSetter("tileId")
	public void setTileId(String tileId) {
		this.tileId = tileId;
	}

	/**
	 * @return the tileNumber
	 */
	@JsonGetter("tileNumber")
	public int getTileNumber() {
		return tileNumber;
	}

	/**
	 * @param tileNumber the tileNumber to set
	 */
	@JsonSetter("tileNumber")
	public void setTileNumber(int tileNumber) {
		this.tileNumber = tileNumber;
	}

	/**
	 * @return the tile
	 */
	@JsonGetter("tile")
	public AbstractTiledSpatialGrid getTile() {
		return tile;
	}

	/**
	 * @param tile the tile to set
	 */
	@JsonSetter("tile")
	public void setTile(AbstractTiledSpatialGrid tile) {
		this.tile = tile;
	}
	 
}
