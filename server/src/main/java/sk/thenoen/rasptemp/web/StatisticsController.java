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

	@RequestMapping("/")
	public String index() {

		temperatureRecordLoadingService.loadRecordsFromFile(inputFilePath);

		String response = "Greetings from Spring Boot!";

		response += " - nr of records: " + temperatureRecordRepository.findAll().size();

		return response;
	}

	@RequestMapping(value="/latestValue", method = GET, produces = "application/json")
	@ResponseBody
	public TemperatureRecord getLatestValue() {
		TemperatureRecord tr = new TemperatureRecord();
		tr.setDateMeasured(new Date());
		tr.setDegrees(25d);
		tr.setId(1L);
		return tr;
	}

}
