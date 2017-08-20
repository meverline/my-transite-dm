package me.math.grid.tiled;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Polygon;

@SuppressWarnings("serial")
public class TileFragament implements Serializable {

	private long uuid = -1;
	private Polygon boundingBox = null;
	private long index = -1;
	private String heatMapName = null;
	private long heatMapUUID = -1;
	
	/**
	 * 
	 */
	public TileFragament()
	{
	}
	
	/**
	 * 
	 * @param tile
	 * @param grid
	 */
	public TileFragament(SpatialTile tile, DbTiledSpatialGrid grid) {
		this.setIndex(tile.getIndex());
		this.setBoundingBox(tile.getBoundingBox());
		this.setHeatMapName(grid.getHeatMapName());
		this.setHeatMapUUID(grid.getUUID());
	}
	/**
	 * @return the uuid
	 */
	public long getUUID() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return the boundingBox
	 */
	public Polygon getBoundingBox() {
		return boundingBox;
	}
	/**
	 * @param boundingBox the boundingBox to set
	 */
	public void setBoundingBox(Polygon boundingBox) {
		this.boundingBox = boundingBox;
	}
	/**
	 * @return the index
	 */
	public long getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(long index) {
		this.index = index;
	}

	/**
	 * @return the heatMapName
	 */
	public String getHeatMapName() {
		return heatMapName;
	}

	/**
	 * @param heatMapName the heatMapName to set
	 */
	public void setHeatMapName(String heatMapName) {
		this.heatMapName = heatMapName;
	}

	/**
	 * @return the heatMapUUID
	 */
	public long getHeatMapUUID() {
		return heatMapUUID;
	}

	/**
	 * @param heatMapUUID the heatMapUUID to set
	 */
	public void setHeatMapUUID(long heatMapUUID) {
		this.heatMapUUID = heatMapUUID;
	}
	
}
