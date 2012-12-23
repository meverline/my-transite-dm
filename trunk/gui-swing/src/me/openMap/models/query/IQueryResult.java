package me.openMap.models.query;

import java.util.HashMap;
import java.util.List;

import me.openMap.utils.IOverlay;

public interface IQueryResult {
	
	
	public final String NAME = "Name";
	public final String AGENCY = "Agency";
	public final String STREET = "Street";
	public final String ROUTE = "Route";
	public final String LOCATION = "Location";
	public final String URL = "url";

	/**
	 * 
	 * @return
	 */
	public List<IQueryResult> getChildNodes();
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String,Object> getData();
	
	/**
	 * 
	 * @return
	 */
	public boolean hasChildren();
	
	/**
	 * 
	 * @return
	 */
	public boolean hasOverlay();
	
	/**
	 * 
	 * @return
	 */
	public IOverlay getOverlay();
}
