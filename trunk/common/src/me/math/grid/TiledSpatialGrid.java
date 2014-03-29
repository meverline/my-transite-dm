package me.math.grid;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import me.math.LocalDownFrame;
import me.math.Vertex;
import me.math.kdtree.INode;
import me.math.kdtree.INode.Direction;
import me.math.kdtree.INodeCreator;
import me.math.kdtree.KDTree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Point;

@XStreamAlias("TiledDatabaseGrid")
public class TiledSpatialGrid extends AbstractSpatialGrid implements INodeCreator {

	@XStreamOmitField()
	private Log logger = LogFactory.getLog(TiledSpatialGrid.class);

	@XStreamAlias("grid")
	private List<SpatialTile> grid_ = new ArrayList<SpatialTile>();
	@XStreamAlias("tileSize")
	private int tileSize = 32;
	
	/**
	 * 
	 * @param spacing
	 */
	public TiledSpatialGrid( double spacingInMeters)
	{
		init(spacingInMeters);
	}

	/**
	 *
	 * @param ul
	 * @param lr
	 * @param spacing
	 */
	public TiledSpatialGrid(Point ul, Point lr, double spacingInMeters) {
		init(spacingInMeters);
		setUpperLeft( new Vertex(ul.getX(), ul.getY()));
		setLowerRight( new Vertex(lr.getX(), lr.getY()));

		createGrid(getUpperLeft(), getLowerRight());
	}
	
	/**
	 *
	 * @param ul
	 * @param lr
	 * @param spacing
	 */
	public TiledSpatialGrid(Vertex ul, Vertex lr, double spacingInMeters) {
		init(spacingInMeters);
		setUpperLeft( ul);
		setLowerRight( lr );

		createGrid(getUpperLeft(), getLowerRight());
	}
	
	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public TiledSpatialGridPoint getEntry(int row, int column) {
		int colWidth = (this.getCols() / this.getTileSize()) +1;
		int index = ((row / this.getTileSize()) * colWidth) + (column / this.getTileSize());
		
		return this.grid_.get(index).getEntry( row, column);
	}
		
	/**
	 * 
	 * @param index
	 * @return
	 */
    public TiledSpatialGridPoint getEntry(int index) {
    	int size = (this.getTileSize() * this.getTileSize()); 	
		return grid_.get(index/size).getEntry(index);
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
		for (int rowIndex = 0; rowIndex < this.getRows(); rowIndex += this.tileSize) {
			for (int colIndex = 0; colIndex < this.getCols(); colIndex += this.tileSize ) {
				SpatialTile tile = new SpatialTile(rowIndex, colIndex, index, tileIndex++);
				this.grid_.add(tile);
				index += this.tileSize * this.tileSize;
			}
			// may need to account for ... missing cols and rows.
		}
		
		LocalDownFrame southWestFrame = new LocalDownFrame(lowerLeft.getEcfFromLatLon());
		for (SpatialTile tile : this.grid_ )  {
			tile.createGrid(tileSize, tileSize, southWestFrame);
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
	public List<SpatialTile> getTiles()
	{
		return this.grid_;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTileSize()
	{
		return this.tileSize;
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
	
	public void dump(Writer out) {
		
		StringBuilder buf = new StringBuilder();

		buf.append("Rows ");
		buf.append(this.getRows());
		buf.append(" Cols ");
		buf.append(this.getCols());
		buf.append("\n");
		
		try {
			out.write(buf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for ( SpatialTile tile : this.grid_) {
			tile.dump(out, this.getTileSize(), "     ");
		}
		
	}

}
