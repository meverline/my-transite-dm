package me.transit.parser.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractFileHandler {

	protected static final String ID = "Id";
	private final Blackboard blackboard;

	/**
	 * 
	 * @param blackboard
	 */
	protected AbstractFileHandler(Blackboard blackboard) {
		this.blackboard = Objects.requireNonNull(blackboard, "blackboard can not be null");
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
	
	public void endProcess() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract String handlesFile();

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
		for (String fld : fields) {
			String item = fld.replace('"', ' ').trim();
			order.add(item);
			indexMap.put(item, ndx);
			ndx++;
		}

		return indexMap;
	}

}
