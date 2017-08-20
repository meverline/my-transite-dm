package me.transit.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import me.database.hibernate.HibernateConnection;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.TransitStop;

public class TransiteStopDaoImpl extends TransitDaoImpl<TransitStop> implements TransiteStopDao {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public TransiteStopDaoImpl() throws SQLException, ClassNotFoundException {
		super(TransitStop.class);
	}
	
	public TransiteStopDaoImpl(HibernateConnection aConnection) throws SQLException, ClassNotFoundException {
		super(TransitStop.class, aConnection);
	}
		
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TrasiteStopDao#query(me.transit.dao.query.StopQueryConstraint)
	 */
	@Override
	public List<TransitStop> query(StopQueryConstraint constraint) 
	{
		List<TransitStop> rtn = new ArrayList<TransitStop>();
		try {

			Session session = getSession();
			Criteria crit = constraint.getCirtera(session);
			
			for ( Object obj: crit.list()) {
				TransitStop st = TransitStop.class.cast(obj);
				
				Hibernate.initialize(st.getAgency());
				rtn.add(st);
			}
			session.close();

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return rtn;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TrasiteStopDao#query(int)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<TransitStop> query(int stopId) 
	{
		List<TransitStop> rtn = new ArrayList<TransitStop>();
		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(TransitStop.class);
			crit.add(Restrictions.eq("id", stopId));
			
			for ( Object obj: crit.list()) {
				TransitStop st = TransitStop.class.cast(obj);
				
				Hibernate.initialize(st.getAgency());
				rtn.add(st);
			}
			session.close();

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return rtn;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.TransitDao#loadById(long, java.lang.String)
	 */
	@Override
	public TransitStop loadById(String id, String agencyName) {
		TransitStop rtn = super.loadById(id, agencyName);
		Hibernate.initialize(rtn.getAgency());
		return rtn;
	}
	
}
