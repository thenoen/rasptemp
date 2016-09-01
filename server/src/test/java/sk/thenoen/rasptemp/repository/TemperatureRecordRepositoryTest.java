package sk.thenoen.rasptemp.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.thenoen.rasptemp.RaspTempApplication;
import sk.thenoen.rasptemp.domain.TemperatureRecord;

import javax.transaction.Transactional;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RaspTempApplication.class)
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

		Assert.assertNotNull(temperatureRecord.getId());
		Assert.assertEquals(testDegrees, temperatureRecord.getDegrees());
		Assert.assertEquals(testDate, temperatureRecord.getDateMeasured());
	}

}