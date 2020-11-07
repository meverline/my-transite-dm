package me.math.grid.tiled;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Polygon;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.database.mongo.IDocument;
import me.math.LocalDownFrame;
import me.math.VectorMath;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGrid;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.data.CrossCovData;
import me.math.kdtree.INode;
import me.math.kdtree.INode.Direction;
import me.math.kdtree.INodeCreator;
import me.math.kdtree.KDTree;
import me.math.kdtree.MinBoundingRectangle;

@JsonRootName(value = "SpatialTile")
@JsonPropertyOrder({ "_id", "rows", "cols", "defaultValue", "row_offset", "col_offset", "grid_size_in_meters", 
					 "index", "tile_index", "frame", "cc_data", "grid" })
public class SpatialTile extends AbstractSpatialGrid implements INodeCreator, IDocument {
	
	public static final String ROW_OFFSET = "rowOffset";
	public static final String COL_OFFSET = "colOffset";

	private int tileIndex = -1;
	private MinBoundingRectangle  mbr_ = null;
	private int index_ = 0;
	private int rowOffset_ = 0;
	private int colOffSet_ = 0;
	private double gridSizeInMeters_ = 0;
	private List<TiledSpatialGridPoint> grid_ = new ArrayList<>();
	private String docId = null;
	private LocalDownFrame frame;
	private AbstractDataSample defaulValue;
	private CrossCovData ccdata;
	
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
						   CrossCovData data)
	{
		this.setFrame(southWestFrame);
		this.setCcdata(data);
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
	 * @param rowOffset the rowOffset_ to set
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
	 * @param colOffSet the colOffSet_ to set
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
	@JsonIgnore()
	public TiledSpatialGridPoint getEntry(int row, int column) {
		int index = ((row - this.getRowOffset()) * this.getCols()) + (column - this.getColOffSet());
		return getGrid().get(index);
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	@JsonIgnore()
	public TiledSpatialGridPoint getEntry(int index) {
		int tileIndex = index - this.getIndex();
		return getGrid().get(tileIndex);
	}
	
	/**
	 * 
	 * @return
	 */
	@JsonIgnore()
	public List<AbstractSpatialGridPoint> getGridPoints() {
		return new ArrayList<>(this.getGrid());
	}
	
	@JsonIgnore()
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
	@JsonIgnore()
	public KDTree getTree() {
		return new KDTree( this.getGridPoints(), this);
	}

	/**
	 * @return the grid_
	 */
	@JsonGetter("grid")
	public List<TiledSpatialGridPoint> getSparseGrid() {
		String defaultHash = this.defaulValue.hash();
		return getGrid()
					.stream()
					.filter(item -> item.getData().hash().compareTo(defaultHash) != 0 )
					.collect(Collectors.toList());
	}

	/**
	 *
	 * @param sparseGrid
	 */
	@JsonSetter("grid")
	public void setSparseGrid(List<TiledSpatialGridPoint> sparseGrid) {
		this.createGrid(getRows(), getCols(), getFrame(), getGridSizeInMeters(), getCcdata());
		
		// set the grid default vale
		grid_.forEach( item -> { 
				try {
					item.setData(defaulValue.getClass().newInstance());
					item.getData().copy(defaulValue);
				} catch (InstantiationException | IllegalAccessException e) {
					item.setData(null);
				}
				
			}
		);
		
		sparseGrid.forEach(item->{
			TiledSpatialGridPoint entry = this.getEntry(item.getRow(), item.getCol());
			entry.setData(item.getData());
		});
	}
	
	/**
	 * @return the grid_
	 */
	@JsonIgnore
	public List<TiledSpatialGridPoint> getGrid() {
		return grid_;
	}

	/**
	 * @param grid_ the grid_ to set
	 */
	@JsonIgnore
	public void setGrid(List<TiledSpatialGridPoint> grid_) {
		this.grid_ = grid_;
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
	@JsonIgnore
	public MinBoundingRectangle getMbr() {
		return mbr_;
	}

	/**
	 * @param mbr_ the mbr_ to set
	 */
	@JsonIgnore
	protected void setMbr(MinBoundingRectangle mbr_) {
		this.mbr_ = mbr_;
	}

	@Override
	@JsonIgnore
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
	 
	public void handleEnum(String key, Object value) {		
	}
	
	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public Polygon getBoundingBox()
	{
		return this.getMbr().toPolygon();
	}

	/**
	 * 
	 * @param box
	 */
	@JsonIgnore
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
	 * @return the gridSizeInMeters_
	 */
	@JsonSetter("grid_size_in_meters")
	public void getGridSizeInMeters(double size) {
		gridSizeInMeters_ = size;
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

	/**
	 * @return the defaulValue
	 */
	@JsonGetter("defaultValue")
	public AbstractDataSample getDefaulValue() {
		defaulValue = null;
		
		grid_.forEach(item->{ 
			String itemHash = item.getData().hash();
			if ( defaulValue == null || itemHash.compareTo(defaulValue.hash()) == 0 ) {
				try {
					defaulValue = item.getData().getClass().newInstance();
					defaulValue.copy(item.getData());
				} catch (InstantiationException | IllegalAccessException e) {
					this.defaulValue = null;
				}
				
			}
		});
		return defaulValue;
	}

	/**
	 * @param defaulValue the defaulValue to set
	 */
	@JsonSetter("defaultValue")
	public void setDefaulValue(AbstractDataSample defaulValue) {
		this.defaulValue = defaulValue;
	}

	/**
	 * @return the frame
	 */
	@JsonGetter("frame")
	public LocalDownFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	@JsonSetter("frame")
	public void setFrame(LocalDownFrame frame) {
		this.frame = frame;
	}

	/**
	 * @return the ccdata
	 */
	@JsonGetter("cc_data")
	public CrossCovData getCcdata() {
		return ccdata;
	}

	/**
	 * @param ccdata the ccdata to set
	 */
	@JsonSetter("cc_data")
	public void setCcdata(CrossCovData ccdata) {
		this.ccdata = ccdata;
	}
	
	
	
	
}
