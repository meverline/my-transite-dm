package me.transit.parser;

import me.transit.parser.service.AbstractGTFSParser;
import me.transit.parser.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import javax.annotation.PreDestroy;

public class FeedParserListener implements ApplicationListener<ApplicationReadyEvent> {


    private final AbstractGTFSParser registry;
    private final boolean isService;

    public FeedParserListener(AbstractGTFSParser parserService, boolean isService) {
        this.registry = parserService;
        this.isService = isService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if ( this.isService ) {
            registry.start();
        }
    }

    @PreDestroy
    public void destroy() {
        if ( this.isService ) {
            registry.stop();
        }
    }

}