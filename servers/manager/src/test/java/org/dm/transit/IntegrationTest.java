package org.dm.transit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.dm.transit.ComputeManagerApp;
import org.dm.transit.config.EmbeddedMongo;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = ComputeManagerApp.class)
@EmbeddedMongo
public @interface IntegrationTest {
}
