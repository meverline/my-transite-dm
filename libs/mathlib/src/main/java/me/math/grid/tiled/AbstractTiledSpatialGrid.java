package me.math.grid.tiled;

import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.persistence.Column;

import me.math.LocalDownFrame;
import me.math.VectorMath;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridOverlay;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.INode;
import me.math.kdtree.INode.Direction;
import me.math.kdtree.INodeCreator;
import me.math.kdtree.KDTree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
		@Type(value = SpatialTile.class, name = "SpatialTile"),
		@Type(value = TiledSpatialGrid.class, name = "TiledSpatialGrid")})
public abstract class AbstractTiledSpatialGrid extends AbstractSpatialGridOverlay implements INodeCreator {
	

	private Log logger = LogFactory.getLog(TiledSpatialGrid.class);

	@Column(name="tileSize" )
	private int tileSize = 32;
	
	/**
	 * 
	 * @param index
	 * @param row
	 * @param col
	 * @return
	 */
	public abstract TiledSpatialGridPoint get(int index, int row, int col) throws UnknownHostException;

	/**
	 * 
	 * @param index
	 * @param gridIndex
	 * @return
	 */
	public abstract AbstractSpatialGridPoint get(int index, int gridIndex);

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
	public TiledSpatialGridPoint getEntry(int row, int column) throws UnknownHostException {
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
    public AbstractSpatialGridPoint getEntry(int index) throws UnknownHostException {
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
		CrossCovData data = new CrossCovData(findAverageLatLon(upperLeft, gridlowerRight));
       
		int index = 0;
		int tileIndex = 0;
		
		for (int rowIndex = 0; rowIndex < this.getRows(); rowIndex += this.tileSize) {
			for (int colIndex = 0; colIndex < this.getCols(); colIndex += this.tileSize ) {
				SpatialTile tile = new SpatialTile(rowIndex, colIndex, index, tileIndex++);
				tile.createGrid(tileSize, tileSize, southWestFrame, this.getGridSpacingMeters(), data);
				this.addTile(tile);
				index += this.tileSize * this.tileSize;
			}
		}
		
		this.setCrossCovariance(data.crossCovariance());
	}

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
	@Column(name="tileSize")
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
	
	///////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////
	
	public static class CrossCovData {
		
	   private double d_latitude = 0.0;
	   private double d_longitude = 0.0;
       private int number = 0;
       private final Vertex avgPoint;
       
       public CrossCovData(Vertex pt) {
    	   avgPoint = pt;
       }
        
       public void addPoint(Vertex pt) {
    	   number++;
    	   d_latitude += pt.getLatitudeDegress() - avgPoint.getLatitudeDegress();
           d_longitude += pt.getLongitudeDegress() - avgPoint.getLongitudeDegress();
       }
       
       public double crossCovariance() {
           d_latitude = d_latitude / ( number - 1.0);
           d_longitude = d_longitude / ( number - 1.0);
           return Math.abs((d_latitude * d_longitude) / ( number - 1.0));
       }
       
	}
	
	
}
