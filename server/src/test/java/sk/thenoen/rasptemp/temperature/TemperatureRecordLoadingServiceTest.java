package sk.thenoen.rasptemp.temperature;

import org.joda.time.DateTimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.thenoen.rasptemp.RaspTempApplication;
import sk.thenoen.rasptemp.domain.TemperatureRecord;
import sk.thenoen.rasptemp.repository.TemperatureRecordRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RaspTempApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
public class TemperatureRecordLoadingServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureRecordLoadingServiceTest.class);

	private static final int FIXED_CURRENT_DATE_MILLIS = 123;

	@Autowired
	private TemperatureRecordLoadingService temperatureRecordLoadingService;

	@Autowired
	private SensorReadingService sensorReadingService;

	@Autowired
	private TemperatureRecordRepository temperatureRecordRepository;

	@Test
	public void verifyCorrectLoadingOfTemperatures() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("temp-log.txt");

		temperatureRecordLoadingService.loadRecordsFromFile(classPathResource.getFile().getAbsolutePath());

		List<TemperatureRecord> temperatureRecords = temperatureRecordRepository.findAll();

		Assert.assertEquals(16, temperatureRecords.size());
	}

	@Test
	public void testPerformanceOfLoadingOfTemperatures() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("/temperature-records-performance-test.txt");

		Long start = System.currentTimeMillis();
		temperatureRecordLoadingService.loadRecordsFromFile(classPathResource.getFile().getAbsolutePath());
		Long end = System.currentTimeMillis();
		LOGGER.info("Duration of loading in first run: {}", end - start);
		Assert.assertEquals(22812, temperatureRecordRepository.findAll().size());
		temperatureRecordRepository.deleteAll();

		for (int threads = 1; threads < 11; threads++) {
			for (int workers = 1; workers < 11; workers++) {
				LOGGER.info("Running benchmark with {} threads and {} workers", threads, workers);
				temperatureRecordLoadingService.setNumberOfThreads(threads);
				temperatureRecordLoadingService.setNumberOfWorkers(workers);
				temperatureRecordLoadingService.loadRecordsFromFile(classPathResource.getFile().getAbsolutePath());

				Assert.assertEquals(22812, temperatureRecordRepository.findAll().size());
				temperatureRecordRepository.deleteAll();
				LOGGER.info("\n");
			}
		}
	}

	@Test
	public void verifyCorrectLoadingOfTemperatureRecordFromSensors() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("/sensor-output.txt");
		sensorReadingService.setSensorOutputPath(classPathResource.getFile().getAbsolutePath());

		DateTimeUtils.setCurrentMillisFixed(FIXED_CURRENT_DATE_MILLIS);

		TemperatureRecord temperatureRecord = temperatureRecordLoadingService.loadFromSensorFile();

		Assert.assertEquals(FIXED_CURRENT_DATE_MILLIS, temperatureRecord.getDateMeasured().getTime());
		Assert.assertEquals(Double.valueOf("24.812"), temperatureRecord.getDegrees());

		DateTimeUtils.setCurrentMillisSystem();
	}

}
