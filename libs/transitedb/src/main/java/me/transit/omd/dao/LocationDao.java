package me.transit.omd.dao;

import me.database.hibernate.AbstractHibernateDao;
import me.transit.omd.data.Location;
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
import java.util.List;

@Repository(value="locationDao")
@Scope("singleton")
public class LocationDao extends AbstractHibernateDao<Location> {

    /**
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Autowired
    public LocationDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
        super(Location.class, aSessionFactory);
    }

    @Transactional(readOnly = true)
    public synchronized Location findById(int id) {
        return this.loadByField(id, "id");
    }

    @Transactional(readOnly = true)
    public synchronized Location findByTitle(String title) {
        return this.loadByField(title, "title");
    }

    @Transactional(readOnly = true)
    public List<Location> list()
    {
        List<Location> rtn = null;
        try  {
            Session session = getSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Location> crit = builder.createQuery(Location.class);

            Root<Location> root = crit.from(Location.class);

            crit.orderBy(builder.desc(root.get("title")));
            rtn = session.createQuery(crit).getResultList();

        } catch (HibernateException ex) {
            getLog().error(ex.getLocalizedMessage(), ex);
        }

        return rtn;
    }

}
