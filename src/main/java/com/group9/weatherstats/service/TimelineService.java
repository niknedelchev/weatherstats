package com.group9.weatherstats.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.group9.weatherstats.dto.TimelineCSVR;
import com.group9.weatherstats.dto.TimelineDTO;
import com.group9.weatherstats.dto.TimelineWeightedAvgDTO;
import com.group9.weatherstats.model.Station;
import com.group9.weatherstats.model.Timeline;
import com.group9.weatherstats.repository.TimelineRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@Service
public class TimelineService {

	@Autowired
	TimelineRepository timelineRepository;
	@Autowired
	StationService stationService;

	public List<Timeline> findAll() {
		return timelineRepository.findAll();
	}

	public List<Timeline> findByPeriodBetween(LocalDate from, LocalDate to) {
		return this.timelineRepository.findByPeriodBetween(from, to);
	}

	public void save(Timeline timeline) {
		timelineRepository.save(timeline);
	}

	public void save(TimelineDTO timeline) {
		Timeline timelineInDB = new Timeline(timeline.getStation(), timeline.getPeriod().atEndOfMonth(),
				timeline.getAvgTemperature(), timeline.getAvgSnow(), timeline.getTotRain(), timeline.getTotSunshine(),
				timeline.getExtrMaxTemp(), timeline.getExtrMinTemp(), timeline.getExtrAvgTemp());

		timelineRepository.save(timelineInDB);
	}

	public Timeline findById(int id) {
		Timeline timeline = timelineRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid timeline Id:" + id));
		return timeline;
	}

	public TimelineDTO findByIdDTO(int id) {
		Timeline timeline = timelineRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid timeline Id:" + id));
		TimelineDTO timelineDTO = new TimelineDTO(timeline.getId(), timeline.getStation(),
				YearMonth.from(timeline.getPeriod()), timeline.getAvgTemperature(), timeline.getAvgSnow(),
				timeline.getTotRain(), timeline.getTotSunshine(), timeline.getExtrMaxTemp(), timeline.getExtrMinTemp(),
				timeline.getExtrAvgTemp());

		return timelineDTO;
	}

	public void updateTimeline(Timeline timeline) throws Exception {

		Timeline timelineInDB = this.findById(timeline.getId());
		if (timelineInDB != null) {
			timelineInDB.setPeriod(timeline.getPeriod());
			timelineInDB.setStation(timeline.getStation());
			timelineInDB.setAvgTemperature(timeline.getAvgTemperature());
			timelineInDB.setExtrAvgTemp(timeline.getExtrAvgTemp());
			timelineInDB.setExtrMaxTemp(timeline.getExtrMaxTemp());
			timelineInDB.setExtrMinTemp(timeline.getExtrMinTemp());
			timelineInDB.setTotRain(timeline.getTotRain());
			timelineInDB.setAvgSnow(timeline.getAvgSnow());
			timelineInDB.setTotSunshine(timeline.getTotSunshine());

			this.save(timelineInDB);
		} else {
			throw new Exception("Timeline not found");
		}

	}

	public void updateTimeline(TimelineDTO timeline) throws Exception {
		Timeline timelineInDB = this.findById(timeline.getId());
		if (timelineInDB != null) {
			timelineInDB.setPeriod(timeline.getPeriod().atEndOfMonth());
			timelineInDB.setStation(timeline.getStation());
			timelineInDB.setAvgTemperature(timeline.getAvgTemperature());
			timelineInDB.setExtrAvgTemp(timeline.getExtrAvgTemp());
			timelineInDB.setExtrMaxTemp(timeline.getExtrMaxTemp());
			timelineInDB.setExtrMinTemp(timeline.getExtrMinTemp());
			timelineInDB.setTotRain(timeline.getTotRain());
			timelineInDB.setAvgSnow(timeline.getAvgSnow());
			timelineInDB.setTotSunshine(timeline.getTotSunshine());

			this.save(timelineInDB);
		} else {
			throw new Exception("Timeline not found");
		}

	}

	public void delete(Timeline timeline) {
		timelineRepository.delete(timeline);
	}

	public void deleteAll() {
		timelineRepository.deleteAll();
	}

