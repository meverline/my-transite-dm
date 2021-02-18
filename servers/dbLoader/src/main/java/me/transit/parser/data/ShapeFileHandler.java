package me.transit.parser.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import me.transit.dao.RouteGeometryDao;
import me.transit.database.RouteGeometry;

@Component(value="shapeFileHandler")
public class ShapeFileHandler extends AbstractFileHandler {

	private Log log = LogFactory.getLog(getClass().getName());
	protected final GeometryFactory factory = new GeometryFactory();
	private RouteGeometryDao routeGeometryDao;

	/**
	 * 
	 * @param blackboard
	 */
	@Autowired
	public ShapeFileHandler(RouteGeometryDao routeGeometryDao, Blackboard blackboard) {
		super(blackboard);
		this.routeGeometryDao = Objects.requireNonNull(routeGeometryDao, "routeGeometryDao can not be null");
	}
	
	/*
	 * 
	 */
	@Override
	public String handlesFile() {
		return "shapes.txt";
	}


	/**
	 * @return the factory
	 */
	public GeometryFactory getFactory() {
		return factory;
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

				RouteGeometry db = new RouteGeometry();
				db.setAgency(getBlackboard().getAgency());
				db.setId(id);
				db.setShape(getFactory().createLineString(array));

				if ( db.valid()) {
					routeGeometryDao.save(db);
					getBlackboard().getShapes().put(id, db);
				} else {
					log.error("Invalid Shape: " + db.getAgency() + " " + db.getId());
				}
			}

		} catch (Exception e) {
			log.error("ID: " + id + " : coords size " + coords.size() + "\n" + e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.parser.data.FileHandler#parse(java.lang.String)
	 */
	@Override
	public boolean parse(String shapeFile) throws Exception{
		boolean rtn = false;

		File fp = new File(shapeFile);
		if (!fp.exists()) {
			log.error("file does not exist: " + shapeFile);
			return rtn;
		}

		try (BufferedReader inStream = new BufferedReader(new FileReader(shapeFile))) {

			if (!inStream.ready()) {
				log.error("file is empty: " + shapeFile);
				return rtn;
			}
			List<String> header = new ArrayList<String>();
			Map<String, Integer> indexMap = processHeader(inStream.readLine(), header);
			log.info(header);

			List<Coordinate> coords = new ArrayList<Coordinate>();
			String current = null;
			while (inStream.ready()) {
				String line = inStream.readLine();
				String data[] = line.split(",");
				String id = data[indexMap.get("shape_id")].trim();

				if (current == null) {
					current = id;
				}

				if (current.compareTo(id) != 0) {
					saveShape(current, coords);
					coords.clear();
					current = id;
					rtn = true;
				}

				double lat = Double.parseDouble(data[indexMap.get("shape_pt_lat")]);
				double lon = Double.parseDouble(data[indexMap.get("shape_pt_lon")]);
				coords.add(new Coordinate(lat, lon));
			}
			saveShape(current, coords);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			rtn = false;
		}
		return rtn;
	}

}
