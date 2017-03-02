package sk.thenoen.rasptemp.temperature;

import org.joda.time.DateTimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@TestPropertySource(locations="classpath:test.properties")
@Transactional
public class TemperatureRecordLoadingServiceTest {

	public static final int FIXED_CURRENT_DATE_MILLIS = 123;
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
