package com.group9.weatherstats.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.group9.weatherstats.dto.TimelineDTO;
import com.group9.weatherstats.model.Station;
import com.group9.weatherstats.model.Timeline;
import com.group9.weatherstats.service.StationService;
import com.group9.weatherstats.service.TimelineService;
import com.group9.weatherstats.service.WebScrapingService;

@Controller
public class TimelineController {

	@Autowired
	TimelineService timelineService;
	@Autowired
	StationService stationService;
	@Autowired
	WebScrapingService webScrapingService;
	
	@GetMapping(path = "/timeline")
	public String showStationsPage(Model model) {
		List<Timeline> timelines = timelineService.findAll();
		model.addAttribute("timelines",timelines);
		return "/timeline/timeline";
	}
	
	@GetMapping(path = "/timeline/add")
	public String showAddTimelinePage(Model model) {
		model.addAttribute("timeline", new TimelineDTO());
		
		List<Station> stations = stationService.findAll();
		model.addAttribute("stations", stations);
			
		return "timeline/timeline-add";
	}
	
	@PostMapping(path = "/timeline/add")
	public String addServiceCompany(@ModelAttribute TimelineDTO timelineDTO) {
		timelineService.save(timelineDTO);
		return "redirect:/timeline";
	}
	
	@GetMapping("/timeline/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
	    TimelineDTO timelineDTO = timelineService.findByIdDTO(id);

		List<Station> stations = stationService.findAll();
		model.addAttribute("stations", stations);

		model.addAttribute("timeline", timelineDTO);
	    return "timeline/timeline-edit";
	}
	
	@PostMapping("/timeline/edit/{id}")
	public String updateTimeline(@ModelAttribute TimelineDTO timelineDTO) throws Exception {
		timelineService.updateTimeline(timelineDTO);
	    return "redirect:/timeline";
	}
	    
	@GetMapping("/timeline/delete/{id}")
	public String deleteStation(@PathVariable("id") int id, Model model) {
		Timeline timeline = timelineService.findById(id);
		timelineService.delete(timeline);
	    return "redirect:/timeline";
	}
	
	@GetMapping(path = "/do-scraping")
	public String doScraping() {
		webScrapingService.ScrapeTimelines(stationService, timelineService);
		return "redirect:/timeline";
	}

	
}
