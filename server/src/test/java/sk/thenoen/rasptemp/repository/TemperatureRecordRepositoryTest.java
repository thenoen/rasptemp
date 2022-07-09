package sk.thenoen.rasptemp.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import sk.thenoen.rasptemp.RaspTempApplication;
import sk.thenoen.rasptemp.domain.TemperatureRecord;

import javax.transaction.Transactional;
import java.util.Date;

@SpringBootTest(classes = RaspTempApplication.class)
@TestPropertySource(locations="classpath:test.properties")
@Transactional
public class TemperatureRecordRepositoryTest {

	@Autowired
	private TemperatureRecordRepository temperatureRecordRepository;

	@Test
	public void recordedValueCanBeSaved() {
		Double testDegrees = 23.5;
		Date testDate = new Date();

		TemperatureRecord temperatureRecord = new TemperatureRecord();
		temperatureRecord.setDegrees(testDegrees);
		temperatureRecord.setDateMeasured(testDate);

		temperatureRecord = temperatureRecordRepository.save(temperatureRecord);

		Assertions.assertNotNull(temperatureRecord.getId());
		Assertions.assertEquals(testDegrees, temperatureRecord.getDegrees());
		Assertions.assertEquals(testDate, temperatureRecord.getDateMeasured());
	}

}