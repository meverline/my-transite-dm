package me.crime.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import me.crime.dao.AddressDao;
import me.crime.database.Address;
import me.database.hibernate.AbstractHibernateDao;

public class AddressDaoImpl extends AbstractHibernateDao<Address> implements
		AddressDao {

	public AddressDaoImpl() throws SQLException, ClassNotFoundException {
		super(Address.class);
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	public Address loadAddress(String id) {

		try {

			Session session = getSession();
			Query query = session.createQuery("from Address as addr where addr.location = :loc");

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
	public List<Address> listAddressByLocation(String location) {

		try {

			Session session = getSession();
			Query query = session.createQuery("from Address as addr " +
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
	public List<Address> listAddressByLocation() {

		try {

			Session session = getSession();
			Query query = session.createQuery("from Address as addr order by LOCATION");

			List<Address> rtn = toAddress(query.list());
			session.close();
			return rtn;

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;
	}
	
}
