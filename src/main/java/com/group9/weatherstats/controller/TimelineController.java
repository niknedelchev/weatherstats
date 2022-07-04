package com.group9.weatherstats.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.group9.weatherstats.dto.TimelineDTO;
import com.group9.weatherstats.dto.TimelineWeightedAvgDTO;
import com.group9.weatherstats.model.Station;
import com.group9.weatherstats.model.Timeline;
import com.group9.weatherstats.service.StationService;
import com.group9.weatherstats.service.TimelineService;
import com.group9.weatherstats.service.WebScrapingService;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@Controller
public class TimelineController {

	@Autowired
	TimelineService timelineService;
	@Autowired
	StationService stationService;
	@Autowired
	WebScrapingService webScrapingService;

	@GetMapping(path = "/timeline")
	public String showStationsPage(Model model, @RequestParam(required = false) String from, @RequestParam(required = false) String to) {
		List<Timeline> timelines; 
		
		if (from != null && to != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
			
			YearMonth startPeriod = YearMonth.parse(from, formatter);
			YearMonth endPeriod = YearMonth.parse(to, formatter);
			
			LocalDate startDate = startPeriod.atDay(1);
			LocalDate endDate = endPeriod.atEndOfMonth();
			
			timelines = timelineService.findByPeriodBetween(startDate, endDate);
		} 
		else 
		{
			timelines = timelineService.findAll();
		}
		
		
		model.addAttribute("timelines", timelines);
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
		webScrapingService.ScrapeTimelines(stationService, timelineService, 2020, 2022, 1, 12);
		return "redirect:/timeline";
	}

	@GetMapping(path = "timeline/scraping")
	public String showScrapingPage() {
		return "timeline/scraping";
	}
	
	//to be finished
	@PostMapping(path = "timeline/scraping")
	public String doScrapingFromTo(@RequestParam String from, @RequestParam String to) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		
		YearMonth startPeriod = YearMonth.parse(from, formatter);
		YearMonth endPeriod = YearMonth.parse(to, formatter);
		
		webScrapingService.ScrapeTimelines(stationService, timelineService, startPeriod.getYear(), endPeriod.getYear(), startPeriod.getMonthValue(), endPeriod.getMonthValue());
		
		return "redirect:/timeline";
	}


	@GetMapping(path = "/timeline/upload-csv")
	public String timlineCSVUploader() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		return "timeline/upload-csv";
	}

	@PostMapping(path = "/timeline/upload-csv")
	public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		timelineService.readTimelinesFromCSV(file);
		return "redirect:/timeline";
	}
	
	
	@GetMapping(path = "/timeline/download-csv")
	public String timlineCSVDownloader() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		return "timeline/download-csv";
	}


	@RequestMapping(value = "/timeline/download-to-csv", method = RequestMethod.GET)
	public ResponseEntity<Object> downloadToCSV()
			throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		List<Timeline> timelines = timelineService.findAll();

		String path = "download.csv";
		File file = new File(path);
		Writer writer = new FileWriter(file);
		StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
		beanToCsv.write(timelines);

		writer.close();

		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", path));// file.getName()
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/txt")).body(resource);

		file.delete();

		return responseEntity;
	}
	
	
	@GetMapping(path = "/timeline/weighted-average")
	public String timelineWeightedAveragePage(Model model, @RequestParam String from, @RequestParam String to) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
	
		YearMonth startPeriod = YearMonth.parse(from, formatter);
		YearMonth endPeriod = YearMonth.parse(to, formatter);
		
		LocalDate startDate = startPeriod.atDay(1);
		LocalDate endDate = endPeriod.atEndOfMonth();
		
		model.addAttribute("startDate", from);
		model.addAttribute("endDate", to);
		
		
		List<TimelineWeightedAvgDTO> timelinesWAvgDTOs = timelineService.getTimelineWAvgDTOs(startDate,endDate);
		model.addAttribute("timelines", timelinesWAvgDTOs);
		
		return "timeline/weighted-average";
	}

	@GetMapping(path = "/timeline/delete-all")
	public String deleteAllTimelines() {
		timelineService.deleteAll();
		return "redirect:/timeline";
	}
	

}
