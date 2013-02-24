package me.crime.dao.impl;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import me.crime.dao.URCCatagoriesDAO;
import me.crime.database.URCCatagories;
import me.transit.dao.hibernate.AbstractHibernateDao;

public class URCCatagoriesDAOImpl extends AbstractHibernateDao<URCCatagories> implements
		URCCatagoriesDAO {

	public URCCatagoriesDAOImpl() throws SQLException, ClassNotFoundException {
		super(URCCatagories.class);
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	public URCCatagories findURCbyCatagory(String id) {

		try {

			Session session = getSession();
			Query query = session.createQuery("from URCCatagories as urc where  urc.catagorie = :name");

			query.setString("name", id);

			URCCatagories rtn = URCCatagories.class.cast(query.uniqueResult());

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
	public  List<URCCatagories> listURCbyCrimeCatagory() {

		try {

			Session session = getSession();
			Query query = session.createQuery("from URCCatagories as urc order by CATAGORIE");

			List<?> results = query.list();
			Iterator<?> it = results.iterator();

			List<URCCatagories> rtn = new ArrayList<URCCatagories>();
			while (it.hasNext()) {
			   rtn.add(URCCatagories.class.cast(it.next()));
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
	 * @return
	 */
	public HashSet<String> listUniqueURC() {

		try {

			Session session = getSession();
			Query query = session.createSQLQuery("select distinct urc_id from Crime");

			List<?> results = query.list();
			Iterator<?> it = results.iterator();

			/// note this is a list of the int id
			HashSet<String> rtn = new HashSet<String>();
			while( it.hasNext() ) {
				BigInteger id = BigInteger.class.cast(it.next());

				Query urc = session.createQuery("from URCCatagories as urc where urc.id = " + id.toString());

				Object obj = urc.uniqueResult();
				URCCatagories aUrc = URCCatagories.class.cast(obj);
				rtn.add(aUrc.getCatagorie());
			}

			session.close();
			return rtn;

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}

}
