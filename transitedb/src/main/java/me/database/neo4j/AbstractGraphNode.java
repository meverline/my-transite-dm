package me.database.neo4j;

import java.util.Map;

import me.transit.database.TransitData;

public abstract class AbstractGraphNode {
	
	/**
	 * Get The node properties
	 * @return
	 */
	public abstract Map<String, String> getProperties();
	
	public String makeKey() {
		TransitData data = (TransitData) this;
		StringBuffer key = new StringBuffer();
		key.append(data.getId());
		key.append("@");
		key.append(data.getAgency().getName());
		return key.toString();
	}
}
