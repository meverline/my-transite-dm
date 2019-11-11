package me.transit.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.database.hibernate.HibernateConnection;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.TransitStop;

@Repository(value="transiteStopDao")
@Scope("singleton")
public class TransiteStopDao extends TransitDao<TransitStop> {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public TransiteStopDao() throws SQLException, ClassNotFoundException {
		super(TransitStop.class);
	}
	
	@Autowired
	public TransiteStopDao(HibernateConnection hibernateConnection) throws SQLException, ClassNotFoundException {
		super(TransitStop.class, hibernateConnection);
	}
		
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TrasiteStopDao#query(me.transit.dao.query.StopQueryConstraint)
	 */
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
