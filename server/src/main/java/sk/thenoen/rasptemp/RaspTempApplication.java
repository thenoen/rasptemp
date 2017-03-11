package sk.thenoen.rasptemp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import sk.thenoen.rasptemp.temperature.TemperatureRecordLoadingService;

@ComponentScan
//@EnableAutoConfiguration
@EnableScheduling
@SpringBootApplication
public class RaspTempApplication {

	private static final Logger log = LoggerFactory.getLogger(RaspTempApplication.class);

	public static void main(String[] args) {
		log.info("Before main() method");
		ConfigurableApplicationContext context = SpringApplication.run(RaspTempApplication.class, args);

		TemperatureRecordLoadingService temperatureRecordLoadingService = context.getBean(TemperatureRecordLoadingService.class);
		temperatureRecordLoadingService.loadInitialRecordsFromFolder();

		log.info("After main() method");
	}
}
