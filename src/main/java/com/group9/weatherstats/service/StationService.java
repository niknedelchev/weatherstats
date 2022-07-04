package com.group9.weatherstats.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group9.weatherstats.dto.StationDTO;
import com.group9.weatherstats.model.Station;
import com.group9.weatherstats.repository.StationRepository;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

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
										"Елхово", "(Козичино)", "Бургас",
										"Ахтопол", "Рожен", "Свиленград",
										"Димитровгр. (С.)", "Кълъраш (Р.)", "Турну Мъгуреле (Р.)",
										"(Гложене)", "(Рилци)", "Одрин (Т.)",
										"Елена", "(Търговище)", "(Кнежа)",
										"(Варна-Акчелар)","(Варна-Боровец)",
										"(Климентово)", "(Дупница)","(Орландовци)",
										"(Бояна)","(Княжево)","Панагюрище", 
										"Ямбол","Стралджа"
									).collect(Collectors.toList());
			
		
		for (String stationName : stationNames) {
			this.save(new Station(stationName, null, startDate, endDate));
		}
	}

	public void writeAllStationsToCSV(List<Station> stations) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		  String path = "D:\\Documents\\NBU\\3rd_year\\6th_semester\\Practice_programming_db\\stations.csv";
		  FileOutputStream fos =  new FileOutputStream(path);
		  Writer writer = new OutputStreamWriter(fos, Charset.forName("UTF8"));
		  
		  StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
		  beanToCsv.write(stations);
		  writer.close();
	}

	public boolean checkValidityAndUpdateBaseWeights(Map<String, String> allRequestParams) {
		float sum =  allRequestParams.values().stream()
					.map( e -> WebScrapingService.tryParseFloat(e))
					.reduce(Float::sum).get();
		
		if (Math.abs(sum-1)<0.00001) {
			for (String key : allRequestParams.keySet()) {
				int id = Integer.parseInt(key);
				Station station = this.stationRepository.findById(id).get();
				float weight = WebScrapingService.tryParseFloat(allRequestParams.get(key));
				station.setBaseWeight(weight);
				this.stationRepository.save(station);
			}
			return true;
		}
		else {
			return false;
		}
	}
	

	public Map<Station, Float> getRecalculatedWeightsByStationIDs(List<Integer> ids) {
		List<Station> stations = this.stationRepository.findAllById(ids);
		
		float totalSumBaseWeights = stations.stream()
											.map(s->s.getBaseWeight())
											.reduce(Float::sum)
											.get();
		
		Map<Station, Float> recalculatedWeightsMap = new HashMap<Station, Float>();
		for (Station station : stations) {
			recalculatedWeightsMap.put(station, station.getBaseWeight()/totalSumBaseWeights);
		}
		
		return recalculatedWeightsMap;
	}
	
	public Map<Station, Float> getRecalculatedWeightsByStations(List<Station> stations) {
		
		float totalSumBaseWeights = stations.stream()
											.map(s->s.getBaseWeight())
											.reduce(Float::sum)
											.get();
		
		Map<Station, Float> recalculatedWeightsMap = new HashMap<Station, Float>();
		for (Station station : stations) {
			recalculatedWeightsMap.put(station, station.getBaseWeight()/totalSumBaseWeights);
		}
		
		return recalculatedWeightsMap;
	}


}