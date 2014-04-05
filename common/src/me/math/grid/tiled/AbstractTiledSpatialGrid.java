package me.math.grid.tiled;

import me.math.LocalDownFrame;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGrid;
import me.math.grid.AbstractSpatialGridOverlay;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.INode;
import me.math.kdtree.INode.Direction;
import me.math.kdtree.INodeCreator;
import me.math.kdtree.KDTree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class AbstractTiledSpatialGrid extends AbstractSpatialGridOverlay implements INodeCreator {
	
	@XStreamOmitField()
	private Log logger = LogFactory.getLog(TiledSpatialGrid.class);

	@XStreamAlias("tileSize")
	private int tileSize = 32;
	
	/**
	 * 
	 * @param index
	 * @param row
	 * @param col
	 * @return
	 */
	public abstract TiledSpatialGridPoint get(int index, int row, int col);

	/**
	 * 
	 * @param index
	 * @param gridIndex
	 * @return
	 */
	public abstract TiledSpatialGridPoint get(int index, int gridIndex);

	/**
	 * 
	 * @param tile
	 */
	protected abstract void addTile(SpatialTile tile) ;

	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public TiledSpatialGridPoint getEntry(int row, int column) {
		int colWidth = (this.getCols() / this.getTileSize()) +1;
		int index = ((row / this.getTileSize()) * colWidth) + (column / this.getTileSize());
		return get(index, row, column);
		//return this.grid_.get(index).getEntry( row, column);
	}
		
	/**
	 * 
	 * @param index
	 * @return
	 */
    public TiledSpatialGridPoint getEntry(int index) {
    	int size = (this.getTileSize() * this.getTileSize()); 
    	return this.get(index/size, index);
		//return grid_.get(index/size).getEntry(index);
	}

	/**
	 * Create a uniform lat/lon grid.
	 * @param upperLeft
	 * @param lowerRight
	 */
	protected void createGrid(Vertex upperLeft, Vertex lowerRight) {

		Vertex lowerLeft = new Vertex(lowerRight.getLatitudeDegress(),
									  upperLeft.getLongitudeDegress());

		this.setCols(AbstractSpatialGrid.findNumberOfCols(upperLeft, lowerRight, getGridSpacingMeters()));
		this.setRows(AbstractSpatialGrid.findNumberOfRows(upperLeft, lowerRight, getGridSpacingMeters()));
		
		logger.info(getClass().getSimpleName() + " Rows: " + this.getRows() + " Cols: " + this.getCols());

		int index = 0;
		int tileIndex = 0;
		LocalDownFrame southWestFrame = new LocalDownFrame(lowerLeft.getEcfFromLatLon());
		
		for (int rowIndex = 0; rowIndex < this.getRows(); rowIndex += this.tileSize) {
			for (int colIndex = 0; colIndex < this.getCols(); colIndex += this.tileSize ) {
				SpatialTile tile = new SpatialTile(rowIndex, colIndex, index, tileIndex++);
				tile.createGrid(tileSize, tileSize, southWestFrame, this.getGridSpacingMeters());
				this.addTile(tile);
				index += this.tileSize * this.tileSize;
			}
		}
	}

	@Override 
	public INode create(AbstractSpatialGridPoint loc, Direction dir, INode parent, int depth) {
		TiledSpatialGridPoint rtn = null;
		if ( loc instanceof TiledSpatialGridPoint ) {
			rtn = TiledSpatialGridPoint.class.cast(loc);
			rtn.setDepth(depth);
			rtn.setParent(parent);
			rtn.setDirection(dir);
		}
		return rtn;
	}
		
	/**
	 * 
	 * @return
	 */
	public int getTileSize()
	{
		return this.tileSize;
	}
	
	protected void setTileSize(int value)
	{
		this.tileSize = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public KDTree getTree() {
		throw new IllegalArgumentException("opration not supported");
	}

	@Override
	public AbstractSpatialGridPoint getNextGridPoint(
			AbstractSpatialGridPoint gridPt) {
		throw new UnsupportedOperationException();
	}
	
}
