package sk.thenoen.rasptemp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.thenoen.rasptemp.repository.TemperatureRecordRepository;

@RestController
public class StatisticsController {

	@Autowired
	private TemperatureRecordRepository temperatureRecordRepository;

	@RequestMapping("/")
	public String index() {
		String response = "Greetings from Spring Boot!";

		response += " - nr of records: " + temperatureRecordRepository.findAll().size();

		return response;
	}

}
