package me.crime.dao;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.crime.database.URCCatagories;
import me.database.hibernate.AbstractHibernateDao;
import me.database.hibernate.HibernateConnection;

@SuppressWarnings("deprecation")
@Repository(value="urcCatagoriesDAO")
public class URCCatagoriesDAO extends AbstractHibernateDao<URCCatagories> {
	
	@Autowired
	public URCCatagoriesDAO(HibernateConnection aConnection) throws SQLException, ClassNotFoundException {
		super(URCCatagories.class, aConnection);
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public URCCatagories findURCbyCatagory(String id) {

		try {

			Session session = getSession();
			Query<URCCatagories> query = session.createQuery("from URCCatagories as urc where  urc.catagorie = :name");

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
	 */
	public void save(URCCatagories current) throws SQLException {
		URCCatagories cat;
		cat = this.findURCbyCatagory(current.getCatagorie());
		if (cat == null) {
			this.save(current);
		}
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<URCCatagories> listURCbyCrimeCatagory() {

		try {

			Session session = getSession();
			Query<URCCatagories> query = session.createQuery("from URCCatagories as urc order by CATAGORIE");

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
	@SuppressWarnings("unchecked")
	public HashSet<String> listUniqueURC() {

		try {

			Session session = getSession();
			Query<URCCatagories> query = session.createSQLQuery("select distinct urc_id from Crime");

			List<?> results = query.list();
			Iterator<?> it = results.iterator();

			/// note this is a list of the int id
			HashSet<String> rtn = new HashSet<String>();
			while( it.hasNext() ) {
				BigInteger id = BigInteger.class.cast(it.next());

				Query<URCCatagories> urc = session.createQuery("from URCCatagories as urc where urc.id = " + id.toString());

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
