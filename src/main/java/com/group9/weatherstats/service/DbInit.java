package com.group9.weatherstats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DbInit implements CommandLineRunner {
	@Autowired
	StationService stationService;
	
	@Override
	public void run(String... args) throws Exception {
		stationService.addAllStations();
		
	}

}
