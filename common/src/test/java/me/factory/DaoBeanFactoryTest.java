package me.factory;

import static org.junit.Assert.*;

import org.junit.Test;

public class DaoBeanFactoryTest {

	@Test
	public void test() {
		try {
			assertNotNull(DaoBeanFactory.initilize());
			assertNotNull(DaoBeanFactory.create());
		} catch (Exception ex) {

		}
	}

}
