package org.dm.transit.controller;

import java.util.Calendar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
	
	@GetMapping(value = "/ping")
	public String ping() {
		Calendar date = Calendar.getInstance();
		
		return date.getTime().toString();
	}

}
