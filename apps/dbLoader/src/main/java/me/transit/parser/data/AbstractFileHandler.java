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
	 * @throws Exception 
	 */
	public abstract boolean parse(String shapeFile) throws Exception;

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
	 *
	 * @param text
	 * @return
	 */
	protected String removeBadChars(String text) {
		if (text == null) return null;
		// strips off all non-ASCII characters
		text = text.replaceAll("[^\\x00-\\x7F]", "");

		// erases all the ASCII control characters
		text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

		// removes non-printable characters from Unicode
		text = text.replaceAll("\\p{C}", "");

		return text.trim();
	}

	/**
	 * Process the header.
	 * 
	 * @param header
	 * @param order
	 * @return
	 */
	protected Map<String, Integer> processHeader(String header, List<String> order) {
		String [] fields = header.split(",");
		Map<String, Integer> indexMap = new HashMap<>();

		int ndx = 0;
		for (String fld : fields) {
			String item = removeBadChars(fld.replace('"', ' ').trim());
			order.add(item);
			indexMap.put(item, ndx);
			ndx++;
		}

		return indexMap;
	}

}
