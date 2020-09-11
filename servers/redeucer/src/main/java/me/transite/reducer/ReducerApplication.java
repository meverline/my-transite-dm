package me.transite.reducer;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ReducerApplication implements ApplicationContextAware, CommandLineRunner {

        private ApplicationContext applicationContext;

        public static void main(String[] args) {
            System.setProperty("spring.config.name", "computeServer");
            SpringApplication.run(ReducerApplication.class, args);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        public ApplicationContext getContext() {
            return applicationContext;
        }

        @Override
        public void run(String... args) throws Exception {

        }
    }

