package me.transit.database.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import me.transit.database.Agency;

import org.junit.Test;

public class AgencyImpleTest {

	@Test
	public void constructor() {
		
		Agency obj = new AgencyImpl();
		
		assertNotNull(obj);
		
		assertEquals("", obj.getName());
		assertEquals("", obj.getLang());
		assertEquals("", obj.getPhone());
		assertEquals("", obj.getTimezone());
		assertEquals("", obj.getUrl());
		assertEquals("", obj.getId());
		assertEquals(-1, obj.getUUID());
		
		assertEquals("0.5", obj.getVersion());
		
		obj = new AgencyImpl("name");
		
		assertNotNull(obj);
		
		assertEquals("name", obj.getName());
		assertEquals("", obj.getLang());
		assertEquals("", obj.getPhone());
		assertEquals("", obj.getTimezone());
		assertEquals("", obj.getUrl());
		assertEquals("", obj.getId());
		assertEquals(-1, obj.getUUID());
		
		assertEquals("0.5", obj.getVersion());
		
	}
	
	@Test
	public void getsAndSets() {
		
		Agency obj = new AgencyImpl("name");
		
		assertNotNull(obj);
		
		assertEquals("name", obj.getName());
		assertEquals("", obj.getLang());
		assertEquals("", obj.getPhone());
		assertEquals("", obj.getTimezone());
		assertEquals("", obj.getUrl());
		assertEquals("", obj.getId());
		assertEquals(-1, obj.getUUID());
		
		assertEquals("0.5", obj.getVersion());
		
		obj.setName("name2");
		obj.setLang("Lang");
		obj.setPhone("000-111-2222");
		obj.setTimezone("DMT");
		obj.setUrl("URL");
		obj.setId("id");
		obj.setUUID(10);
		obj.setVersion("1.0");
		
		assertEquals("name2", obj.getName());
		assertEquals("Lang", obj.getLang());
		assertEquals("000-111-2222", obj.getPhone());
		assertEquals("DMT", obj.getTimezone());
		assertEquals("URL", obj.getUrl());
		assertEquals("id", obj.getId());
		assertEquals(10, obj.getUUID());
		
		assertEquals("1.0", obj.getVersion());
		
	}
	
	

}
