package com.group9.weatherstats.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.group9.weatherstats.model.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline, Integer> {
	
    @Query("select t from Timeline t where t.period >= :from and t.period <= :to ")
    List<Timeline> findByPeriodBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

}
