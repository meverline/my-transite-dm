package me.transit.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

public interface HibernateDao {

	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	public abstract void save(Object item) throws SQLException;

	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	public abstract void delete(long uuid) throws SQLException;

	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	public abstract void delete(List<Long> uuids) throws SQLException;

	/**
	 *
	 * @param id
	 * @return
	 */
	public abstract Object loadByUUID(Long id,
			@SuppressWarnings("rawtypes") Class aClass);

}