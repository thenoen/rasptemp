package sk.thenoen.rasptemp.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sk.thenoen.rasptemp.RaspTempApplication;
import sk.thenoen.rasptemp.temperature.TemperatureRecordLoadingService;
import sk.thenoen.rasptemp.web.StatisticsController;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RaspTempApplication.class, loader= SpringBootContextLoader.class)
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
		mvc = MockMvcBuilders.standaloneSetup(new StatisticsController()).build();
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/latestValue")).andReturn();
		Assert.assertTrue(mvcResult.getResponse().getContentAsString().startsWith("{\"id\":1,\"degrees\":25.0,\"dateMeasured\":"));
	}
}
