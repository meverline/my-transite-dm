package me.transit.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface HibernateDao<T extends Serializable> {

	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	public abstract void save(T item) throws SQLException;

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
	public abstract T loadByUUID(Long id, Class<?> aClass);

}