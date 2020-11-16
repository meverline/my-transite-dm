package me.math.grid.tiled;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.database.nsstore.IDocument;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.INode;


public class TiledSpatialGridPoint extends AbstractSpatialGridPoint implements INode, IDocument {

	private int tileIndex_;
	private String docId = null;
	
	private transient SpatialTile grid_ = null;
	
	/**
	 * 
	 */
	public TiledSpatialGridPoint()
	{
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @param corner
	 * @param index
	 * @param tileIndex
	 */
	public TiledSpatialGridPoint(int row, int col, Vertex corner, int index, int tileIndex)
	{
		super(row, col, corner, index);
		this.tileIndex_ = tileIndex;
	}
		
	/**
	 * 
	 * @param aGrid
	 */
	@JsonIgnore
	public void setGrid(SpatialTile aGrid) {
	    this.grid_ = aGrid;
	}
	
	/**
	 * @return the grid_
	 */
	@JsonIgnore
	public SpatialTile getGrid() {
		return grid_;
	}

	/**
	 * @return the tileIndex
	 */
	@JsonGetter("tile_index")
	public int getTileIndex() {
		return tileIndex_;
	}

	/**
	 * @param tileIndex the tileIndex to set
	 */
	@JsonSetter("tile_index")
	public void setTileIndex(int tileIndex) {
		this.tileIndex_ = tileIndex;
	}
	
	@JsonGetter("_id")
	@Override
	public String getDocId() {
		return this.docId;
	}

	@JsonSetter("_id")
	@Override
	public void setDocId(String docId) {
		this.docId = docId;
	}

}
