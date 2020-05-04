package me.database.neo4j;

import java.util.Map;


public abstract class AbstractGraphNode {

	public abstract String getId();
	/**
	 * Get The node properties
	 * @return
	 */
	public abstract Map<String, String> getProperties(String agencyName);

	public String makeKey(String agencyName) {
		return getId() + "@" + agencyName;
	}
}
