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

	@Autowired
	private TemperatureRecordLoadingService temperatureRecordLoadingService;

	@Scheduled(cron = "*/5 * * * * *")
	public void loadMeasurementsFromFile() {
		temperatureRecordLoadingService.loadFromSensorFile();
	}

}
