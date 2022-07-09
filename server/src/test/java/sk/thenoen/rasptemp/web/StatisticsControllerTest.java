package sk.thenoen.rasptemp.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import sk.thenoen.rasptemp.temperature.TemperatureRecordLoadingService;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:test.properties")
class StatisticsControllerTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TemperatureRecordLoadingService temperatureRecordLoadingService;


    @Test
    void getLatestValue() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("temperatures/temp-log.txt");
        temperatureRecordLoadingService.setInitialFolderPath(classPathResource.getFile().getParent());
        temperatureRecordLoadingService.loadInitialRecordsFromFolder();

        final String response = this.restTemplate.getForObject("http://localhost:" + port + "/latestValue", String.class);
        Assertions.assertTrue(response.matches("\\{\"id\":\\d+,\"degrees\":27\\.625,\"dateMeasured\":\"2017-01-04T00:00:10\\.000\\+00:00\"\\}"));
    }
}