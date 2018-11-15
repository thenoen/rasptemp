package sk.thenoen.rasptemp.temperature;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.thenoen.rasptemp.RaspTempApplication;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RaspTempApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
public class SensorReadingServiceTest {

	@Autowired
	private SensorReadingService sensorReadingService;

	@Test
	public void correctLoadingOfTemperature() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("/sensor-output.txt");
		sensorReadingService.setSensorOutputPath(classPathResource.getFile().getAbsolutePath());

		BigDecimal temperatureValue = sensorReadingService.readTemperature();

		Assert.assertEquals(new BigDecimal("24.812"), temperatureValue);
	}

	@Test
	public void nullIsReturnedWhenIsChecksumInvalid() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("/sensor-output-invalid-checksum.txt");
		sensorReadingService.setSensorOutputPath(classPathResource.getFile().getAbsolutePath());

		BigDecimal temperatureValue = sensorReadingService.readTemperature();

		Assert.assertEquals(null, temperatureValue);
	}

	@Test
	public void nullIsReturnedWhenIsFormatInvalid() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("/sensor-output-invalid-format.txt");
		sensorReadingService.setSensorOutputPath(classPathResource.getFile().getAbsolutePath());

		BigDecimal temperatureValue = sensorReadingService.readTemperature();

		Assert.assertEquals(null, temperatureValue);
	}

	@Test
	public void nullIsReturnedWhenFileIsNotAvailable() throws IOException {
		sensorReadingService.setSensorOutputPath("not-existing-path");

		BigDecimal temperatureValue = sensorReadingService.readTemperature();

		Assert.assertEquals(null, temperatureValue);
	}
}
