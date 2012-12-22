package me.transit.dao;

import java.util.List;

import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.TransitStop;

public interface TransiteStopDao extends TransitDao {

	/**
	 * 
	 * @param constraint
	 * @return
	 */
	public abstract List<TransitStop> query(StopQueryConstraint constraint);

	/**
	 * 
	 * @param stopId
	 * @return
	 */
	public abstract List<TransitStop> query(int stopId);

}