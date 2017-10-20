package me.utils;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Calendar;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import org.meanbean.test.BeanTester;import org.meanbean.test.BeanTester;

public class TestGradientParameters {

	@Test
	public void test() {
		BeanTester tester = new BeanTester();
		
		Configuration configuration = new ConfigurationBuilder().
						overrideFactory("endColor", new ColorFactory()).
						overrideFactory("startColor", new ColorFactory()).build();
		
		tester.testBean(GradientParameters.class, configuration);
		
		GradientParameters obj = new GradientParameters(Color.BLACK, Color.RED, 10, 35, 20, 85);
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	
	class ColorFactory implements Factory<Color> {
		
        @Override
        public Color create() {
        	    return Color.BLACK;
        }
    }


}
