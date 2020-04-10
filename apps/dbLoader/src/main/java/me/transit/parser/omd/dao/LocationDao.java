package me.transit.parser.omd.dao;

import me.database.hibernate.AbstractHibernateDao;
import me.transit.database.Agency;
import me.transit.parser.omd.Location;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
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

    public synchronized Location findById(int id) {
        return this.loadByField(id, "id");
    }

    public synchronized Location findByTitle(String title) {
        return this.loadByField(title, "title");
    }

    public List<Location> list()
    {
        List<Location> rtn = null;
        try (Session session = getSession()) {

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
