package me.transit.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;

import me.transit.parser.service.AbstractGTFSParser;

@SpringBootApplication
@ComponentScan({ "me.transit"})
@EnableAutoConfiguration
public class TransitFeedParser implements ApplicationContextAware, CommandLineRunner {

	private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    } 

    public ApplicationContext getContext() {
        return applicationContext;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(TransitFeedParser.class, args);
    }
  
	/**
	 * @param args
	 */
	public void run(String... args) {
		Log log = LogFactory.getLog(TransitFeedParser.class.getName());		
		AbstractGTFSParser parser = null;
		
		try {
			if (args.length > 0 &&  args[0].equals("service")) {
				parser = (AbstractGTFSParser) getContext().getBean("parserService");
			} else {
				parser = (AbstractGTFSParser) getContext().getBean("localParser");
			}
		
			parser.start();
			
		} catch ( Exception ex ) {
			log.error(ex.getLocalizedMessage(), ex);
		}

	}

}
