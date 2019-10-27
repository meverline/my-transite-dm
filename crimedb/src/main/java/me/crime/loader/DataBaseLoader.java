//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.crime.loader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import me.factory.DaoBeanFactory;

public class DataBaseLoader implements ApplicationContextAware, CommandLineRunner {

	private SAXParser   	   parser_ = null;
	private ApplicationContext applicationContext;
	private ParseCrimeXml      parseCrimeXml;

	protected static Log log_ = LogFactory.getLog(DataBaseLoader.class);
	
	/**
	 * 
	 */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    } 

    /**
     * 
     * @return
     */
    public ApplicationContext getContext() {
        return applicationContext;
    }
    
    /**
     * 
     * @return
     */
    public ParseCrimeXml getParseCrimeXml() {
    	if ( parseCrimeXml == null ) {
    		parseCrimeXml = (ParseCrimeXml) this.getContext().getBean("parseCrimeXml");
    	}
    	return parseCrimeXml;
    }
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DataBaseLoader.class, args);
    }
  
    /**
     * 
     */
	public DataBaseLoader() {
				
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			parser_ = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			DataBaseLoader.log_.error(e);
		} catch (SAXException e) {
			DataBaseLoader.log_.error(e);
		}
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public int parse(String data) {

		try {

			parser_.parse(new InputSource(new FileReader(data)), getParseCrimeXml());

		} catch (SAXException e) {
			DataBaseLoader.log_.error(e,e);
		} catch (IOException e) {
			DataBaseLoader.log_.error(e,e);
		}
		return getParseCrimeXml().numberOfRecoredsSaved();
	}

	/**
	 * 
	 * @param dir
	 * @return
	 */
	public int parseDir(File dir) {

		File files[]  = dir.listFiles();

		int total = 0;
		for ( int ndx = 0; ndx < files.length; ndx++ ) {
		     if ( files[ndx].isFile()  && (! files[ndx].isHidden()) ) {
		    	 DataBaseLoader.log_.info("parsing: " + files[ndx].toString() + "...");
		    	 parse( files[ndx].toString() );
			     total += getParseCrimeXml().numberOfRecoredsSaved();
			     DataBaseLoader.log_.info(files[ndx].toString() + " saved: " + getParseCrimeXml().numberOfRecoredsSaved() + "...");
			     getParseCrimeXml().reset();
		     }
		}
		return total;
	}

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void run(String... args) {

		DaoBeanFactory.initilize();
		DataBaseLoader  loader = new DataBaseLoader();
		
		try {
			LoadURCCatagories loadURCCatagories = (LoadURCCatagories) getContext().getBean("loadURCCatagories");
			loadURCCatagories.loadURCTable();
		} catch (SQLException e) {
			log_.error(e);
		} catch (ClassNotFoundException e) {
			log_.error(e);
		}

		int total = 0;
		for ( String s : args) {
			try {
			   System.out.println("loading " + s + "... ");
			   File fp = new File(s);
			   if ( fp.isDirectory() ) {
				   total = loader.parseDir(fp);
			   } else {
				   total = loader.parse(s);
			   }
			   
			   System.out.println(s + " done, saved:" + total);
			} catch ( Exception ex ) {
				log_.error(ex);
			} catch ( java.lang.Error ex ) {
				log_.error(ex);
			}
		}
	}

}
