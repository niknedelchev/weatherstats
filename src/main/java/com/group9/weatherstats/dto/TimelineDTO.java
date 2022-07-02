package com.group9.weatherstats.dto;

import java.time.YearMonth;

import com.group9.weatherstats.model.Station;

public class TimelineDTO {
    private int id;
	private Station station;
    private YearMonth period;
	private float avgTemperature; 
	private float avgSnow; 
	private float totRain; 
	private float totSunshine; 
	private float extrMaxTemp;
	private float extrMinTemp;
	private float extrAvgTemp;
	
	public TimelineDTO() {}
	
	public TimelineDTO(int id, Station station, YearMonth period, float avgTemperature, float avgSnow, float totRain,
			float totSunshine, float extrMaxTemp, float extrMinTemp, float extrAvgTemp) {
		this.id = id;
		this.station = station;
		this.period = period;
		this.avgTemperature = avgTemperature;
		this.avgSnow = avgSnow;
		this.totRain = totRain;
		this.totSunshine = totSunshine;
		this.extrMaxTemp = extrMaxTemp;
		this.extrMinTemp = extrMinTemp;
		this.extrAvgTemp = extrAvgTemp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public YearMonth getPeriod() {
		return period;
	}

	public void setPeriod(YearMonth period) {
		this.period = period;
	}

	public float getAvgTemperature() {
		return avgTemperature;
	}

	public void setAvgTemperature(float avgTemperature) {
		this.avgTemperature = avgTemperature;
	}

	public float getAvgSnow() {
		return avgSnow;
	}

	public void setAvgSnow(float avgSnow) {
		this.avgSnow = avgSnow;
	}

	public float getTotRain() {
		return totRain;
	}

	public void setTotRain(float totRain) {
		this.totRain = totRain;
	}

	public float getTotSunshine() {
		return totSunshine;
	}

	public void setTotSunshine(float totSunshine) {
		this.totSunshine = totSunshine;
	}

	public float getExtrMaxTemp() {
		return extrMaxTemp;
	}

	public void setExtrMaxTemp(float extrMaxTemp) {
		this.extrMaxTemp = extrMaxTemp;
	}

	public float getExtrMinTemp() {
		return extrMinTemp;
	}

	public void setExtrMinTemp(float extrMinTemp) {
		this.extrMinTemp = extrMinTemp;
	}

	public float getExtrAvgTemp() {
		return extrAvgTemp;
	}

	public void setExtrAvgTemp(float extrAvgTemp) {
		this.extrAvgTemp = extrAvgTemp;
	}
	


}
