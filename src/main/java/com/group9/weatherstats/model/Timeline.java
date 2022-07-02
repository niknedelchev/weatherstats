package com.group9.weatherstats.model;

import java.time.LocalDate;
import java.time.YearMonth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "timelines")
public class Timeline {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@ManyToOne
	@JoinColumn(name = "station_id", nullable = false)
	private Station station;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "period",nullable = false)
    private LocalDate period;
	@Column(columnDefinition="Decimal(10,2) default '00.00'")
	private float avgTemperature; 
	@Column(columnDefinition="Decimal(10,2) default '00.00'")
	private float avgSnow; 
	@Column(columnDefinition="Decimal(10,2) default '00.00'")
	private float totRain; 
	@Column(columnDefinition="Decimal(10,2) default '00.00'")
	private float totSunshine; 
	@Column(columnDefinition="Decimal(10,2) default '00.00'")
	private float extrMaxTemp;
	@Column(columnDefinition="Decimal(10,2) default '00.00'")
	private float extrMinTemp;
	@Column(columnDefinition="Decimal(10,2) default '00.00'")
	private float extrAvgTemp;
	
	
	public Timeline() {	}


	public Timeline(Station station, LocalDate period, float avgTemperature, float avgSnow, float totRain,
			float totSunshine, float extrMaxTemp, float extrMinTemp, float extrAvgTemp) {
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
