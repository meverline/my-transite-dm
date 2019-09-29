package me.transit.parser.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

import me.factory.DaoBeanFactory;
import me.transit.dao.RouteGeometryDao;
import me.transit.database.RouteGeometry;

public class ShapeFileHandler extends FileHandler {

	private Log log = LogFactory.getLog(getClass().getName());

	/**
	 * 
	 * @param blackboard
	 */
	public ShapeFileHandler(Blackboard blackboard) {
		super(blackboard);
	}

	/**
	 * 
	 * @param id
	 * @param coords
	 */
	private void saveShape(String id, List<Coordinate> coords) {
		try {
			Coordinate array[] = new Coordinate[coords.size()];
			coords.toArray(array);
			if (array.length > 1) {
				LineString poly = factory.createLineString(array);

				RouteGeometry db = new RouteGeometry();
				db.setAgency(getBlackboard().getAgency());
				db.setId(id);
				db.setShape(poly);

				RouteGeometryDao dao = RouteGeometryDao.class
						.cast(DaoBeanFactory.create().getDaoBean(RouteGeometryDao.class));
				dao.save(db);
				getBlackboard().getShaps().put(id, db);
			}
		} catch (Exception e) {
			log.error("ID: " + id + " : coords size " + coords.size() + "\n" + e.getLocalizedMessage(), e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.parser.data.FileHandler#parse(java.lang.String)
	 */
	@Override
	public void parse(String shapeFile) {
		try {

			File fp = new File(shapeFile);
			if (!fp.exists()) {
				return;
			}

			BufferedReader inStream = new BufferedReader(new FileReader(shapeFile));
			if (!inStream.ready()) {
				inStream.close();
				return;
			}
			List<String> header = new ArrayList<String>();
			Map<String, Integer> indexMap = processHeader(inStream.readLine(), "shape", header);

			List<Coordinate> coords = new ArrayList<Coordinate>();
			String current = null;
			while (inStream.ready()) {
				String line = inStream.readLine();
				String data[] = line.split(",");
				String id = data[indexMap.get(FileHandler.ID)].trim();

				if (current == null) {
					current = id;
				}

				if (current.compareTo(id) != 0) {
					saveShape(current, coords);
					coords.clear();
					current = id;
				}

				double lat = Double.parseDouble(data[indexMap.get("PtLat")]);
				double lon = Double.parseDouble(data[indexMap.get("PtLon")]);
				coords.add(new Coordinate(lat, lon));
			}
			saveShape(current, coords);
			inStream.close();

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

}
