package me.config;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.crime.dao.AddressDao;
import me.crime.dao.CrimeDao;
import me.crime.dao.URCCatagoriesDAO;
import me.crime.dao.impl.AddressDaoImpl;
import me.crime.dao.impl.CrimeDaoImpl;
import me.crime.dao.impl.URCCatagoriesDAOImpl;

/**
 * 
 * @author markeverline
 *
 */
@Configuration
public class AppConfig extends AppConfigBase {

	/*
	 * (non-Javadoc)
	 * @see me.config.AppConfigBase#packageToScan()
	 */
	@Override
	protected String[] packageToScan() {
		String pscan[] = { "me.crime.database" };
		return pscan;
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public CrimeDao crimeDao() throws ClassNotFoundException, SQLException {
		return new CrimeDaoImpl(this.hibernateConnection());
	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public AddressDao addressDao() throws ClassNotFoundException, SQLException {
		return new AddressDaoImpl(this.hibernateConnection());
	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public URCCatagoriesDAO uRCCatagoriesDAO() throws ClassNotFoundException, SQLException {
		return new URCCatagoriesDAOImpl(this.hibernateConnection());
	}
	
}
