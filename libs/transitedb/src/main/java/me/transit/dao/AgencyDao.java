
package me.transit.dao;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.database.hibernate.AbstractHibernateDao;
import me.transit.database.Agency;
import org.springframework.transaction.annotation.Transactional;

@Repository(value="agencyDao")
@Scope("singleton")
@Transactional
public class AgencyDao extends AbstractHibernateDao<Agency> {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public AgencyDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(Agency.class, aSessionFactory);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public synchronized Agency findByName(String id) {
		return this.loadByField(id, "name");
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public synchronized List<Agency> findAllByName(String id) {
		List<Agency> aList = null;
		try {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agency> crit = builder.createQuery(Agency.class);
			
			Root<?> root = crit.from(Agency.class);
			
			crit.where(
				builder.equal(root.get("name"), id)
			);
			
			aList = session.createQuery(crit).getResultList();
			
		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}
		return aList;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public synchronized Agency loadById(String id) {
	    Agency rtn = null;

		try {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agency> crit = builder.createQuery(Agency.class);
			
			Root<Agency> root = crit.from(Agency.class);
			
			crit.where(
					builder.equal(root.get("id"), id)
			);
			
			rtn = session.createQuery(crit).getSingleResult();
			
		} catch (NoResultException ex) {
			rtn = null;
		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return rtn;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Agency> list()
	{
		List<Agency> rtn = null;
		try {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agency> crit = builder.createQuery(Agency.class);
			
			Root<Agency> root = crit.from(Agency.class);
			
			crit.orderBy(builder.desc(root.get("name")));
			rtn = session.createQuery(crit).getResultList();
			
		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return rtn;
	}
	
}
