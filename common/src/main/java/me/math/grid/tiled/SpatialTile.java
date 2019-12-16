package me.math.grid.tiled;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.database.mongo.IDocument;
import me.math.LocalDownFrame;
import me.math.VectorMath;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGrid;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.INode;
import me.math.kdtree.INode.Direction;
import me.math.kdtree.INodeCreator;
import me.math.kdtree.KDTree;
import me.math.kdtree.MinBoundingRectangle;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.locationtech.jts.geom.Polygon;

public class SpatialTile extends AbstractSpatialGrid implements INodeCreator, IGridDocument {
	
	public static final String ROW_OFFSET = "rowOffset";
	public static final String COL_OFFSET = "colOffset";
	

	private int tileIndex = -1;
	private MinBoundingRectangle  mbr_ = null;
	private int index_ = 0;
	private int root_ = -1;
	private int rowOffset_ = 0;
	private int colOffSet_ = 0;
	private double gridSizeInMeters_ = 0;
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
	public void createGrid(int rows, int cols, 
						   LocalDownFrame southWestFrame, 
						   double spacingInMeters, 
						   AbstractTiledSpatialGrid.CrossCovData data)
	{
		this.setRows(rows);
		this.setCols(cols);
		
		int startIndex = this.getIndex();
		gridSizeInMeters_ = spacingInMeters;
		this.setMbr(new MinBoundingRectangle());
		for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
			for (int colIndex = 0; colIndex < cols; colIndex++) {
				double northDistanceMeters = (double) (rowIndex + this.getRowOffset())* spacingInMeters;
				double eastDistanceMeters = (double) (colIndex + this.getColOffSet())* spacingInMeters;

				VectorMath newPos = southWestFrame.getRelativePosition(
															northDistanceMeters,
															eastDistanceMeters,
															LocalDownFrame.RelativePositionOrder.NORTH_THEN_EAST);

				Vertex crPt = Vertex.getLatLonFromEcf(newPos);
				data.addPoint(crPt);
				
				TiledSpatialGridPoint pt = new TiledSpatialGridPoint(rowIndex + this.getRowOffset(), 
																     colIndex + this.getColOffSet(), 
																     crPt,
																     startIndex++, this.tileIndex);
				this.getMbr().extend(pt);
				this.grid_.add(pt);
				this.mbr_.extend(pt);
				pt.setGrid(this);
			}
		}
	}

	/**
	 * @return the rowOffset_
	 */
	@JsonGetter("row_offset")
	public int getRowOffset() {
		return rowOffset_;
	}

	/**
	 * @param rowOffset_ the rowOffset_ to set
	 */
	@JsonSetter("row_offset")
	public void setRowOffset(int rowOffset) {
		this.rowOffset_ = rowOffset;
	}

	/**
	 * @return the colOffSet_
	 */
	@JsonGetter("col_offset")
	public int getColOffSet() {
		return colOffSet_;
	}

	/**
	 * @param colOffSet_ the colOffSet_ to set
	 */
	@JsonSetter("col_offset")
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
	@JsonGetter("root")
	public int getRoot() {
		return root_;
	}

	/**
	 * @param root_ the root_ to set
	 */
	@JsonSetter("root")
	public void setRoot(int root) {
		this.root_ = root;
	}

	/**
	 * @return the grid_
	 */
	@JsonGetter("grid")
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
	@JsonGetter("index")
	public int getIndex() {
		return index_;
	}

	/**
	 * @param index_ the index_ to set
	 */
	@JsonSetter("index")
	public void setIndex(int index_) {
		this.index_ = index_;
	}
	
	/**
	 * @return the tileIndex
	 */
	@JsonGetter("tile_index")
	public int getTileIndex() {
		return tileIndex;
	}

	/**
	 * @param tileIndex the tileIndex to set
	 */
	@JsonSetter("tile_index")
	public void setTileIndex(int tileIndex) {
		this.tileIndex = tileIndex;
	}

	/**
	 * @return the mbr_
	 */
	@JsonGetter("min_bounding_rectangle")
	public MinBoundingRectangle getMbr() {
		return mbr_;
	}

	/**
	 * @param mbr_ the mbr_ to set
	 */
	@JsonSetter("min_bounding_rectangle")
	public void setMbr(MinBoundingRectangle mbr_) {
		this.mbr_ = mbr_;
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
	
	/**
	 * 
	 * @param node
	 */
	@JsonGetter("root_node")
	public void setRootNode(int node) {
		this.root_ = node;
	}

	public Map<String, Object> toDocument() {
		Map<String,Object> rtn = new HashMap<String,Object>();
		
		rtn.put(IDocument.CLASS, SpatialTile.class.getName());
		rtn.put(SpatialTile.ROW_OFFSET, this.getRowOffset());
		rtn.put(SpatialTile.COL_OFFSET, this.getColOffSet());
		rtn.put(IGridDocument.INDEX, this.getIndex());
		rtn.put(IGridDocument.TILE_INDEX, this.tileIndex);
		rtn.put(AbstractSpatialGrid.ROWS, this.getRows());
		rtn.put(AbstractSpatialGrid.COLS, this.getCols());
		
		rtn.put("rootNode", this.root_);
		rtn.put(IGridDocument.MBR, this.getMbr());
		rtn.put(IGridDocument.GRID, this.getGridPoints());
		return rtn;
	}

	public void handleEnum(String key, Object value) {		
	}
	
	/**
	 * 
	 * @return
	 */
	public Polygon getBoundingBox()
	{
		return this.getMbr().toPolygon();
	}

	/**
	 * 
	 * @param box
	 */
	public void setBoundingBox(Polygon box) {
		this.setMbr( new MinBoundingRectangle(box));
	}

	/**
	 * @return the gridSizeInMeters_
	 */
	@JsonGetter("grid_size_in_meters")
	public double getGridSizeInMeters() {
		return gridSizeInMeters_;
	}
	
	/**
	 * 
	 * @param out
	 * @param iterpolationValue
	 * @throws UnknownHostException
	 */
	public void toCSV(PrintStream out, boolean iterpolationValue ) throws UnknownHostException {
		
		int col = 0;
		for ( TiledSpatialGridPoint pt : this.getGrid()) {
			if ( col != 0 ) { out.print(","); }
			if ( iterpolationValue ) {
				out.print(pt.getData().getInterpolationValue());
			} else {
				out.print(pt.getData().getValue());
			}
			col++;
			if ( col > this.getCols() ) {
				out.println();
				col = 0;
			}
		}
		
	}
	
}
