package com.group9.weatherstats.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.group9.weatherstats.dto.StationDTO;
import com.group9.weatherstats.model.Station;
import com.group9.weatherstats.service.StationService;


@Controller
public class StationController {
	
	@Autowired
	StationService stationService;
	
	@GetMapping(path = "/stations")
	public String showStationsPage(Model model) {
		List<Station> stations = stationService.findAll();
		model.addAttribute("stations",stations);
		return "/stations/stations";
	}
	
	@GetMapping(path = "/stations/add")
	public String showAddStationsPage(Model model) {
		model.addAttribute("station", new StationDTO());
		return "stations/stations-add";
	}
	
	@PostMapping(path = "/stations/add")
	public String addServiceCompany(@ModelAttribute StationDTO stationDTO) {
		stationService.save(stationDTO);
		return "redirect:/stations";
	}
	
	@GetMapping("/stations/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
	    StationDTO stationDTO = stationService.findByIdDTO(id);
	    model.addAttribute("station", stationDTO);
	    return "stations/stations-edit";
	}
	
	@PostMapping("/stations/edit/{id}")
	public String updateStation(@ModelAttribute StationDTO stationDTO) throws Exception {
		stationService.updateStation(stationDTO);
	    return "redirect:/stations";
	}
	    
	@GetMapping("/stations/delete/{id}")
	public String deleteStation(@PathVariable("id") int id, Model model) {
		Station station = stationService.findById(id);
		stationService.delete(station);
	    return "redirect:/stations";
	}
	
}
