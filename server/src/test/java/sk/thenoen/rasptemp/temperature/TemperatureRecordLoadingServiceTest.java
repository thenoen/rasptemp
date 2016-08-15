package sk.thenoen.rasptemp.temperature;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.thenoen.rasptemp.RaspTempApplication;
import sk.thenoen.rasptemp.domain.TemperatureRecord;
import sk.thenoen.rasptemp.repository.TemperatureRecordRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RaspTempApplication.class)
@Transactional
public class TemperatureRecordLoadingServiceTest {

	@Autowired
	private TemperatureRecordLoadingService temperatureRecordLoadingService;

	@Autowired
	private TemperatureRecordRepository temperatureRecordRepository;

	@Test
	public void verifyCorrectLoadingOfTemperatures() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("temp-log.txt");

		temperatureRecordLoadingService.loadRecordsFromFile(classPathResource.getFile().getAbsolutePath());

		List<TemperatureRecord> temperatureRecords = temperatureRecordRepository.findAll();

		Assert.assertEquals(4, temperatureRecords.size());
	}

}
