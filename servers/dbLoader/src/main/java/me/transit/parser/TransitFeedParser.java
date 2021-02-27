package me.transit.parser;

import lombok.extern.apachecommons.CommonsLog;
import me.transit.parser.service.AbstractGTFSParser;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "me.transit"})
@CommonsLog
public class  TransitFeedParser implements ApplicationContextAware, CommandLineRunner {

	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "transiteLoader");
		SpringApplication.run(TransitFeedParser.class, args);
	}

	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
		return applicationContext;
    }

	/**
	 * @param args
	 */
	public void run(String... args) {
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
