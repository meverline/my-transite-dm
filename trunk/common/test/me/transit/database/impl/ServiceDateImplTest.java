package me.transit.database.impl;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Map;

import me.transit.database.Agency;
import me.transit.database.ServiceDate;

import org.junit.Test;

public class ServiceDateImplTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		ServiceDate service = new ServiceDateImpl();
		
		assertNull(service.getAgency());
		assertNull(service.getId());
		assertEquals(-1, service.getUUID());
		assertEquals("0.5", service.getVersion());
		assertEquals(0, service.getServiceDayFlag());
		assertEquals(ServiceDate.ServiceDays.ALL_WEEK, service.getService());
		assertNull(service.getEndDate());
		assertNull(service.getStartDate());
		
	}
	
	/**
	 * 
	 */
	public static ServiceDate createServiceDate()
	{
		Agency agency = new AgencyImpl("name");
		ServiceDate service = new ServiceDateImpl();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		service.setAgency(agency);
		service.setId("Id");
		service.setUUID(10);
		service.setVersion("1.0");
		service.setEndDate(end);
		service.setStartDate(start);
		service.setService(ServiceDate.ServiceDays.WEEKDAY_SAT_SERVICE);
		
		service.setServiceDayFlag( ServiceDate.WeekDay.MONDAY.getBit() | ServiceDate.WeekDay.SATURDAY.getBit());	
		return service;
	}

	/**
	 * 
	 */
	@Test
	public void testGetAndSet() {
		Agency agency = new AgencyImpl("name");
		ServiceDate service = new ServiceDateImpl();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		service.setAgency(agency);
		service.setId("Id");
		service.setUUID(10);
		service.setVersion("1.0");
		service.setEndDate(end);
		service.setStartDate(start);
		service.setService(ServiceDate.ServiceDays.WEEKDAY_SAT_SERVICE);
		
		service.setServiceDayFlag( ServiceDate.WeekDay.MONDAY.getBit() | ServiceDate.WeekDay.SATURDAY.getBit());	
		
		assertEquals(agency, service.getAgency());
		assertEquals("Id", service.getId());
		assertEquals(10, service.getUUID());
		assertEquals("1.0", service.getVersion());
		assertEquals(ServiceDate.WeekDay.MONDAY.getBit() | ServiceDate.WeekDay.SATURDAY.getBit(), service.getServiceDayFlag());
		assertEquals(ServiceDate.ServiceDays.WEEKDAY_SAT_SERVICE, service.getService());
		assertEquals(end, service.getEndDate());
		assertEquals(start, service.getStartDate());
		assertEquals(true, service.isMonday());
		assertEquals(true, service.isSaturday());
		
		int value = 0;
		for ( ServiceDate.WeekDay day : ServiceDate.WeekDay.values() ) {
			value = (0 | day.getBit());
			service.setServiceDayFlag(value);
			
			assertEquals(value, service.getServiceDayFlag());
			assertEquals(true, service.hasService(day));
		}
		
		Map<String, Object> map = service.toDocument();
		assertNotNull(map);
		
	}

}
