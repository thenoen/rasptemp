package sk.thenoen.rasptemp.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sk.thenoen.rasptemp.RaspTempApplication;
import sk.thenoen.rasptemp.temperature.TemperatureRecordLoadingService;
import sk.thenoen.rasptemp.web.StatisticsController;


@SpringBootTest(classes = RaspTempApplication.class)
@TestPropertySource(locations="classpath:test.properties")
@AutoConfigureMockMvc
public class IndexControllerTest {

	private MockMvc mvc;

	@Autowired
	private StatisticsController statisticsController;

	@Autowired
	private TemperatureRecordLoadingService temperatureRecordLoadingService;

		@Test
	public void exampleTest() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource("temperatures/temp-log.txt");
		temperatureRecordLoadingService.setInitialFolderPath(classPathResource.getFile().getParent());
		temperatureRecordLoadingService.loadInitialRecordsFromFolder();

		mvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/latestValue")).andReturn();
		final String jsonResponse = mvcResult.getResponse().getContentAsString();
		Assertions.assertTrue(jsonResponse.matches("\\{\"id\":\\d+,\"degrees\":27.625,\"dateMeasured\":1483488010000\\}"));
	}
}
