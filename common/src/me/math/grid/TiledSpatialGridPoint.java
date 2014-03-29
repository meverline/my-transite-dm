package me.math.grid;

import me.math.Vertex;
import me.math.kdtree.INode;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("TiledSpatialGridPoint")
public class TiledSpatialGridPoint extends AbstractSpatialGridPoint implements INode {

	@XStreamAlias("tileIndex")
	private int tileIndex_;
	@XStreamAlias("corner")
	private Vertex corner_ = null;
	@XStreamAlias("left")
	private int left_ = -1;
	@XStreamAlias("right")
	private int right_ = -1;
	@XStreamAlias("parent")
	private int parent_ = -1;
	@XStreamOmitField
	private transient SpatialTile grid_ = null;
	
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
	public Vertex getVertex()
	{
		return this.corner_;
	}
	
	/**
	 * @param corner_ the corner_ to set
	 */
	protected void setCorner(Vertex corner) {
		this.corner_ = corner;
	}
	
	
	@Override
	public INode getLeft() {
		if ( left_ != -1 )
			return this.grid_.getEntry(left_);
		else 
			return null;
	}

	@Override
	public void setLeft(INode left) {
		if ( left instanceof TiledSpatialGridPoint) {
			TiledSpatialGridPoint node = TiledSpatialGridPoint.class.cast(left);
			left_ = node.getIndex();
		}

	}

	@Override
	public INode getRight() {
		if ( right_ != -1 ) 
			return this.grid_.getEntry(right_);
		else 
			return null;
	}

	@Override
	public void setRight(INode right) {
		if ( right instanceof TiledSpatialGridPoint) {
			TiledSpatialGridPoint node = TiledSpatialGridPoint.class.cast(right);
			right_ = node.getIndex();
		}
	}

	@Override
	public INode getParent() {
		return this.grid_.getEntry(this.parent_);
	}

	@Override
	public AbstractSpatialGridPoint getPoint() {
		return this;
	}
	
	public void setParent(INode index) {
		if ( index instanceof TiledSpatialGridPoint) {
			TiledSpatialGridPoint node = TiledSpatialGridPoint.class.cast(index);
			this.parent_ = node.getIndex();
		}
	}

	/* (non-Javadoc)
	 * @see me.math.grid.AbstractSpatialGridPoint#Grid()
	 */
	@Override
	public AbstractSpatialGrid Grid() {
		return this.grid_;
	}
	
	public void setGrid(SpatialTile aGrid) {
	    this.grid_ = aGrid;
	}

	/**
	 * @return the tileIndex
	 */
	protected int getTileIndex() {
		return tileIndex_;
	}

	/**
	 * @param tileIndex the tileIndex to set
	 */
	protected void setTileIndex(int tileIndex) {
		this.tileIndex_ = tileIndex;
	}
		
}
