package me.transit.dao;

import java.sql.SQLException;
import java.util.List;

import me.transit.dao.hibernate.HibernateDao;
import me.transit.database.Agency;

public interface TransitDao extends HibernateDao {

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
	public abstract Object loadById(long id, String agencyName);

}