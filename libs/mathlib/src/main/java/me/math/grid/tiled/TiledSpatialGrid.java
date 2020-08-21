package me.math.grid.tiled;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.math.Vertex;
import org.locationtech.jts.geom.Point;


public class TiledSpatialGrid extends AbstractTiledSpatialGrid {

	private List<SpatialTile> grid_ = new ArrayList<>();
	
	/**
	 * 
	 * @param spacingInMeters
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
	
	@Override
	public TiledSpatialGridPoint get(int index, int row, int column) {
		return this.grid_.get(index).getEntry( row, column);
	}

	@Override
	public TiledSpatialGridPoint get(int index, int gridIndex) {
		return grid_.get(index).getEntry(gridIndex);
	}

	@Override
	protected void addTile(SpatialTile tile) {
		this.grid_.add(tile);
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
				TiledSpatialGridPoint pt = this.getEntry(row, col);
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
	
}
