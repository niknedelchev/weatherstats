package com.group9.weatherstats.service;

import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group9.weatherstats.dto.TimelineDTO;
import com.group9.weatherstats.model.Timeline;
import com.group9.weatherstats.repository.TimelineRepository;

@Service
public class TimelineService {

	@Autowired
	TimelineRepository timelineRepository;

	public List<Timeline> findAll() { return timelineRepository.findAll();}

	
	public void save(Timeline timeline) { timelineRepository.save(timeline);}
	
	
	public void save(TimelineDTO timeline) { 
		Timeline timelineInDB = new Timeline(timeline.getStation(),
											timeline.getPeriod().atEndOfMonth(),
											timeline.getAvgTemperature(),
											timeline.getAvgSnow(),
											timeline.getTotRain(),
											timeline.getTotSunshine(),
											timeline.getExtrMaxTemp(),
											timeline.getExtrMinTemp(),
											timeline.getExtrAvgTemp()
											);
		
		timelineRepository.save(timelineInDB);
		}

	public Timeline findById(int id) {
		Timeline timeline = timelineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid timeline Id:" + id));
		return timeline;
	}

	public TimelineDTO findByIdDTO(int id) {
		Timeline timeline = timelineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid timeline Id:" + id));
		TimelineDTO timelineDTO = new TimelineDTO(timeline.getId(),
													timeline.getStation(), 
													YearMonth.from(timeline.getPeriod()),
													timeline.getAvgTemperature(),
													timeline.getAvgSnow(),
													timeline.getTotRain(),
													timeline.getTotSunshine(),
													timeline.getExtrMaxTemp(),
													timeline.getExtrMinTemp(),
													timeline.getExtrAvgTemp()
													);
		
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
	
	
	
}
