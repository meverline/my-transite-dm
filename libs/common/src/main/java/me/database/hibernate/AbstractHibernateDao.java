package me.database.hibernate;

import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@CommonsLog
public abstract class AbstractHibernateDao<T extends Serializable> {

	private final SessionFactory sessionFactory; 
	private final Class<T> daoClass;
	
	protected AbstractHibernateDao(Class<T> aClass, SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
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
	 * @return the connection
	 */
	protected Session getSession() {
		return this.getSessionFactory().getCurrentSession();
	}

	/**
	 * @return the daoClass
	 */
	protected final Class<T> getDaoClass() {
		return daoClass;
	}


	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	@Transactional
	public synchronized T save(T item) throws SQLException {

		try {
			this.getSession().saveOrUpdate(item);
		} catch (Exception ex) {
			log.error(ex);
			throw new SQLException(ex.getLocalizedMessage());
		}
		return item;
	}
	
	/**
	 * 
	 * @param uuid
	 * @throws SQLException
	 */
	@Transactional
	public synchronized void delete(long uuid) throws SQLException {
		try {
			Session session = this.getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> crit = builder.createQuery(this.getDaoClass());

			Root<T> root = crit.from(this.getDaoClass());

			crit.where(
					builder.equal(root.get("UUID"), uuid)
			);

			@SuppressWarnings("unchecked")
			T result = (T) session.createQuery(crit).getSingleResult();
			session.remove(result);

		} catch (NoResultException ex) {
			log.warn("unable to delete uuid: " + Long.toString(uuid));
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
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
		try  {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> crit = builder.createQuery(this.getDaoClass());

			Root<T> root = crit.from(this.getDaoClass());

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
		try {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> crit = builder.createQuery(this.getDaoClass());

			Root<T> root = crit.from(this.getDaoClass());

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
	@Transactional(readOnly = true)
	protected  T loadByUUID(Long id, @SuppressWarnings("rawtypes") Class aClass) {
		T rtn = null;

		try {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> crit = builder.createQuery(this.getDaoClass());

			Root<T> root = crit.from(this.getDaoClass());

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
