package me.transit.dao;

import java.util.List;

import me.transit.dao.hibernate.HibernateDao;
import me.transit.database.Agency;

public interface AgencyDao extends HibernateDao {

	/**
	 * 
	 * @param id
	 * @return
	 */
	public abstract Agency findByName(String id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public abstract List<Agency> findAllByName(String id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public abstract Object loadById(String id);

	/**
	 * 
	 * @return
	 */
	public abstract List<Agency> list();

}