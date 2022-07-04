package com.group9.weatherstats.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "stations")
public class Station {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "station")
	List<Timeline> timelines;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "valid_from", nullable = false)
	private LocalDate validFrom;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "valid_to", nullable = false)
	private LocalDate validTo;

	@Column(columnDefinition = "Decimal(5,4) default '0.0000'")
	float baseWeight;

	public Station() {
	}

	public Station(String name, List<Timeline> timelines, LocalDate validFrom, LocalDate validTo) {
		this.name = name;
		this.timelines = timelines;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.baseWeight = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Timeline> getTimelines() {
		return timelines;
	}

	public void setTimelines(List<Timeline> timelines) {
		this.timelines = timelines;
	}

	public LocalDate getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public LocalDate getValidTo() {
		return validTo;
	}

	public void setValidTo(LocalDate validTo) {
		this.validTo = validTo;
	}
	
	
	public float getBaseWeight() {
		return baseWeight;
	}

	public void setBaseWeight(float baseWeight) {
		this.baseWeight = baseWeight;
	}

	@Override
	public String toString() {
		return "" + id;
	}

}
