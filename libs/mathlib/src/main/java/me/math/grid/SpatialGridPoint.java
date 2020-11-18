package me.math.grid;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import me.math.Vertex;
import me.math.grid.data.AbstractDataSample;
import me.math.kdtree.INode;
import me.math.kdtree.MinBoundingRectangle;

@JsonRootName(value = "SpatialGridPoint")
public class SpatialGridPoint implements INode {
	
	private Vertex corner_ = null;
	private int row_ = -1;
	private int col_ = -1;
	private int index_ = -1;
	private AbstractDataSample data_ = null;

	private transient int depth_ = 0;
	private transient MinBoundingRectangle mbr_ = null;
	private transient INode.Direction direction_ = INode.Direction.UNKOWN;
	private transient INode left_ = null;
	private transient INode right_ = null;
	private transient INode parent_ = null;
	private transient AbstractSpatialGrid grid_;

	/**
	 *
	 */
	public SpatialGridPoint() {
	}
	
	/**
	 *
	 */
	public SpatialGridPoint(int row, int col, Vertex corner, int index, AbstractSpatialGrid grid ) {
		this.setRow(row);
		this.setCol(col);
		this.setCorner(corner);
		this.setIndex(index);
		this.grid_ = grid;
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("row")
	public int getRow() {
		return row_;
	}

	/**
	 * 
	 */
	@JsonGetter("col")
	public int getCol() {
		return col_;
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getIndex() {
		return index_;
	}



	/**
	 * 
	 * @return
	 */
	public String dumpInfo() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("\t[" + this.getRow() + ", " + this.getCol() + "]");
		return buffer.toString();
	}

	/**
	 * return
	 */
	@JsonIgnore
	public Vertex getVertex()
	{
		return new Vertex(corner_);
	}
	
	/**
	 * @param row_ the row_ to set
	 */
	@JsonSetter("row")
	protected void setRow(int row_) {
		this.row_ = row_;
	}

	/**
	 * @param col_ the col_ to set
	 */
	@JsonSetter("col")
	protected void setCol(int col_) {
		this.col_ = col_;
	}

	/**
	 * @param index_ the index_ to set
	 */
	@JsonIgnore
	protected void setIndex(int index_) {
		this.index_ = index_;
	}
	
	/**
	 * @return the data_
	 */
	@JsonGetter("data")
	public AbstractDataSample getData() {
		return data_;
	}

	/**
	 * @param data_ the data_ to set
	 */
	@JsonSetter("data")
	public void setData(AbstractDataSample data_) {
		this.data_ = data_;
	}
	
	@JsonIgnore
	public Vertex getPointVertex() {
		return this.getVertex();
	}
	
	@JsonIgnore
	public int getDepth() {
		return this.depth_;
	}
	
	@JsonIgnore
	public void setDepth(int depth) {
		this.depth_ = depth;
	}
	
	@JsonIgnore
	public Direction getDirection() {
		return this.direction_;
	}
	
	@JsonIgnore
	public void setDirection(Direction dir) {
		this.direction_ = dir;
	}

	@JsonIgnore
	public MinBoundingRectangle getMBR() {
		return this.mbr_;
	}

	@JsonIgnore
	public void setMBR(MinBoundingRectangle mbr) {
		this.mbr_ = mbr;
	}
	
	/**
	 * @return the corner_
	 */
	@JsonIgnore
	public Vertex getCorner() {
		return corner_;
	}

	/**
	 * @param corner_ the corner_ to set
	 */
	@JsonIgnore
	public void setCorner(Vertex corner_) {
		this.corner_ = corner_;
	}

	/**
	 * @return the left_
	 */
	@JsonIgnore
	public INode getLeft() {
		return left_;
	}

	/**
	 * @param left_ the left_ to set
	 */
	@JsonIgnore
	public void setLeft(INode left_) {
		this.left_ = left_;
	}

	/**
	 * @return the right_
	 */
	@JsonIgnore
	public INode getRight() {
		return right_;
	}

	/**
	 * @param right_ the right_ to set
	 */
	@JsonIgnore
	public void setRight(INode right_) {
		this.right_ = right_;
	}

	/**
	 * @return the parent_
	 */
	@JsonIgnore
	public INode getParent() {
		return parent_;
	}

	/**
	 * @param parent_ the parent_ to set
	 */
	@JsonIgnore
	public void setParent(INode parent_) {
		this.parent_ = parent_;
	}

	public boolean contains(Vertex pt) {
		return getMBR().contains(pt);
	}
	
	/**
	 * 
	 * @param dir
	 * @param depth
	 */
	public void initNode(Direction dir, int depth)
	{
		setDirection(dir);
		setDepth(depth);
		setMBR( new MinBoundingRectangle( this.getPointVertex()));
	}
	
	 /* 
	   * (non-Javadoc)
	   * @see java.lang.Object#toString()
	   */
	public String toString()
	{
       StringBuffer buf = new StringBuffer();

       buf.append(getDirection() + " ");
       buf.append(getDepth());
       buf.append(" { " + getPoint().getIndex());
       if ( this.getPointVertex() != null ) {
    	   		buf.append(" ( " + getVertex().getLatitudeDegress() +"," +
                               getVertex().getLongitudeDegress() + ")");
       }
       buf.append("}");
       return buf.toString();
	}
	
	/* (non-Javadoc)
	 * @see me.math.kdtree.INode#getPoint()
	 */
	@JsonIgnore
	public SpatialGridPoint getPoint() {
		return this;
	}

	/**
	 *
	 * @return
	 */
	@JsonIgnore
	public AbstractSpatialGrid Grid()
	{
		return grid_;
	}

}
