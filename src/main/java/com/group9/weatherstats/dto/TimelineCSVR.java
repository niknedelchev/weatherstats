package com.group9.weatherstats.dto;

import java.time.LocalDate;
import java.time.YearMonth;

import com.group9.weatherstats.model.Station;
import com.opencsv.bean.CsvBindByName;

public class TimelineCSVR {
	@CsvBindByName
    private int id;
	@CsvBindByName
	private int station;
	@CsvBindByName
    private String period;
	@CsvBindByName
	private float avgTemperature; 
	@CsvBindByName
	private float avgSnow; 
	@CsvBindByName
	private float totRain; 
	@CsvBindByName
	private float totSunshine; 
	@CsvBindByName
	private float extrMaxTemp;
	@CsvBindByName
	private float extrMinTemp;
	@CsvBindByName
	private float extrAvgTemp;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStation() {
		return station;
	}
	public void setStation(int station) {
		this.station = station;
	}
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
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
