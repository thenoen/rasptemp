package sk.thenoen.rasptemp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sk.thenoen.rasptemp.domain.TemperatureRecord;
import sk.thenoen.rasptemp.repository.TemperatureRecordRepository;
import sk.thenoen.rasptemp.temperature.TemperatureRecordLoadingService;

import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class StatisticsController {

	@Value("${temperature.input.file}")
	private String inputFilePath;

	@Autowired
	private TemperatureRecordRepository temperatureRecordRepository;

	@Autowired
	private TemperatureRecordLoadingService temperatureRecordLoadingService;

	@RequestMapping("/data")
	public String index() {

		long start =  System.currentTimeMillis();
		temperatureRecordLoadingService.loadRecordsFromFile(inputFilePath);
		long end =  System.currentTimeMillis();


		String response = "Greetings from Spring Boot!";

		response += " - nr of records: " + temperatureRecordRepository.findAll().size();
		response += " (loaded in " + ((end - start)/1000.0) +" s)";

		return response;
	}

	@RequestMapping(value="/latestValue", method = GET, produces = "application/json")
	@ResponseBody
	public TemperatureRecord getLatestValue() {

//		TemperatureRecord firstOrOrderByDateMeasuredDesc = temperatureRecordRepository.findFirstByOrderByDateMeasuredDesc();
		TemperatureRecord tr = new TemperatureRecord();
		tr.setDateMeasured(new Date());
		tr.setDegrees(25d);
		tr.setId(1L);
		return tr;
//		return firstOrOrderByDateMeasuredDesc;
	}

}
