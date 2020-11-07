package me.database.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class AbstractHibernateDao<T extends Serializable> {

	private Log log = LogFactory.getLog(AbstractHibernateDao.class);
	
	private final SessionFactory sessionFactory; 
	private final Class<?> daoClass;
	
	protected AbstractHibernateDao(Class<?> aClass, SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		
		daoClass = Objects.requireNonNull(aClass, "aClass cannot be null");
		sessionFactory = Objects.requireNonNull(aSessionFactory, "aSessionFactory cannot be null");
	}
	
	/**
	 * 
	 * @return
	 */
	public SessionFactory getSessionFactory() {
	    return this.sessionFactory;
	}
	
	/**
	 * @return the log
	 */
	protected Log getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	protected void setLog(Log log) {
		this.log = log;
	}

	/**
	 * @return the connection
	 */
	protected Session getSession() {
		try {
			return this.getSessionFactory().getCurrentSession();
		} catch ( Exception e) {
			return this.getSessionFactory().openSession();
		}
	}
	
	/**
	 * @return the daoClass
	 */
	protected final Class<?> getDaoClass() {
		return daoClass;
	}


	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	public synchronized T save(T item) throws SQLException {
			
		Transaction tx = null;

		try (Session session = this.getSession()) {
			tx = session.beginTransaction();
			session.saveOrUpdate(item);
			tx.commit();
		} catch (Exception ex) {
			log.error(ex);
			tx.rollback();
			throw new SQLException(ex.getLocalizedMessage());
		}
		return item;
	}
	
	/**
	 * 
	 * @param uuid
	 * @throws SQLException
	 */
	public synchronized void delete(long uuid) throws SQLException {
		
		Transaction tx = null;
		try (Session session = this.getSession()) {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<?> crit = builder.createQuery(this.getDaoClass());

			Root<?> root = crit.from(this.getDaoClass());

			crit.where(
					builder.equal(root.get("UUID"), uuid)
			);

			tx = session.beginTransaction();
			@SuppressWarnings("unchecked")
			T result = (T) session.createQuery(crit).getSingleResult();
			session.remove(result);
			tx.commit();

		} catch (NoResultException ex) {
			log.warn("unable to delete uuid: " + Long.toString(uuid));
			tx.rollback();
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
			tx.rollback();
		}
	}
	
	/**
	 * 
	 * @param uuids
	 * @throws SQLException
	 */
	public synchronized void delete(List<Long> uuids) throws SQLException {
		for (Long uuid : uuids) {
			this.delete(uuid);
		}
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	protected T loadByField(String id, String property) {
		T rtn = null;
		try (Session session = getSession()) {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<?> crit = builder.createQuery(this.getDaoClass());

			Root<?> root = crit.from(this.getDaoClass());

			crit.where(
					builder.equal(root.get(property), id)
			);

			@SuppressWarnings("unchecked")
			T result = (T) session.createQuery(crit).getSingleResult();
			rtn = result;

		} catch (NoResultException ex) {
			rtn = null;
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return rtn;

	}

	protected T loadByField(int id, String property) {
		T rtn = null;
		try (Session session = getSession()) {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<?> crit = builder.createQuery(this.getDaoClass());

			Root<?> root = crit.from(this.getDaoClass());

			crit.where(
					builder.equal(root.get(property), id)
			);

			@SuppressWarnings("unchecked")
			T result = (T) session.createQuery(crit).getSingleResult();
			rtn = result;

		} catch (NoResultException ex) {
			rtn = null;
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return rtn;

	}
	
	/**
	 * 
	 * @param id
	 * @param aClass
	 * @return
	 */
	public  T loadByUUID(Long id, @SuppressWarnings("rawtypes") Class aClass) {
		T rtn = null;

		try (Session session = getSession()) {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<?> crit = builder.createQuery(this.getDaoClass());

			Root<?> root = crit.from(this.getDaoClass());

			crit.where(
					builder.equal(root.get("UUID"), id)
			);

			@SuppressWarnings("unchecked")
			T result = (T) session.createQuery(crit).getSingleResult();
			rtn = result;

		} catch (NoResultException ex) {
			rtn = null;
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return rtn;

	}
	

	
}
