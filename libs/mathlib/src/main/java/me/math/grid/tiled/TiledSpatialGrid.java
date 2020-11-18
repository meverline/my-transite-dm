package me.math.grid.tiled;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import me.database.nsstore.IDocument;
import me.math.Vertex;
import me.math.grid.SpatialGridPoint;
import me.math.kdtree.INodeCreator;
import org.locationtech.jts.geom.Point;

@JsonRootName(value = "TiledSpatialGrid")
public class TiledSpatialGrid extends AbstractTiledSpatialGrid implements IDocument {

	private long uuid = -1;
	private String docId = null;
	private Vertex upperLeft_ = null;
	private Vertex lowerRight_ = null;
	private final List<SpatialTile> grid_ = new ArrayList<>();

	/**
	 *
	 */
	public TiledSpatialGrid( )
	{
	}
	
	/**
	 * 
	 * @param spacingInMeters
	 */
	public TiledSpatialGrid( double spacingInMeters)
	{
		init(spacingInMeters);
	}

	/**
	 *
	 * @param ul
	 * @param lr
	 * @param spacingInMeters
	 * @throws UnknownHostException 
	 * @throws SQLException 
	 */
	public TiledSpatialGrid(Point ul, Point lr, double spacingInMeters) throws UnknownHostException, SQLException {
		init(spacingInMeters);
		setUpperLeft( new Vertex(ul.getX(), ul.getY()));
		setLowerRight( new Vertex(lr.getX(), lr.getY()));

		createGrid(getUpperLeft(), getLowerRight());
	}
	
	/**
	 *
	 * @param ul
	 * @param lr
	 * @param spacingInMeters
	 * @throws UnknownHostException 
	 * @throws SQLException 
	 */
	public TiledSpatialGrid(Vertex ul, Vertex lr, double spacingInMeters) throws UnknownHostException, SQLException {
		init(spacingInMeters);
		setUpperLeft( ul);
		setLowerRight( lr );
		createGrid(getUpperLeft(), getLowerRight());
	}

	/**
	 *
	 * @return
	 */
	@JsonGetter("uuid")
	@DynamoDBAttribute(attributeName = "uuid")
	public long getUuid() {  return uuid; }

	/**
	 *
	 * @param uuid
	 */
	@JsonSetter("uuid")
	public void setUuid(long uuid) {  this.uuid = uuid; }

	@JsonGetter("_id")
	@DynamoDBAttribute(attributeName = "id")
	@Override
	public String getDocId() {
		return this.docId;
	}

	@JsonSetter("_id")
	@Override
	public void setDocId(String docId) {
		this.docId = docId;
	}

	@JsonIgnore
	@Override
	public SpatialGridPoint get(int index, int row, int column) {
		return this.grid_.get(index).getEntry( row, column);
	}

	@JsonIgnore
	@Override
	public SpatialGridPoint get(int index, int gridIndex) {
		return grid_.get(index).getEntry(gridIndex);
	}

	@Override
	protected void addTile(SpatialTile tile) {
		tile.setGridUUID(this.getUuid());
		this.grid_.add(tile);
	}
	    
	/**
	 * 
	 * @return
	 */
	@JsonGetter("tiles")
	public List<SpatialTile> getTiles() { return this.grid_; }

	@JsonSetter("tiles")
	public void setTiles(List<SpatialTile> tileList) {
		this.grid_.clear();
		this.grid_.addAll(tileList);
	}
	
	/**
	 * 
	 * @param out
	 */
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
	
	/**
	 * 
	 * @param out
	 * @throws UnknownHostException
	 */
	public void toCSV(PrintStream out, boolean iterpolationValue ) throws UnknownHostException {
		
		for (int row = 0; row < this.getRows(); row++ ) {
			for (int col = 0; col < this.getCols(); col++ ) {
				SpatialGridPoint pt = this.getEntry(row, col);
				if ( col != 0 ) { out.print(","); }
				if ( pt.getData() != null ) {
					if ( iterpolationValue ) {
						out.print(pt.getData().getInterpolationValue());
					} else {
						out.print(pt.getData().getValue());
					}
				}
			}
			out.println();
		}
		
	}

	/**
	 *
	 * @return
	 */
	@JsonIgnore
	public Vertex getLowerRight() {
		return lowerRight_;
	}

	/**
	 *
	 * @param lowerRight_
	 */
	@JsonIgnore
	public void setLowerRight(Vertex lowerRight_) {
		this.lowerRight_ = lowerRight_;
	}

	/**
	 *
	 * @return
	 */
	@JsonIgnore
	public Vertex getUpperLeft() {
		return upperLeft_;
	}

	/**
	 * @param upperLeft_
	 */
	@JsonIgnore
	public void setUpperLeft(Vertex upperLeft_) {
		this.upperLeft_ = upperLeft_;
	}

	/**
	 *
	 * @return
	 */
	@JsonIgnore
	public double getMaxLatitude()
	{
		return lowerRight_.getLatitudeDegress();
	}

	/**
	 *
	 * @return
	 */
	@JsonIgnore
	public double getMaxLongitude()
	{
		return lowerRight_.getLongitudeDegress();
	}


}
