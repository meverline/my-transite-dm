package me.transit.graph.nodes;

import me.transit.database.TransitData;

public class TransiteNode {

	/**
	 * 
	 * @param data
	 * @return
	 */
	public String makeKey(TransitData data) {
		StringBuffer key = new StringBuffer();
		key.append(data.getId());
		key.append("@");
		key.append(data.getAgency().getName());
		return key.toString();
	}

}
