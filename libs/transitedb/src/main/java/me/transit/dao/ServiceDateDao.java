package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.transit.database.ServiceDate;
import org.springframework.transaction.annotation.Transactional;

@Repository(value="serviceDateDao")
@Scope("singleton")
@Transactional
public class ServiceDateDao extends TransitDao<ServiceDate>  {

	/**
	 * 
	 * @param aSessionFactory
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public ServiceDateDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(ServiceDate.class, aSessionFactory);
	}
		
}
