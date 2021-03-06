package me.transit.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface GTFSFileModel {
	public String filename();
}
