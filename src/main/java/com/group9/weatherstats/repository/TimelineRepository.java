package com.group9.weatherstats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group9.weatherstats.model.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline, Integer> {

}
