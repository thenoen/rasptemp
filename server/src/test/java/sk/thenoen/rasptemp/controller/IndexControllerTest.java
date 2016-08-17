package sk.thenoen.rasptemp.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import sk.thenoen.rasptemp.RaspTempApplication;
import sk.thenoen.rasptemp.temperature.TemperatureRecordLoadingService;
import sk.thenoen.rasptemp.web.StatisticsController;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RaspTempApplication.class, loader=SpringApplicationContextLoader.class)
@AutoConfigureMockMvc
public class IndexControllerTest {

//	@Autowired
//	private TestRestTemplate restTemplate;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private StatisticsController statisticsController;

	@Autowired
	private TemperatureRecordLoadingService temperatureRecordLoadingService;

	@Test
	public void exampleTest() {
//		String body = this.restTemplate.getForObject("/", String.class);
		Assert.assertEquals("body", "Hello World");
	}
}
