package me.math.grid.tiled;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.locationtech.jts.geom.Polygon;

@SuppressWarnings("serial")
@Entity
@Table(name = "hmt_tileFragment")
public class TileFragament implements Serializable {

	@Id
	@Column(name = "FRAGMENT_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long uuid = -1;

	@Column(name = "MBR", columnDefinition = "Geometry")
	private Polygon boundingBox = null;

	@Column(name = "tileIndex")
	private long index = -1;

	@Column(name = "heatMapName")
	private String heatMapName = null;

	@Column(name = "heatMapUUID")
	private long heatMapUUID = -1;

	/**
	 * 
	 */
	public TileFragament() {
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
	 * @param uuid
	 *            the uuid to set
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
	 * @param boundingBox
	 *            the boundingBox to set
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
	 * @param index
	 *            the index to set
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
	 * @param heatMapName
	 *            the heatMapName to set
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
	 * @param heatMapUUID
	 *            the heatMapUUID to set
	 */
	public void setHeatMapUUID(long heatMapUUID) {
		this.heatMapUUID = heatMapUUID;
	}

}
