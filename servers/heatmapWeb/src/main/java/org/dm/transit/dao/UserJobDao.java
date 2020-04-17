package org.dm.transit.dao;

import me.database.hibernate.AbstractHibernateDao;
import org.dm.transit.model.UserJob;
import org.dm.transit.model.UserPreferences;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

public class UserJobDao extends AbstractHibernateDao<UserJob> {

    /**
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Autowired
    public UserJobDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
        super(UserJob.class, aSessionFactory);
    }

}
