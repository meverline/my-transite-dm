package me.crime.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.crime.database.Address;
import me.database.hibernate.AbstractHibernateDao;
import me.database.hibernate.HibernateConnection;

@SuppressWarnings("deprecation")
@Repository(value="addressDao")
@Scope("singleton")
public class AddressDao extends AbstractHibernateDao<Address> {
	
	@Autowired
	public AddressDao(HibernateConnection aConnection) throws SQLException, ClassNotFoundException {
		super(Address.class, aConnection);
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Address loadAddress(String id) {

		try {

			Session session = getSession();
		
			Query<Address> query = session.createQuery("from Address as addr where addr.location = :loc");

			query.setParameter("loc", id);

			Address rtn = null;
			Object obj = query.uniqueResult();
			if ( obj != null ) {
				rtn = Address.class.cast( obj);
			}

			session.close();
			return rtn;

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}

	/**
	 * 
	 * @param results
	 * @return
	 */
	private List<Address> toAddress( List<?> results)
	{
		Iterator<?> it = results.iterator();

		List<Address> rtn = new ArrayList<Address>();
		while (it.hasNext()) {
		   rtn.add(Address.class.cast(it.next()));
		}
		return rtn;
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Address> listAddressByLocation(String location) {

		try {

			Session session = getSession();
			Query<Address> query = session.createQuery("from Address as addr " +
											  "where addr.location like :loc order by LOCATION");

			query.setString("loc", location + "%");

			List<Address> rtn = toAddress(query.list());
			session.close();
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
	@SuppressWarnings("unchecked")
	public List<Address> listAddressByLocation() {

		try {

			Session session = getSession();
			Query<Address> query = session.createQuery("from Address as addr order by LOCATION");

			List<Address> rtn = toAddress(query.list());
			session.close();
			return rtn;

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;
	}
	
}
