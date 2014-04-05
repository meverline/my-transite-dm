package me.math.grid.tiled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.database.mongo.IDocument;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.INode;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("TiledSpatialGridPoint")
public class TiledSpatialGridPoint extends AbstractSpatialGridPoint implements INode, IGridDocument {

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
	
	public void setGrid(SpatialTile aGrid) {
	    this.grid_ = aGrid;
	}

	/**
	 * @return the tileIndex
	 */
	public int getTileIndex() {
		return tileIndex_;
	}

	/**
	 * @param tileIndex the tileIndex to set
	 */
	public void setTileIndex(int tileIndex) {
		this.tileIndex_ = tileIndex;
	}
	
	/**
	 * 
	 * @param node
	 */
	public void setLeftNode(int node) {
		this.left_ = node;
	}
	
	/**
	 * 
	 * @param node
	 */
	public void setRightNode(int node) {
		this.right_ = node;
	}
	
	/**
	 * 
	 * @param node
	 */
	public void setParentNode(int node) {
		this.parent_ = node;
	}

	@Override
	public Map<String, Object> toDocument() {
		Map<String,Object> rtn = new HashMap<String,Object>();
		
		rtn.put(IDocument.CLASS, TiledSpatialGridPoint.class.getName());
		rtn.put(AbstractSpatialGridPoint.ROW, this.getRow());
		rtn.put(AbstractSpatialGridPoint.COL, this.getCol());
		rtn.put(AbstractSpatialGridPoint.DEPTH, this.getDepth());
		rtn.put(IGridDocument.INDEX, this.getIndex());
		rtn.put(AbstractSpatialGridPoint.DIRECTION, this.getDirection().name());
		rtn.put("leftNode", this.left_);
		rtn.put("rightNode", this.right_);
		rtn.put("parentNode", this.parent_);
		rtn.put(IGridDocument.TILE_INDEX, this.getTileIndex());
		rtn.put(IGridDocument.MBR, this.getMBR());
		
		List<Double> data = new ArrayList<Double>();
		data.add( this.getVertex().getLatitudeDegress());
		data.add( this.getVertex().getLongitudeDegress());
		rtn.put(IGridDocument.CORNER, data);
		return rtn;
	}

	@Override
	public void handleEnum(String key, Object value) {
        if ( key.equals( "Direction" ) ) {
            this.setDirection( INode.Direction.valueOf(value.toString()));
        }
	}
		
}
