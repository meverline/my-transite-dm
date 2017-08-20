package me.openMap.config;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import me.crime.dao.AddressDao;
import me.crime.dao.CrimeDao;
import me.crime.dao.URCCatagoriesDAO;
import me.crime.dao.impl.AddressDaoImpl;
import me.crime.dao.impl.CrimeDaoImpl;
import me.crime.dao.impl.URCCatagoriesDAOImpl;
import me.crime.database.Address;
import me.crime.database.Crime;
import me.crime.database.URCCatagories;
import me.datamining.AbstractDensityEstimateAlgorithm;
import me.datamining.ClusteringAlgorithm;
import me.datamining.Kernel.Epanechnikov;
import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.bandwidth.SlivermanRule;
import me.datamining.cluster.STING;
import me.datamining.densityEstimate.NonAdaptiveKDE;
import me.factory.TransitedAnotationConfig;

@Configuration
public class OpenMapAnotationConfig extends TransitedAnotationConfig {
		
	/*
	 * (non-Javadoc)
	 * @see me.factory.TransitedAnotationConfig#sessionBuilder()
	 */
	@Override
	protected LocalSessionFactoryBuilder sessionBuilder() {
		LocalSessionFactoryBuilder local = super.sessionBuilder();
	
		local.addAnnotatedClasses(Crime.class);
		local.addAnnotatedClasses(Address.class);
		local.addAnnotatedClasses(URCCatagories.class);	    
		return local;
	}
		
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public AddressDao addressDao() throws ClassNotFoundException, SQLException {
		return new AddressDaoImpl(connection());
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public CrimeDao crimeDao() throws ClassNotFoundException, SQLException {
		return new CrimeDaoImpl(connection());
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public URCCatagoriesDAO uRCCatagoriesDAO() throws ClassNotFoundException, SQLException {
		return new URCCatagoriesDAOImpl(connection());
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public IDensityKernel kernel() {
		return new Epanechnikov();
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public IBandwidth bandWidth() {
		return new SlivermanRule();
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public AbstractDensityEstimateAlgorithm  densityEstimateAlgorithm() {
		return new NonAdaptiveKDE( kernel(), bandWidth(), bandWidth());
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public ClusteringAlgorithm  clusteringAlgorithm() {
		return new STING( 1000000, 0, 0.5, 1.0);
	}
	
	
}
