package sk.thenoen.rasptemp.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sk.thenoen.rasptemp.domain.TemperatureRecord;
import sk.thenoen.rasptemp.repository.TemperatureRecordRepository;
import sk.thenoen.rasptemp.temperature.TemperatureRecordLoadingService;
import sk.thenoen.rasptemp.web.model.DataSince;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class StatisticsController {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StatisticsController.class);

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
		final TemperatureRecord firstByOrderByDateMeasuredDesc = temperatureRecordRepository.findFirstByOrderByDateMeasuredDesc();
		return firstByOrderByDateMeasuredDesc;
	}

	@RequestMapping(value = "/lastHours/{hours}/{groupSize}", method = GET, produces = "application/json")
	@ResponseBody
	public List<TemperatureRecord> getDataDuringLastPeriod(@PathVariable("hours") int hours,
														   @PathVariable("groupSize") int groupSize) {

		LocalDateTime now = LocalDateTime.now();
		Date oldestDate = convertToDate(now.minusHours(hours));
		logger.info("now: {}", now);
		logger.info("oldestDate: {}", oldestDate);

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
//			convertTime(averageTemperatures, 0);
			return averageTemperatures;
		}

//		convertTime(measuredAfter, 0);
		return measuredAfter;
	}

	@RequestMapping(value = "/lastHoursSince", method = POST, produces = "application/json")
	@ResponseBody
	public List<TemperatureRecord> getDataSince(@RequestBody DataSince dataSince) {

		final LocalDateTime newestDateTime = dataSince.getSince();
		final LocalDateTime oldestDateTime = newestDateTime.minusHours(dataSince.getHours());
		final Date newestDate = convertToDate(newestDateTime);
		final Date oldestDate = convertToDate(oldestDateTime);

		List<TemperatureRecord> measuredAfter = temperatureRecordRepository.findAllByDateMeasuredAfterAndAndDateMeasuredBefore(oldestDate, newestDate);

		if (measuredAfter.size() > MAX_MEASUREMENTS_FOR_DISPLAY) {
			int mod = measuredAfter.size() % dataSince.getGroupSize();

			ArrayList<TemperatureRecord> averageTemperatures = new ArrayList<>();
			if (mod != 0) {
				averageTemperatures.add(calculateAverage(measuredAfter.subList(0, mod)));
			}
			for (int i = mod; i != measuredAfter.size(); i += dataSince.getGroupSize()) {
				averageTemperatures.add(calculateAverage(measuredAfter.subList(i, i + dataSince.getGroupSize())));
			}

			convertTime(averageTemperatures, 1);
			return averageTemperatures;
		}

		convertTime(measuredAfter,1);
		return measuredAfter;
	}

	private void convertTime(List<TemperatureRecord> averageTemperatures, long plusYears) {
		averageTemperatures.stream()
						   .forEach(temperatureRecord -> {
							   final Date dateMeasured = temperatureRecord.getDateMeasured();
							   final LocalDateTime newLocalDateTime = convertToLocalDateTime(dateMeasured).plusYears(plusYears);
							   final Date newDateMeasured = convertToDate(newLocalDateTime);
							   temperatureRecord.setDateMeasured(newDateMeasured);
						   });
	}

	public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
		return dateToConvert.toInstant()
							.atZone(ZoneId.systemDefault())
							.toLocalDateTime();
	}

	public Date convertToDate(LocalDateTime dateToConvert) {
		return java.util.Date.from(dateToConvert.atZone(ZoneOffset.UTC)
												.toInstant());
	}

	public TemperatureRecord calculateAverage(List<TemperatureRecord> temperatureRecords) {
		Double sumTemperature = temperatureRecords.stream()
												  .map(TemperatureRecord::getDegrees)
												  .reduce(Double::sum)
												  .orElse(0d);

		Double averageTemperature = sumTemperature / temperatureRecords.size();
		TemperatureRecord average = new TemperatureRecord();
		average.setDateMeasured(temperatureRecords.get(temperatureRecords.size() / 2).getDateMeasured());
		BigDecimal bigDecimal = BigDecimal.valueOf(averageTemperature);
		BigDecimal round = bigDecimal.round(MATH_CONTEXT);
		average.setDegrees(round.doubleValue());
		return average;
	}

	// GET method for finding records between two dates
	@RequestMapping(value = "/between/{from}/{to}", method = GET, produces = "application/json")
	@ResponseBody
	public List<TemperatureRecord> getDataBetween(@PathVariable("from") long from,
												  @PathVariable("to") long to) {
		Date fromDate = new Date(from);
		Date toDate = new Date(to);
		return temperatureRecordRepository.findAllByDateMeasuredBetween(fromDate, toDate);
	}

}
