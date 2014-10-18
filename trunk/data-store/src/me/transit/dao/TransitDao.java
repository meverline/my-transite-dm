package me.transit.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import me.database.hibernate.HibernateDao;
import me.transit.database.Agency;

public interface TransitDao<T extends Serializable> extends HibernateDao<T> {

	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	public abstract void deleteByAgency(Agency agency) throws SQLException;

	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	public abstract List<Long> findByAgency(Agency agency) throws SQLException;

	/**
	 * 
	 */
	public abstract T loadById(String id, String agencyName);

}