package com.group9.weatherstats.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.group9.weatherstats.model.Station;
import com.group9.weatherstats.model.Timeline;
import com.group9.weatherstats.repository.StationRepository;

@Service
public class WebScrapingService {
//	@Autowired
//	StationService stationService;
//	@Autowired
//	TimelineService timelineService;


	public static float tryParseFloat(String s) {
	    try {
	        return Float.parseFloat(s);
	    }
	    catch (NumberFormatException e) {
	        return 0;
	    }
	}
	
    public static Timeline Scraping(Station station, int year, int month) {

        //Варна - станция, за която има данни във всички таблици
        //Station station1 = new Station("Варна", null, 0, 0, 0, 0);

        //Рожен - - станция, за която НЯМА данни във всички таблици
        //Station station1 = new Station("Рожен", null, 0, 0, 0, 0);

        //List<Station> stations;//да се обходят
        //2 for loops
        //periods
        //      stations
        //Извадка->от базата
    	LocalDate period = YearMonth.of(year, month).atEndOfMonth();
    	Timeline timeline =	new Timeline(station,period,0,0,0,0,0,0,0);
        //Timeline timeline = new Timeline(station, month, year, null, null, null, null, null, null, null);
        HtmlPage avgTemperaturePage = null;
        HtmlPage avgSnowPage = null;
        HtmlPage totRainPage = null;
        HtmlPage totSunshinePage = null;
        HtmlPage extremumsPage = null;

        try(WebClient webClient = new WebClient()) {
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            avgTemperaturePage = webClient.getPage("https://www.stringmeteo.com/synop/temp_month.php?month=" + timeline.getPeriod().getMonthValue() + "&year=" + timeline.getPeriod().getYear());
            avgSnowPage = webClient.getPage("https://www.stringmeteo.com/synop/snow_month.php?month=" + timeline.getPeriod().getMonthValue() + "&year=" + timeline.getPeriod().getYear());
            totRainPage = webClient.getPage("https://www.stringmeteo.com/synop/prec_month.php?month=" + timeline.getPeriod().getMonthValue() + "&year=" + timeline.getPeriod().getYear());
            totSunshinePage = webClient.getPage("https://www.stringmeteo.com/synop/sunsh_month.php?month="+ timeline.getPeriod().getMonthValue() + "&year=" + timeline.getPeriod().getYear());
            extremumsPage = webClient.getPage("https://www.stringmeteo.com/synop/maxmin_month.php?month=" + timeline.getPeriod().getMonthValue() + "&year=" + timeline.getPeriod().getYear());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // избираме таблицата за avgTemperature или [Мес. темпер. Т.]
        //final HtmlTable avgTemperatureTable = avgTemperaturePage.querySelector("div[align=\"center\"][valign=\"top\"]:first-child");//не работи
        final HtmlTable avgTemperatureTable = avgTemperaturePage.querySelector("table[cellpadding=\"1\"]");

        //първото дете на div-а с атрибути align="center" и valign="top";
        //може да се селектира и като другите таблици със cellpadding = 1,
        //тъй като само за тази таблица от страницата имаме, че cellpadding = 1
        //(за съжаление няма id или class, по които да се селектира)

        // избираме таблицата за avgSnow или [Мес. сн. покр.]
        final HtmlTable avgSnowTable = avgSnowPage.querySelector("table[cellpadding=\"1\"]");

        // избираме таблицата за totRain или [Мес. валежи]
        final HtmlTable totRainTable = totRainPage.querySelector("table[cellpadding=\"1\"]");

        // избираме таблицата за totSunshine или [Мес. сл. греене]
        final HtmlTable totSunshineTable = totSunshinePage.querySelector("table[cellpadding=\"1\"]");

        // избираме таблицата за екстремумите или [Мес. екстр.] -> min, max, avg
        final HtmlTable extremumsTable = extremumsPage.querySelector("table[cellpadding=\"1\"]");

        //Как да стане скрейпването?
        //Първа идея: да запазваме всеки път цялата таблица в някаква структура от данни, напр. Списък от списъци и после да търсим в тази
        //структура

        //Втора идея: директно по таблицата да откриваме съответните клетки

        //Избираме втората идея.

        //да итерираме и да намерим клетката, в която е стойността на avgTemperature [Мес. темпер. Т.]
        for (final HtmlTableRow row : avgTemperatureTable.getRows()) {
            //System.out.println("Found row");
            for (final HtmlTableCell cell : row.getCells()) {
                //System.out.println("   Found cell: " + cell.asNormalizedText());

                //да намираме на кой ред се намира името на станцията и да взимаме съответната стойност за температурата
                //за td-тата имаме клас .lb -> (името на станцията) и клас .rb -> (съответната температура) -> дали може да ни помогне това?
                //Трябва да видим третата клетка на реда с клас .lb и съседната ѝ вдясно клетка с клас .rb
                //Прескачаме 7 реда.

                //Клетката на станцията съдържа:
                //<td class="lb">
                //	<span class="small">&nbsp;<име на станция></span>
                //</td>

                //Това долу е неработещ псевдокод:
                //if(cell.asNormalizedText() == this.station.getName() && cell.getNextElementSibling().hasAttribute("class") == "rb" ) {
                //if(cell.asNormalizedText() == timeline.getStation().getName() && cell.getNextElementSibling().getAttribute("class") == "rb" ) {

               //System.out.println("timeline.getStation().getName(): "+timeline.getStation().getName());
               //System.out.println("cell.asNormalizedText(): " + cell.asNormalizedText());
               //System.out.println("Are they equal??? " + cell.asNormalizedText().equals(timeline.getStation().getName()) );

                //trim()-ове - премахваме излишните разстояния (Да, на всякъде е добре да ги има)
                if(cell.asNormalizedText().trim().equals(timeline.getStation().getName().trim())) {
                    //Всеки път презаписва стойността и заради това на третия път слага третата стойност, която и търсим
                    timeline.setAvgTemperature(WebScrapingService.tryParseFloat(cell.getNextElementSibling().asNormalizedText().trim()));
                }
            }
        }
        //[Мес. темпер. Т.]/avgTemperature -> https://www.stringmeteo.com/synop/temp_month.php?month=3&year=2000 -> temp_month е разликата

        //да итерираме и да намерим клетката, в която е стойността на avgSnow или [Мес. сн. покр.]
        for (final HtmlTableRow row : avgSnowTable.getRows()) {
            //System.out.println("Found row");
            for (final HtmlTableCell cell : row.getCells()) {
                //System.out.println("   Found cell: " + cell.asNormalizedText());

                //да намираме на кой ред се намира името на станцията и да взимаме съответната стойност за среддното количество сняг
                //за td-тата имаме клас .lb -> (името на станцията) и клас .rb -> (съответната температура) -> дали може да ни помогне това?
                //Трябва да видим третата клетка на реда с клас .lb, като тук след нея
                //имаме три съседни клетки с клас .rb;
                //ние трябва да вземем втората/средната клетка
                //Прескачаме 7 реда. (ако въобще е важно)

                //Клетката на станцията съдържа:
                //<td class="lb">
                //	<span class="small">&nbsp;<име на станция></span>
                //</td>
                //Това долу е неработещ псевдокод:
                //if(cell.asNormalizedText() == this.station.getName() && cell.getNextElementSibling().hasAttribute("class") == "rb" ) {
//                if(cell.asNormalizedText() == timeline.getStation().getName() && cell.getNextElementSibling().getAttribute("class") == "rb" ) {
//                    //if-ът е аналогичен, но взимаме последващата клетка от таблицата
//                    timeline.setAvgSnow(cell.getNextElementSibling().getNextElementSibling().asNormalizedText());
//                }

                if(cell.asNormalizedText().trim().equals(timeline.getStation().getName().trim())) {
                    //Всеки път презаписва стойността и заради това на третия път слага третата стойност, която и търсим
                    timeline.setAvgSnow(WebScrapingService.tryParseFloat(cell.getNextElementSibling().getNextElementSibling().asNormalizedText().trim()));
                }
            }
        }
        //[Мес. сн. покр.]/avgSnow -> https://www.stringmeteo.com/synop/snow_month.php?month=3&year=2000 -> snow е разликата

        //да итерираме и да намерим клетката, в която е стойността на totRain или [Мес. валежи]
        for (final HtmlTableRow row : totRainTable.getRows()) {
            //System.out.println("Found row");
            for (final HtmlTableCell cell : row.getCells()) {
                //System.out.println("   Found cell: " + cell.asNormalizedText());

                //да намираме на кой ред се намира името на станцията и да взимаме съответната стойност за температурата
                //за td-тата имаме клас .lb -> (името на станцията) и клас .rb -> (съответната температура) -> дали може да ни помогне това?
                //Аналогично с avgTemperature - Трябва да видим третата клетка на реда с клас .lb и съседната ѝ вдясно клетка с клас .rb
                //Прескачаме 7 реда.

                //Клетката на станцията съдържа:
                //<td class="lb">
                //	<span class="small">&nbsp;<име на станция></span>
                //</td>
                //Това долу е неработещ псевдокод:
                //if(cell.asNormalizedText() == this.station.getName() && cell.getNextElementSibling().hasAttribute("class") == "rb" ) {
//                if(cell.asNormalizedText() == timeline.getStation().getName() && cell.getNextElementSibling().getAttribute("class") == "rb" ) {
//                    timeline.setTotRain(cell.getNextElementSibling().asNormalizedText());
//                }
                if(cell.asNormalizedText().trim().equals(timeline.getStation().getName().trim())) {
                    //Всеки път презаписва стойността и заради това на третия път слага третата стойност, която и търсим
                    timeline.setTotRain(WebScrapingService.tryParseFloat(cell.getNextElementSibling().asNormalizedText().trim()));
                }
            }
        }
        //[Мес. валежи]/totRain -> https://www.stringmeteo.com/synop/prec_month.php?month=3&year=2000 -> prec_month е разликата

        //да итерираме и да намерим клетката, в която е стойността на totSunshine или [Мес. сл. греене]
        for (final HtmlTableRow row : totSunshineTable.getRows()) {
            //System.out.println("Found row");
            for (final HtmlTableCell cell : row.getCells()) {
                //System.out.println("   Found cell: " + cell.asNormalizedText());

                //да намираме на кой ред се намира името на станцията и да взимаме съответната стойност за температурата
                //за td-тата имаме клас .lb -> (името на станцията) и клас .rb -> (съответната температура) -> дали може да ни помогне това?
                //Аналогично с avgTemperature - Трябва да видим третата клетка на реда с клас .lb и съседната ѝ вдясно клетка с клас .rb
                //Прескачаме 7 реда.

                //Клетката на станцията съдържа:
                //<td class="lb">
                //	<span class="small">&nbsp;<име на станция></span>
                //</td>
                //Това долу е неработещ псевдокод:
                //if(cell.asNormalizedText() == this.station.getName() && cell.getNextElementSibling().hasAttribute("class") == "rb" ) {
//                if(cell.asNormalizedText() == timeline.getStation().getName() && cell.getNextElementSibling().getAttribute("class") == "rb" ) {
//                    timeline.setTotSunshine(cell.getNextElementSibling().asNormalizedText());
//                }
                if(cell.asNormalizedText().trim().equals(timeline.getStation().getName().trim())) {
                    //Всеки път презаписва стойността и заради това на третия път слага третата стойност, която и търсим
                    timeline.setTotSunshine(WebScrapingService.tryParseFloat(cell.getNextElementSibling().asNormalizedText().trim()));
                }
            }
        }
        //[Мес. сл. греене]/totSunshine -> https://www.stringmeteo.com/synop/sunsh_month.php?month=3&year=2000 -> sunsh_month е разликата

        //да итерираме и да намерим клетките, в които са стойностите на екстремумите или [Мес. екстр.] -> min, max, avg
        boolean flag = false;
        //boolean avgFlag = false;
        int counter = 0;
        for (final HtmlTableRow row : extremumsTable.getRows()) {
            //System.out.println("Found row");
            for (final HtmlTableCell cell : row.getCells()) {
                //System.out.println("   Found cell: " + cell.asNormalizedText());

                //да намираме на кой ред се намира името на станцията и да взимаме съответната стойност за температурата
                //за td-тата имаме клас .lb -> (името на станцията) и клас .rb -> (съответната температура) -> дали може да ни помогне това?
                //Тук ще е най-трудно!
                //Всяка станция има rowspan="3" и пак има клас .lb
                //На първия от трите реда е Макс., на втория е Мин., на третия е Уср. (пак имат клас .rb)
                //Прескачаме само 6 реда.

                //Клетката на станцията съдържа:
                //<td class="lb">
                //	<span class="small">&nbsp;<име на станция></span>
                //</td>
                //Това долу е неработещ псевдокод:

                //if(cell.asNormalizedText() == this.station.getName() && cell.getNextElementSibling().hasAttribute("class") == "lb" ) {
//                if(cell.asNormalizedText() == timeline.getStation().getName() && cell.getNextElementSibling().getAttribute("class") == "lb" ) {
//                    //Тук съседната клетка вдясно е отново с клас .lb и с текст "Макс.". Следващата клетка след нея съдържа Макс. температура и заради това тя се намира първа
//                    timeline.setExtrMaxTemp(cell.getNextElementSibling().getNextElementSibling().asNormalizedText());
//
//                    //Ако може по някакъв начин да се отива на следващия ред на клетката, която е непосредствено отдолу, то тогава ще може да се намери Мин. температура
//                    timeline.setExtrMinTemp(cell.getNextElementSibling().asNormalizedText());
//
//                    //Отново трябва един ред надолу, за да се намери Уср. температура
//                    timeline.setExtrAvgTemp(cell.getNextElementSibling().asNormalizedText());
//
//                    //В краен случай -> може да намираме само Макс. температурата, а останалите да не се и опитваме да ги намираме.
//                }
                //System.out.println("timeline.getStation().getName(): "+timeline.getStation().getName());
                //System.out.println("cell.asNormalizedText(): " + cell.asNormalizedText());
                //System.out.println("Are they equal??? " + cell.asNormalizedText().trim().equals(timeline.getStation().getName().trim()) );

                if(cell.asNormalizedText().trim().equals(timeline.getStation().getName().trim())) {
                    //Всеки път презаписва стойността и заради това на третия път слага третата стойност, която и търсим
                    String temp = cell.getNextElementSibling().getNextElementSibling().asNormalizedText().trim();
                    timeline.setExtrMaxTemp(WebScrapingService.tryParseFloat(temp));

                    flag = true;
                }
                if(cell.asNormalizedText().trim().equals("Мин.") && flag){
                    String temp = cell.getNextElementSibling().asNormalizedText().trim();
                    timeline.setExtrMinTemp(WebScrapingService.tryParseFloat(temp));
                }
                if(cell.asNormalizedText().trim().equals("Уср.") && flag){
                    String temp = cell.getNextElementSibling().asNormalizedText().trim();
                    timeline.setExtrAvgTemp(WebScrapingService.tryParseFloat(temp));
                    counter++;
                    if(counter == 3){
                        flag = false;
                    }
                }
            }
        }

        //System.out.println("blah");
        //System.out.println(timeline);
        return timeline;
    }
    //[Мес. екстр.]/extrMaxTemp, extrMinTemp, extrAvgTemp -> https://www.stringmeteo.com/synop/maxmin_month.php?month=3&year=2000 -> maxmin_month e разликата

    //[Мес. темпер. Т.]/avgTemperature -> https://www.stringmeteo.com/synop/temp_month.php?month=3&year=2000 -> temp_month е разликата
    //[Мес. сн. покр.]/avgSnow -> https://www.stringmeteo.com/synop/snow_month.php?month=3&year=2000 -> snow е разликата
    //[Мес. валежи]/totRain -> https://www.stringmeteo.com/synop/prec_month.php?month=3&year=2000 -> prec_month е разликата
    //[Мес. сл. греене]/totSunshine -> https://www.stringmeteo.com/synop/sunsh_month.php?month=3&year=2000 -> sunsh_month е разликата
    //[Мес. екстр.]/extrMaxTemp, extrMinTemp, extrAvgTemp -> https://www.stringmeteo.com/synop/maxmin_month.php?month=3&year=2000 -> maxmin_month e разликата

    public static void ScrapeTimelines(StationService stationService, TimelineService timelineService){

        List<Station> stations = new ArrayList<>();
        

 //       stations.add(stationService.findById(1));
//        stations.add(stationService.findById(2));
 //       stations.add(stationService.findById(3));
        stations.add(stationService.findById(4));
        stations.add(stationService.findById(5));
// 
        List<Timeline> timelines = new ArrayList<>();

        for (int year = 2021; year <= 2022; year++){
            for (int month = 1; month <= 12; month++){

                //Има данни до 6-ти месец (юни), 2022 г.
                if(year == 2022 && month == 7){
                    break;
                }

                for (Station station: stations) {
                    Timeline timeline1;
                    timeline1 = Scraping(station, year, month);

                    System.out.println(timeline1);
                    
                    timelineService.save(timeline1);
                }
            }
        }

    }

}
