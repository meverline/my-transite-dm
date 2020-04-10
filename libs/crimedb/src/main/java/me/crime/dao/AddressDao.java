package me.crime.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.crime.database.Address;
import me.database.hibernate.AbstractHibernateDao;

@SuppressWarnings("deprecation")
@Repository(value="addressDao")
@Scope("singleton")
public class AddressDao extends AbstractHibernateDao<Address> {
	
	@Autowired
	public AddressDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(Address.class, aSessionFactory);
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	public Address loadAddress(String id) {

		try {

			Session session = getSession();
		
			Query<Address> query = (Query<Address>) session.createQuery("from Address as addr where addr.location = :loc", Address.class);
			
			
			query.setParameter("loc", id);

			Address rtn = null;
			Object obj = query.uniqueResult();
			if ( obj != null ) {
				rtn = Address.class.cast( obj);
			}

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
			return toAddress(query.list());

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
			return toAddress(query.list());

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;
	}
	
}
