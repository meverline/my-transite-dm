package me.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.crime.dao.AddressDao;
import me.crime.dao.CrimeDao;
import me.crime.dao.URCCatagoriesDAO;
import me.crime.loader.LoadURCCatagories;
import me.crime.loader.ParseCrimeXml;

/**
 * 
 * @author markeverline
 *
 */
@Configuration
public class AppConfig {

	@Bean(value="loadURCCatagories")
	public LoadURCCatagories loadURCCatagories(URCCatagoriesDAO urcCatagoriesDAO) {
		return new LoadURCCatagories(urcCatagoriesDAO);
	}
	
	@Bean(value="parseCrimeXml")
	public ParseCrimeXml parseCrimeXml(AddressDao addressDao, CrimeDao crimeDao, URCCatagoriesDAO urcCatagoriesDAO) {
		return new ParseCrimeXml(addressDao, crimeDao, urcCatagoriesDAO);
	}
		
}
