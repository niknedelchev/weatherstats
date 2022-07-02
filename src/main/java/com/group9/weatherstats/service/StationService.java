package com.group9.weatherstats.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group9.weatherstats.dto.StationDTO;
import com.group9.weatherstats.model.Station;
import com.group9.weatherstats.repository.StationRepository;

@Service
public class StationService {

	@Autowired
	StationRepository stationRepository;
	

	public List<Station> findAll(){ return stationRepository.findAll();	}
	
	public void save(Station station) {stationRepository.save(station);}

	public void save(StationDTO stationDTO) {
		Station station = new Station(stationDTO.getName(), null, 
				stationDTO.getValidFrom().atDay(1),
				stationDTO.getValidTo().atEndOfMonth());
		stationRepository.save(station);}
	
	
	public Station findById(int id) {
		Station station = stationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid station Id:" + id));
		return station;
	}

	public StationDTO findByIdDTO(int id) {
		Station station = stationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid station Id:" + id));
		StationDTO stationDTO = new StationDTO(station.getId(), station.getName(), YearMonth.from(station.getValidFrom()),  YearMonth.from(station.getValidTo()));
		
		return stationDTO;
	}

	public void updateStation(Station station) throws Exception {
		Station stationInDB = this.findById(station.getId());
		if (stationInDB != null) {
			stationInDB.setName(station.getName());
			stationInDB.setValidFrom(station.getValidFrom());			
			stationInDB.setValidTo(station.getValidTo());			
			this.save(stationInDB);
		} else {
			throw new Exception("Sation not found");
		}
	}
	
	public void updateStation(StationDTO station) throws Exception {
		Station stationInDB = this.findById(station.getId());
		if (stationInDB != null) {
			stationInDB.setName(station.getName());
			stationInDB.setValidFrom(station.getValidFrom().atDay(1));			
			stationInDB.setValidTo(station.getValidTo().atEndOfMonth());			
			this.save(stationInDB);
		} else {
			throw new Exception("Sation not found");
		}
	}

	public void delete(Station station) {stationRepository.delete(station);}
	
	
	public void addAllStations() {
			LocalDate startDate = LocalDate.of(1999, 1, 1);
			LocalDate endDate = LocalDate.of(2022, 12, 31);
			
			List<String> stationNames = Stream.of(
										"Видин", "Варна", "вр. Мургаш",
										"София", "вр. Мусала", "Сливен",
										"Сандански", "Кърджали", "Ново Село", 
										"Враца", "Лом",	"Оряхово", 
										"Ловеч", "Плевен","В. Търново", 
										"Свищов",	"Русе", "Шумен",
										"Разград", "(Топчии)", "Силистра",
										"(в.з. Осеново)", "(Долище)", "(Левски)",
										"н. Шабла", "н. Калиакра", "Добрич",
										"н. Кюстендил", "Драгоман", "Черни вр.",
										"Пловдив<201000", "Пловдив", "вр. Ботев",
										"Пазарджик", "Чирпан", "(Добри дол)",
										"Елхоово", "(Козичино)", "Бургас",
										"Ахтопол", "Рожен", "Свиленград",
										"Димитровгр. (С.)", "Кълъраш (Р.)", "Турну Мъгуреле (Р.)",
										"(Гложене)", "(Рилци)", "Одрин (Т.)"
									).collect(Collectors.toList());
			
		
		for (String stationName : stationNames) {
			this.save(new Station(stationName, null, startDate, endDate));
		}
	}

}