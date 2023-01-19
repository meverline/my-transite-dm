package me.transit.parser;

import javax.annotation.PreDestroy;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import me.transit.parser.service.AbstractGTFSParser;

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