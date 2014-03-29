package me.math.grid;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import me.math.LocalDownFrame;
import me.math.VectorMath;
import me.math.Vertex;
import me.math.kdtree.INode;
import me.math.kdtree.INode.Direction;
import me.math.kdtree.INodeCreator;
import me.math.kdtree.KDTree;
import me.math.kdtree.MinBoundingRectangle;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("SpatialTile")
public class SpatialTile extends AbstractSpatialGrid implements INodeCreator {

	@XStreamAlias("tileIndex")
	private int tileIndex = -1;
	@XStreamAlias("mbr")
	private MinBoundingRectangle  mbr_ = null;
	@XStreamAlias("index")
	private int index_ = 0;
	@XStreamAlias("root")
	private int root_ = -1;
	@XStreamAlias("rowOffset")
	private int rowOffset_ = 0;
	@XStreamAlias("colOffest")
	private int colOffSet_ = 0;
	@XStreamImplicit(itemFieldName="grid")
	private List<TiledSpatialGridPoint> grid_ = new ArrayList<TiledSpatialGridPoint>();
	
	/**
	 * 
	 */
	public SpatialTile()
	{
	}
	
	/**
	 * 
	 * @param rowOffset
	 * @param colOffset
	 */
	public SpatialTile(int rowOffset, int colOffset, int index, int tileIndex)
	{
		this.setColOffSet(colOffset);
		this.setRowOffset(rowOffset);
		this.setIndex(index);
		this.tileIndex = tileIndex;
		this.mbr_ = new MinBoundingRectangle();
	}
	
	/**
	 * 
	 * @param rows
	 * @param cols
	 * @param southWestFrame
	 */
	public void createGrid(int rows, int cols, LocalDownFrame southWestFrame)
	{
		this.setRows(rows);
		this.setCols(cols);
		int startIndex = this.getIndex();
		for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
			for (int colIndex = 0; colIndex < cols; colIndex++) {
				double northDistanceMeters = (double) (rowIndex + this.getRowOffset())* this.getGridSpacingMeters();
				double eastDistanceMeters = (double) (colIndex + this.getColOffSet())* this.getGridSpacingMeters();

				VectorMath newPos = southWestFrame.getRelativePosition(
															northDistanceMeters,
															eastDistanceMeters,
															LocalDownFrame.RelativePositionOrder.NORTH_THEN_EAST);

				TiledSpatialGridPoint pt = new TiledSpatialGridPoint(rowIndex + this.getRowOffset(), 
																     colIndex + this.getColOffSet(), 
																     Vertex.getLatLonFromEcf(newPos),
																     startIndex++, this.tileIndex);
				this.grid_.add(pt);
				this.mbr_.extend(pt);
				pt.setGrid(this);
			}
		}
	}

	/**
	 * @return the rowOffset_
	 */
	public int getRowOffset() {
		return rowOffset_;
	}

	/**
	 * @param rowOffset_ the rowOffset_ to set
	 */
	public void setRowOffset(int rowOffset) {
		this.rowOffset_ = rowOffset;
	}

	/**
	 * @return the colOffSet_
	 */
	public int getColOffSet() {
		return colOffSet_;
	}

	/**
	 * @param colOffSet_ the colOffSet_ to set
	 */
	public void setColOffSet(int colOffSet) {
		this.colOffSet_ = colOffSet;
	}

	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public TiledSpatialGridPoint getEntry(int row, int column) {
		int index = ((row - this.getRowOffset()) * this.getCols()) + (column - this.getColOffSet());
		return grid_.get(index);
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public TiledSpatialGridPoint getEntry(int index) {
		int tileIndex = index - this.getIndex();
		return grid_.get(tileIndex);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<AbstractSpatialGridPoint> getGridPoints() {
		List<AbstractSpatialGridPoint> rtn = new ArrayList<AbstractSpatialGridPoint>();
		rtn.addAll(this.grid_);
		return rtn;
	}
	
	@Override 
	public INode create(AbstractSpatialGridPoint loc, Direction dir, INode parent, int depth) {
		TiledSpatialGridPoint rtn = null;
		if ( loc instanceof TiledSpatialGridPoint ) {
			rtn = TiledSpatialGridPoint.class.cast(loc);
			rtn.initNode(dir, depth);
			rtn.setParent(parent);
		}
		return rtn;
	}
	
	/**
	 * 
	 * @return
	 */
	public KDTree getTree() {
		KDTree rtn = null;
		if ( this.getRoot() != -1) {
			rtn = new KDTree( this.getEntry(this.getRoot()));
		} else  {
			rtn = new KDTree( this.getGridPoints(), this);
			setRoot(TiledSpatialGridPoint.class.cast(rtn.getRootNode()).getIndex());
		}
		return rtn;
	}

	/**
	 * @return the root_
	 */
	public int getRoot() {
		return root_;
	}

	/**
	 * @param root_ the root_ to set
	 */
	public void setRoot(int root) {
		this.root_ = root;
	}

	/**
	 * @return the grid_
	 */
	public List<TiledSpatialGridPoint> getGrid() {
		return grid_;
	}

	/**
	 * @param grid_ the grid_ to set
	 */
	public void setGrid(List<TiledSpatialGridPoint> grid) {
		this.grid_ = grid;
	}

	/**
	 * @return the index_
	 */
	public int getIndex() {
		return index_;
	}

	/**
	 * @param index_ the index_ to set
	 */
	public void setIndex(int index_) {
		this.index_ = index_;
	}

	@Override
	public AbstractSpatialGridPoint getNextGridPoint(
			AbstractSpatialGridPoint gridPt) {
		throw new UnsupportedOperationException();
	}
	
	public void dump(Writer out, int tileSize, String tab)
	{
		StringBuilder buf = new StringBuilder();
		
		buf.append(tab);
		buf.append("Tile index ");
		buf.append(this.tileIndex);
		buf.append(" Row offset ");
		buf.append(this.getRowOffset());
		buf.append(" Col offset ");
		buf.append(this.getColOffSet());
		buf.append("\n");
		buf.append(tab);
		int ndx = 0;
		for ( TiledSpatialGridPoint pt : this.grid_) {
			ndx++;
			buf.append("(");
			buf.append(pt.getRow());
			buf.append(",");
			buf.append(pt.getCol());
			buf.append(")");
			if ( ndx+1 > tileSize) {
				buf.append(tab);
				buf.append("\n");
				ndx = 0;
			}
		}
		buf.append("\n");
		
		try {
			out.write(buf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
