package me.factory;

import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.postgresql.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import me.database.hibernate.HibernateConnection;
import me.database.hibernate.SpringHibernateConnection;
import me.math.grid.tiled.DbTiledSpatialGrid;
import me.math.grid.tiled.TileFragament;
import me.math.grid.tiled.dao.DbTiledSpatialGridDao;
import me.math.grid.tiled.dao.TileFragmentDao;

@Configuration
public class AnotationConfig {
	
	/**
	 * 
	 * @return
	 */
	@Bean 
	public  SimpleDriverDataSource dataSource() {
		
		return new SimpleDriverDataSource( new Driver(),
										   "jdbc:postgresql://localhost:5432/Transit",
										   "postgres",
										   "postgres");
	}
	
	/**
	 * 
	 * @return
	 */
	protected LocalSessionFactoryBuilder sessionBuilder() {
		
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource());
		 
		sessionBuilder.addProperties(getHibernateProperties());
	    
	    sessionBuilder.addAnnotatedClasses(TileFragament.class);
	    sessionBuilder.addAnnotatedClasses(DbTiledSpatialGrid.class);
	    
	    return sessionBuilder;
		
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public SessionFactory sessionFactory() {
	    return sessionBuilder().buildSessionFactory();
	}
	
	/**
	 * 
	 * @return
	 */
	private Properties getHibernateProperties() {
	    Properties properties = new Properties();
	    properties.put("hibernate.show_sql", "false");
	    properties.put("hibernate.dialect", "org.hibernate.spatial.dialect.postgis.PostgisDialect");
	    properties.put("hibernate.hbm2ddl.auto", "create");
	    return properties;
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	protected HibernateConnection connection() {
		return new SpringHibernateConnection(sessionFactory());
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public TileFragmentDao tileFragmentDao() throws ClassNotFoundException, SQLException {
		return new TileFragmentDao(connection());
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public DbTiledSpatialGridDao  dbTiledSpatialGridDao() throws ClassNotFoundException, SQLException {
		return new DbTiledSpatialGridDao(connection());
	}
	
}