	public void readTimelinesFromCSV(MultipartFile file) throws IllegalStateException, IOException {

		Reader reader = new InputStreamReader(file.getInputStream());
		List<TimelineCSVR> timelinesCSVR = new CsvToBeanBuilder(reader).withType(TimelineCSVR.class).build().parse();

		if (!timelinesCSVR.isEmpty()) {
			List<Station> stations = stationService.findAll();
			List<Timeline> timelines = new ArrayList<Timeline>();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			for (TimelineCSVR timelineCSVR : timelinesCSVR) {
				Station station = stations.stream().filter(s -> s.getId() == timelineCSVR.getStation()).findFirst()
						.get();
				Timeline timeline = new Timeline();
				timeline.setStation(station);
				timeline.setPeriod(LocalDate.parse(timelineCSVR.getPeriod(), formatter));
				timeline.setAvgSnow(timelineCSVR.getAvgSnow());
				timeline.setAvgTemperature(timelineCSVR.getAvgTemperature());
				timeline.setTotRain(timelineCSVR.getTotRain());
				timeline.setTotSunshine(timelineCSVR.getTotSunshine());
				timeline.setExtrMaxTemp(timelineCSVR.getExtrMaxTemp());
				timeline.setExtrMinTemp(timelineCSVR.getExtrMinTemp());
				timeline.setExtrAvgTemp(timelineCSVR.getExtrAvgTemp());
				timelines.add(timeline);
			}

			this.timelineRepository.saveAll(timelines);
		}
	}

	public List<TimelineWeightedAvgDTO> getTimelineWAvgDTOs(LocalDate startDate, LocalDate endDate) {
		List<Timeline> timelines = this.timelineRepository.findByPeriodBetween(startDate, endDate);

		// get all unique periods
		List<LocalDate> periods = timelines.stream().map(t -> t.getPeriod()).distinct().collect(Collectors.toList());

		// get all unique stations in selected period 
		List<Station> allStationsInPeriod = timelines.stream().map(t -> t.getStation()).distinct()
				.collect(Collectors.toList());
		
		Map<Station, Float> stationScaledWeights = stationService.getRecalculatedWeightsByStations(allStationsInPeriod);

		List<TimelineWeightedAvgDTO> timelinesWAvgDTOs = new ArrayList<TimelineWeightedAvgDTO>();
		
		for (LocalDate period : periods) {
			float avgTemperature = 0;
			float avgSnow = 0;
			float totRain = 0;
			float totSunshine = 0;
			float extrMaxTemp = 0;
			float extrMinTemp = 0;
			float extrAvgTemp = 0;

			for (Timeline timeline : timelines) {
				if (timeline.getPeriod().equals(period)) {

					float stationWeight = stationScaledWeights.get(timeline.getStation());

					avgTemperature += (timeline.getAvgTemperature() * stationWeight);
					avgSnow += (timeline.getAvgSnow() * stationWeight);
					totRain += (timeline.getTotRain() * stationWeight);
					totSunshine += (timeline.getTotSunshine() * stationWeight);
					extrMaxTemp += (timeline.getExtrMaxTemp() * stationWeight);
					extrMinTemp += (timeline.getExtrMinTemp() * stationWeight);
					extrAvgTemp += (timeline.getExtrAvgTemp() * stationWeight);
				}

			}
			
			TimelineWeightedAvgDTO timelinesWAvgDTO = new TimelineWeightedAvgDTO();

			timelinesWAvgDTO.setPeriod(period);
			timelinesWAvgDTO.setAvgTemperature(avgTemperature);
			timelinesWAvgDTO.setAvgSnow(avgSnow);
			timelinesWAvgDTO.setTotRain(totRain);
			timelinesWAvgDTO.setTotSunshine(totSunshine);
			timelinesWAvgDTO.setExtrMaxTemp(extrMaxTemp);
			timelinesWAvgDTO.setExtrMinTemp(extrMinTemp);
			timelinesWAvgDTO.setExtrAvgTemp(extrAvgTemp);

			timelinesWAvgDTOs.add(timelinesWAvgDTO);

		}
		
		return timelinesWAvgDTOs;
	}

}
