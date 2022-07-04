package com.group9.weatherstats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.group9.weatherstats.model.Role;
import com.group9.weatherstats.model.User;

@Service
public class DbInit implements CommandLineRunner {
	@Autowired
	StationService stationService;
	@Autowired
	private UserService userService;

	
	@Override
	public void run(String... args) throws Exception {
		if(stationService.findAll().isEmpty())
			stationService.addAllStations();
		
		if(userService.findByUsername("admin")==null)
			userService.registerAdmin("Admin", "Adminov", "admin", "admin");
	
	}

}
