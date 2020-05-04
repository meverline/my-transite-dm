package org.dm.transit.dao;

import java.sql.SQLException;

import org.dm.transit.model.UserJob;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import me.database.hibernate.AbstractHibernateDao;

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
