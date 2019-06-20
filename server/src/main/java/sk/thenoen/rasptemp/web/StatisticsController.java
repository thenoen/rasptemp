package sk.thenoen.rasptemp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sk.thenoen.rasptemp.domain.TemperatureRecord;
import sk.thenoen.rasptemp.repository.TemperatureRecordRepository;
import sk.thenoen.rasptemp.temperature.TemperatureRecordLoadingService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class StatisticsController {

	public static final int MAX_MEASUREMENTS_FOR_DISPLAY = 100;
	public static final MathContext MATH_CONTEXT = new MathContext(4, RoundingMode.HALF_UP);
	@Value("${temperature.input.file}")
	private String inputFilePath;

	@Autowired
	private TemperatureRecordRepository temperatureRecordRepository;

	@Autowired
	private TemperatureRecordLoadingService temperatureRecordLoadingService;

	@RequestMapping("/data")
	public String index() {
		long start = System.currentTimeMillis();
		temperatureRecordLoadingService.loadRecordsFromFile(inputFilePath);
		long end = System.currentTimeMillis();


		String response = "Greetings from Spring Boot!";

		response += " - nr of records: " + temperatureRecordRepository.findAll().size();
		response += " (loaded in " + ((end - start) / 1000.0) + " s)";

		return response;
	}

	@RequestMapping(value = "/latestValue", method = GET, produces = "application/json")
	@ResponseBody
	public TemperatureRecord getLatestValue() {
		return temperatureRecordRepository.findFirstByOrderByDateMeasuredDesc();
	}

	@RequestMapping(value = "/lastHours/{hours}/{groupSize}", method = GET, produces = "application/json")
	@ResponseBody
	public List<TemperatureRecord> getDataDuringLastPeriod(@PathVariable("hours") int hours,
	                                                       @PathVariable("groupSize") int groupSize) {

		LocalDateTime now = LocalDateTime.now();
		Date oldestDate = new Date(now.minusHours(hours).toEpochSecond(ZoneOffset.UTC) * 1000);
		List<TemperatureRecord> measuredAfter = temperatureRecordRepository.findAllByDateMeasuredAfter(oldestDate);

		if (measuredAfter.size() > MAX_MEASUREMENTS_FOR_DISPLAY) {
			int mod = measuredAfter.size() % groupSize;

			ArrayList<TemperatureRecord> averageTemperatures = new ArrayList<>();
			if (mod != 0) {
				averageTemperatures.add(calculateAverage(measuredAfter.subList(0, mod)));
			}
			for (int i = mod; i != measuredAfter.size(); i += groupSize) {
				averageTemperatures.add(calculateAverage(measuredAfter.subList(i, i + groupSize)));
			}
			return averageTemperatures;
		}

		return measuredAfter;
	}

	public TemperatureRecord calculateAverage(List<TemperatureRecord> temperatureRecords) {
		Double sumTemperature = temperatureRecords.stream()
				.map(TemperatureRecord::getDegrees)
				.reduce(Double::sum)
				.orElse(0d);

		Double averageTemperature = sumTemperature / temperatureRecords.size();
		TemperatureRecord average = new TemperatureRecord();
		average.setDateMeasured(temperatureRecords.get(temperatureRecords.size()/2).getDateMeasured());
		BigDecimal bigDecimal = BigDecimal.valueOf(averageTemperature);
		BigDecimal round = bigDecimal.round(MATH_CONTEXT);
		average.setDegrees(round.doubleValue());
		return average;
	}

}
