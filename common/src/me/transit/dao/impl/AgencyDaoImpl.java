package me.transit.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import me.transit.dao.AgencyDao;
import me.transit.dao.hibernate.HibernateDaoImpl;
import me.transit.database.Agency;

public class AgencyDaoImpl extends HibernateDaoImpl implements AgencyDao {

	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public AgencyDaoImpl() throws SQLException, ClassNotFoundException {
		super(Agency.class);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.AgencyDao#findByName(java.lang.String)
	 */
	@Override
	public synchronized Agency findByName(String id) {
		Object obj = this.loadByField(id, "name");
		if ( obj != null ) {
			return Agency.class.cast(obj);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.AgencyDao#findAllByName(java.lang.String)
	 */
	@Override
	public synchronized List<Agency> findAllByName(String id) {
		
		List<Agency> rtn = new ArrayList<Agency>();
		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("name", id));
			
			for ( Object obj : crit.list()) {
				rtn.add(Agency.class.cast(obj));
			}
			session.close();
			return rtn;

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.AgencyDao#loadById(java.lang.String)
	 */
	@Override
	public synchronized Object loadById(String id) {
		
		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("id", id));
			
			Object rtn = crit.uniqueResult();

			session.close();
			return rtn;

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.AgencyDao#list()
	 */
	@Override
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

			session.close();
			return rtn;
			
		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;
	}

	
}
