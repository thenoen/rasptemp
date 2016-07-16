package sk.thenoen.rasptemp.temperature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LoadingScheduler {

	private static final Logger log = LoggerFactory.getLogger(LoadingScheduler.class);

	@Value("${temperature.input.file}")
	private String inputFilePath;

	@Autowired
	private TemperatureRecordLoadingService temperatureRecordLoadingService;

//	@Scheduled(fixedRate = 60000)
	public void loadMeasurementsFromFile() {

		log.info("loading records from file {}", inputFilePath);
		temperatureRecordLoadingService.loadRecordsFromFile(inputFilePath);
	}

}
