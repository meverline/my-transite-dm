package me.transit.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.database.hibernate.AbstractHibernateDao;
import me.transit.database.Agency;

@SuppressWarnings("deprecation")
@Repository(value="agencyDao")
@Scope("singleton")
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
	public synchronized List<Agency> findAllByName(String id) {
		
		List<Agency> rtn = new ArrayList<Agency>();
		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("name", id));
			
			for ( Object obj : crit.list()) {
				rtn.add(Agency.class.cast(obj));
			}
			return rtn;

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public synchronized Object loadById(String id) {
		
		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("id", id));
			
			Object rtn = crit.uniqueResult();

			return rtn;

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Agency> list()
	{

		try {

			Session session = getSession();
			Query query = session.createQuery("from Agency as urc order by NAME");

			List results = query.list();
			Iterator it = results.iterator();

			List<Agency> rtn = new ArrayList<Agency>();
			while (it.hasNext()) {
			   rtn.add(Agency.class.cast(it.next()));
			}

			return rtn;
			
		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;
	}
	
}
