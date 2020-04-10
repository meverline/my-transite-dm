package me.math.grid.tiled;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.INode;


public class TiledSpatialGridPoint extends AbstractSpatialGridPoint implements INode, IGridDocument {

	private int tileIndex_;
	private Vertex corner_ = null;
	private int left_ = -1;
	private int right_ = -1;
	private int parent_ = -1;
	private transient SpatialTile grid_ = null;
	private String docId = null;
	
	public TiledSpatialGridPoint()
	{
	}
	
	public TiledSpatialGridPoint(int row, int col, Vertex corner, int index, int tileIndex)
	{
		this.setRow(row);
		this.setCol(col);
		this.setIndex(index);
		corner_ = corner;
		this.tileIndex_ = tileIndex;
	}
	
	/**
	 * 
	 * @return
	 */
	@JsonGetter("vertex")
	public Vertex getVertex()
	{
		return this.corner_;
	}
	
	/**
	 * @param corner_ the corner_ to set
	 */
	@JsonSetter("vertex")
	public void setCorner(Vertex corner) {
		this.corner_ = corner;
	}
	
	public INode getLeft() {
		if ( left_ != -1 )
			return this.grid_.getEntry(left_);
		else 
			return null;
	}

	public void setLeft(INode left) {
		if ( left instanceof TiledSpatialGridPoint) {
			TiledSpatialGridPoint node = TiledSpatialGridPoint.class.cast(left);
			left_ = node.getIndex();
		}

	}

	public INode getRight() {
		if ( right_ != -1 ) 
			return this.grid_.getEntry(right_);
		else 
			return null;
	}

	public void setRight(INode right) {
		if ( right instanceof TiledSpatialGridPoint) {
			TiledSpatialGridPoint node = TiledSpatialGridPoint.class.cast(right);
			right_ = node.getIndex();
		}
	}

	public INode getParent() {
		return this.grid_.getEntry(this.parent_);
	}

	public AbstractSpatialGridPoint getPoint() {
		return this;
	}
	
	public void setParent(INode index) {
		if ( index instanceof TiledSpatialGridPoint) {
			TiledSpatialGridPoint node = TiledSpatialGridPoint.class.cast(index);
			this.parent_ = node.getIndex();
		}
	}
	
	public void setGrid(SpatialTile aGrid) {
	    this.grid_ = aGrid;
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
	
	/**
	 * 
	 * @param node
	 */
	@JsonSetter("left_node")
	public void setLeftNode(int node) {
		this.left_ = node;
	}
	
	@JsonGetter("left_node")
	public int getLeftNode() {
		return this.left_;
	}
	
	/**
	 * 
	 * @param node
	 */
	@JsonSetter("right_node")
	public void setRightNode(int node) {
		this.right_ = node;
	}
	
	@JsonGetter("right_node")
	public int getRightNode() {
		return this.right_;
	}
	
	/**
	 * 
	 * @param node
	 */
	@JsonSetter("parent_node")
	public void setParentNode(int node) {
		this.parent_ = node;
	}
	
	/**
	 * 
	 * @param node
	 */
	@JsonGetter("parent_node")
	public int getParentNode() {
		return this.parent_;
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
