package me.transit.parser.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.GeometryFactory;

public abstract class FileHandler {

	protected static final String ID = "Id";

	protected static GeometryFactory factory = new GeometryFactory();
	private final Blackboard blackboard;

	/**
	 * 
	 * @param blackboard
	 */
	protected FileHandler(Blackboard blackboard) {
		this.blackboard = blackboard;
	}

	/**
	 * 
	 * @param shapeFile
	 */
	public abstract void parse(String shapeFile);

	/**
	 * @return the blackboard
	 */
	public Blackboard getBlackboard() {
		return blackboard;
	}

	/**
	 * Process the header.
	 * 
	 * @param header
	 * @param strip
	 * @return
	 */
	protected Map<String, Integer> processHeader(String header, String strip, List<String> order) {
		String fields[] = header.split(",");
		Map<String, Integer> indexMap = new HashMap<String, Integer>();

		int ndx = 0;
		String mapTo = null;
		for (String fld : fields) {
			String item = fld.replace('"', ' ').trim();
			if (item.indexOf('_') == -1) {
				mapTo = item.substring(0, 1).toUpperCase() + item.substring(1);
				order.add(mapTo);
				indexMap.put(mapTo, ndx);
			} else {
				String data[] = item.toLowerCase().split("_");
				StringBuilder fieldName = new StringBuilder();

				for (String name : data) {
					if (name.compareTo(strip) != 0) {
						mapTo = name.substring(0, 1).toUpperCase() + name.substring(1);
						fieldName.append(mapTo);
					}
				}
				order.add(fieldName.toString());
				indexMap.put(fieldName.toString(), ndx);
			}
			ndx++;
		}

		return indexMap;
	}

}
