package com.group9.weatherstats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group9.weatherstats.model.Station;

public interface StationRepository extends JpaRepository<Station, Integer> {

}
