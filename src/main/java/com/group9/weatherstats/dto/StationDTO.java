package com.group9.weatherstats.dto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;


import org.springframework.format.annotation.DateTimeFormat;



public class StationDTO {
	
	private int id;
    private String name;
    private YearMonth validFrom;
    private YearMonth validTo;
    
    public StationDTO() {}
    
	public StationDTO(String name, YearMonth validFrom, YearMonth validTo) {
		this.id = 0; // default
		this.name = name;
		this.validFrom = validFrom;
		this.validTo = validTo;
	}
    
	public StationDTO(int id,String name, YearMonth validFrom, YearMonth validTo) {
		this.id = id;
		this.name = name;
		this.validFrom = validFrom;
		this.validTo = validTo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public YearMonth getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(YearMonth validFrom) {
		this.validFrom = validFrom;
	}

	public YearMonth getValidTo() {
		return validTo;
	}

	public void setValidTo(YearMonth validTo) {
		this.validTo = validTo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    
}
