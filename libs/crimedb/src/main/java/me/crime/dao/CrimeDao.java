package me.crime.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.crime.database.Crime;
import me.database.hibernate.AbstractHibernateDao;
import me.transit.dao.query.tuple.IQueryTuple;

@SuppressWarnings("deprecation")
@Repository(value="crimeDao")
@Scope("singleton")
@CommonsLog
public class CrimeDao extends AbstractHibernateDao<Crime> {

	@Autowired
	public CrimeDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(Crime.class, aSessionFactory);
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Crime loadCrime(String id) {

		try {

			Session session = getSession();
			Query<Crime> query = session.createQuery("from Crime crime where crime.crimeNumber = :id");

			query.setParameter("id", id);

			Crime rtn = Crime.class.cast( query.uniqueResult());

			if (rtn != null) {
				Hibernate.initialize(rtn.getAddress());
				Hibernate.initialize(rtn.getCodes());
			}

			return rtn;

		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public List<Crime> queryCrimes(IQueryTuple list) {

		try {

			Session session =  getSession();;
			Query<Crime> query = (Query<Crime>) session.createQuery("from Crime crime select *", Crime.class);
			return toCrime( query.getResultList());

		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}
	
	/**
	 * 
	 * @param results
	 * @return
	 */
	private List<Crime> toCrime(List<?> results)
	{
		List<Crime> rtn = new LinkedList<Crime>();
		Iterator<?> it = results.iterator();
		
		while (it.hasNext()) {

			Crime c = Crime.class.cast(it.next());

			Hibernate.initialize(c.getCodes());
			Hibernate.initialize(c.getAddress());
			rtn.add(c);
		}
		return rtn;

	}
	
	/**
	 *
	 * @return
	 */
	public synchronized List<Crime> listCrimes() {

		try {

			Session session = getSession();

			Criteria crit = getBasicCriteria(session, null, null)
				.addOrder(Order.asc("startDate"))
				.addOrder(Order.asc("codes"));

			List<?> results = crit.list();
			Iterator<?> it = results.iterator();

			List<Crime> rtn = new ArrayList<Crime>();
			while (it.hasNext()) {

				Crime c = Crime.class.cast(it.next());

				Hibernate.initialize(c.getCodes());
				Hibernate.initialize(c.getAddress());
				rtn.add(c);

			}

			return rtn;

		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}
	
	/**
	 * 
	 * @param session
	 * @param start
	 * @param end
	 * @return
	 */
	private Criteria getBasicCriteria(Session session, Calendar start, Calendar end)
	{
		Criteria crit = session.createCriteria(Crime.class);

		if(start != null)
		{
			start.set(Calendar.SECOND, 0);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.HOUR_OF_DAY, 0);
			start.add(Calendar.SECOND, -1);

			crit.add(Restrictions.conjunction()
					.add(Restrictions.gt("startDate", start)));
		}
		
		if(end != null)
		{
			end.set(Calendar.SECOND, 0);
			end.set(Calendar.MINUTE, 0);
			end.set(Calendar.HOUR_OF_DAY, 0);

			crit.add(Restrictions.conjunction()
						.add(Restrictions.lt("startDate", end)));
		}


		return crit;
	}

	/**
	 * 
	 */
	public synchronized List<Crime> getNextCrimes(long lastMaxId, int maxResults) 
	{
		try {
			
				Session session = getSession();
	
				Criteria crit = getBasicCriteria(session, null, null);
				crit.add(Restrictions.gt("id", lastMaxId))
				.addOrder(Order.asc("id"))
				.setMaxResults(maxResults);
				
				List<?> results = crit.list();
				return toCrime(results);
				
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return null;
		}
	}



}
