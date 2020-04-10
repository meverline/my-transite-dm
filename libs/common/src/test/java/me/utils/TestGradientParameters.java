package me.utils;

import java.awt.Color;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

public class TestGradientParameters {

	@Test
	public void test() {
		BeanTester tester = new BeanTester();
		
		Configuration configuration = new ConfigurationBuilder().
						overrideFactory("endColor", new ColorFactory()).
						overrideFactory("startColor", new ColorFactory()).build();
		
		tester.testBean(GradientParameters.class, configuration);
				
		GradientParameters obj = new GradientParameters("Black", "Red", 10, 35, 20, 85);
		obj = new GradientParameters(Color.BLACK, Color.RED, 10, 35, 20, 85);
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for ( int ndx = 0; ndx < 100; ndx++) {
			stats.addValue(Math.random());
		}
		
		obj.createColorGradient(stats);
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	
	class ColorFactory implements Factory<Color> {
		
        @Override
        public Color create() {
        	    return Color.BLACK;
        }
    }


}
