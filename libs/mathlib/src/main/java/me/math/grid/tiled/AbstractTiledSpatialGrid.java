package me.math.grid.tiled;

import java.net.UnknownHostException;
import java.sql.SQLException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.fasterxml.jackson.annotation.*;
import me.math.LocalDownFrame;
import me.math.VectorMath;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGrid;
import me.math.grid.SpatialGridPoint;
import me.math.grid.data.CrossCovData;
import me.math.kdtree.INode;
import me.math.kdtree.INode.Direction;
import me.math.kdtree.INodeCreator;
import me.math.kdtree.KDTree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

public abstract class AbstractTiledSpatialGrid extends AbstractSpatialGrid implements INodeCreator {

	private Log logger = LogFactory.getLog(getClass());
	private int tileSize = 32;

	/**
	 * 
	 * @param index
	 * @param row
	 * @param col
	 * @return
	 */
	@JsonIgnore
	public abstract SpatialGridPoint get(int index, int row, int col) throws UnknownHostException;

	/**
	 * 
	 * @param index
	 * @param gridIndex
	 * @return
	 */
	@JsonIgnore
	public abstract SpatialGridPoint get(int index, int gridIndex);

	/**
	 * 
	 * @param tile
	 */
	protected abstract void addTile(SpatialTile tile) throws UnknownHostException, SQLException;

	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 * @throws UnknownHostException 
	 */
	@JsonIgnore
	public SpatialGridPoint getEntry(int row, int column) throws UnknownHostException {
		int colWidth = (this.getCols() / this.getTileSize()) +1;
		int index = ((row / this.getTileSize()) * colWidth) + (column / this.getTileSize());
		return get(index, row, column);
	}
		
	/**
	 * 
	 * @param index
	 * @return
	 * @throws UnknownHostException 
	 */
	@JsonIgnore
    public SpatialGridPoint getEntry(int index) throws UnknownHostException {
    	int size = (this.getTileSize() * this.getTileSize()); 
    	return this.get(index/size, index);
	}

	/**
	 * Create a uniform lat/lon grid.
	 * @param upperLeft
	 * @param lowerRight
	 * @throws UnknownHostException 
	 * @throws SQLException 
	 */
	protected void createGrid(Vertex upperLeft, Vertex lowerRight) throws UnknownHostException, SQLException {

		Vertex lowerLeft = new Vertex(lowerRight.getLatitudeDegress(),
									  upperLeft.getLongitudeDegress());

		this.setCols(findNumberOfCols(upperLeft, lowerRight, getGridSpacingMeters()));
		this.setRows(findNumberOfRows(upperLeft, lowerRight, getGridSpacingMeters()));
		
		
		// find number of tiles
		int totalColTiles = 0;
		int totalRowTiles = 0;
		for (int rowIndex = 0; rowIndex < this.getRows(); rowIndex += this.tileSize) {
			totalColTiles = 0;
			totalRowTiles++;
			for (int colIndex = 0; colIndex < this.getCols(); colIndex += this.tileSize ) {
				totalColTiles++;
			}
		}
		
		int tilesCols = totalColTiles * this.tileSize;
		int tilesRows = totalRowTiles * this.tileSize;
		
		logger.info(getClass().getSimpleName() + " Rows: " +  this.getRows() 
											   + " Cols: " + this.getCols()
											   + " Num Cells " + (this.getRows() * this.getCols())
											   + " Tile Cols " + tilesCols + " Tile Row "+ tilesRows);

		
		double northDistanceMeters = (double) tilesRows* this.getGridSpacingMeters();
		double eastDistanceMeters = (double) tilesCols* this.getGridSpacingMeters();
		
		LocalDownFrame southWestFrame = new LocalDownFrame(lowerLeft.getEcfFromLatLon());

		VectorMath newPos = southWestFrame.getRelativePosition( northDistanceMeters,
																eastDistanceMeters,
																LocalDownFrame.RelativePositionOrder.NORTH_THEN_EAST);
		Vertex gridlowerRight = Vertex.getLatLonFromEcf(newPos); 
		this.setCcdata( new CrossCovData(findAverageLatLon(upperLeft, gridlowerRight)));
       
		int index = 0;
		int tileIndex = 0;
		
		for (int rowIndex = 0; rowIndex < this.getRows(); rowIndex += this.tileSize) {
			for (int colIndex = 0; colIndex < this.getCols(); colIndex += this.tileSize ) {
				SpatialTile tile = new SpatialTile(rowIndex, colIndex, index, tileIndex++);
				tile.createGrid(tileSize, tileSize, southWestFrame, this.getGridSpacingMeters(), this.getCcdata());
				this.addTile(tile);
				index += this.tileSize * this.tileSize;
			}
		}
	}

	public INode create(SpatialGridPoint loc, Direction dir, INode parent, int depth) {
		SpatialGridPoint rtn = null;
		if ( loc instanceof SpatialGridPoint ) {
			rtn = SpatialGridPoint.class.cast(loc);
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
	@JsonGetter("tileSize")
	@DynamoDBAttribute(attributeName = "tileSize")
	public int getTileSize()
	{
		return this.tileSize;
	}

	@JsonSetter("tileSize")
	protected void setTileSize(int value)
	{
		this.tileSize = value;
	}
	
	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public KDTree getTree() {
		throw new IllegalArgumentException("opration not supported");
	}

	@JsonIgnore
	@Override
	public SpatialGridPoint getNextGridPoint(
			SpatialGridPoint gridPt) {
		throw new UnsupportedOperationException();
	}
		
}
