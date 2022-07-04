package com.group9.weatherstats.dto;

import java.time.LocalDate;

public class TimelineWeightedAvgDTO {
	
    private LocalDate period;
	private float avgTemperature; 
	private float avgSnow; 
	private float totRain; 
	private float totSunshine; 
	private float extrMaxTemp;
	private float extrMinTemp;
	private float extrAvgTemp;
	
	public TimelineWeightedAvgDTO() {}
	
	public TimelineWeightedAvgDTO(LocalDate period, float avgTemperature, float avgSnow, float totRain,
			float totSunshine, float extrMaxTemp, float extrMinTemp, float extrAvgTemp) {
		this.period = period;
		this.avgTemperature = avgTemperature;
		this.avgSnow = avgSnow;
		this.totRain = totRain;
		this.totSunshine = totSunshine;
		this.extrMaxTemp = extrMaxTemp;
		this.extrMinTemp = extrMinTemp;
		this.extrAvgTemp = extrAvgTemp;
	}

	public LocalDate getPeriod() {
		return period;
	}

	public void setPeriod(LocalDate period) {
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
