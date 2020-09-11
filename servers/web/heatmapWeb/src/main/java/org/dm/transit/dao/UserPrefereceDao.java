package org.dm.transit.dao;

import me.database.hibernate.AbstractHibernateDao;
import org.dm.transit.model.UserPreferences;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository(value="userPrefereceDao")
@Scope("singleton")
public class UserPrefereceDao extends AbstractHibernateDao<UserPreferences> {

    /**
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Autowired
    public UserPrefereceDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
        super(UserPreferences.class, aSessionFactory);
    }

}
