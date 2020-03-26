package me.database.neo4j;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.transit.database.TransitData;


public abstract class AbstractGraphNode {
	
	/**
	 * Get The node properties
	 * @return
	 */
	@JsonIgnore
	public abstract Map<String, String> getProperties(String agencyName);

	@JsonIgnore
	public String makeKey(String agencyName) {
		TransitData data = (TransitData) this;
		StringBuffer key = new StringBuffer();
		key.append(data.getId());
		key.append("@");
		key.append(agencyName);
		return key.toString();
	}
}
