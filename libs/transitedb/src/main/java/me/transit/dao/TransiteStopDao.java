package me.transit.dao;

import lombok.extern.apachecommons.CommonsLog;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.TransitStop;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository(value="transiteStopDao")
@Scope("singleton")
@Transactional
@CommonsLog
public class TransiteStopDao extends TransitDao<TransitStop> {
		
	@Autowired
	public TransiteStopDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(TransitStop.class, aSessionFactory);
	}
		
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TrasiteStopDao#query(me.transit.dao.query.StopQueryConstraint)
	 */
	@Transactional(readOnly = true)
	public List<TransitStop> query(StopQueryConstraint constraint) 
	{
		List<TransitStop> rtn = new ArrayList<>();

		try {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TransitStop> crit = builder.createQuery(TransitStop.class);
			
			@SuppressWarnings("unused")
			Root<TransitStop> root = crit.from(TransitStop.class);
			rtn = session.createQuery(crit).getResultList();
			
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return rtn;
	}

	@Transactional(readOnly = true)
	public TransitStop loadByUUID(long uuid) {
		return this.loadByUUID(uuid, TransitStop.class);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TrasiteStopDao#query(int)
	 */
	@Transactional(readOnly = true)
	public List<TransitStop> query(int stopId) 
	{
		List<TransitStop> rtn = new ArrayList<>();

		try {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TransitStop> crit = builder.createQuery(TransitStop.class);
			
			Root<TransitStop> root = crit.from(TransitStop.class);
			
			crit.where(
				builder.equal(root.get("id"), stopId)
			);

			rtn = session.createQuery(crit).getResultList();
			
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return rtn;
	}
	
}
