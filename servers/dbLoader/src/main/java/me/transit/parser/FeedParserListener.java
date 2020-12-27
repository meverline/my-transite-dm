package me.transit.parser;

import me.transit.parser.service.AbstractGTFSParser;
import me.transit.parser.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import javax.annotation.PreDestroy;

public class FeedParserListener implements ApplicationListener<ApplicationReadyEvent> {


    private final AbstractGTFSParser registry;

    @Autowired
    public FeedParserListener(AbstractGTFSParser parserService) {
        this.registry = parserService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        registry.start();
    }

    @PreDestroy
    public void destroy() {
        registry.stop();
    }

}