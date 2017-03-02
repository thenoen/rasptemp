package sk.thenoen.rasptemp.temperature;

import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.thenoen.rasptemp.domain.TemperatureRecord;
import sk.thenoen.rasptemp.repository.TemperatureRecordRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Service
public class TemperatureRecordLoadingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureRecordLoadingService.class);

	public static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss z YYYY";

	@Value("${workers.number}")
	private int numberOfWorkers;

	@Value("${threads.number}")
	public int numberOfThreads;

	@Autowired
	private SensorReadingService sensorReadingService;

	@Autowired
	private TemperatureRecordRepository temperatureRecordRepository;

	public void loadRecordsFromFile(String pathToFile) {

		LOGGER.info("loading temperature records from file: {}", pathToFile);

		File file = new File(pathToFile);
		if (!file.exists()) {
			LOGGER.error("file does not exist");
			return;
		}

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			LOGGER.error("Problem loading temperature records from file", e);
			return;
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		List<TemperatureRecord> temperatureRecords;
		try {
			temperatureRecords = loadRecords(bufferedReader);
		} catch (Exception e) {
			LOGGER.error("problem with loading records", e);
			return;
		}


		LOGGER.info("going to store all loaded records to database");
		temperatureRecordRepository.save(temperatureRecords);
//		for (TemperatureRecord temperatureRecord : temperatureRecords) {
//			temperatureRecordRepository.save(temperatureRecord);
//			LOGGER.info("Loaded temperature record: {}", temperatureRecord.getDegrees());
//			int progress = temperatureRecords.indexOf(temperatureRecord) * 100 / temperatureRecords.size();
//			if (progress % 10 == 0) {
//				LOGGER.info("saving records to database - {}%", progress);
//			}
//		}
		LOGGER.info("all loaded records were stored to database");
	}

	public TemperatureRecord loadFromSensorFile() {
		BigDecimal temperatureValue = sensorReadingService.readTemperature();

		TemperatureRecord temperatureRecord = new TemperatureRecord();
		temperatureRecord.setDateMeasured(new Date(DateTimeUtils.currentTimeMillis()));
		temperatureRecord.setDegrees(temperatureValue.doubleValue());

		return temperatureRecord;
	}

	private List<TemperatureRecord> loadRecords(BufferedReader bufferedReader) throws IOException, ParseException, BrokenBarrierException, InterruptedException, ExecutionException {
		List<TemperatureRecord> temperatureRecords = new ArrayList<>();

		final List<Future<TemperatureRecord>> futureList = new ArrayList<>(numberOfWorkers);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
		CountDownLatch countDownLatch = new CountDownLatch(numberOfWorkers);


		while (true) {
			boolean shouldFinish = false;
			for (int i = 0; i < numberOfWorkers; i++) {
				Worker worker = createWorker(bufferedReader, countDownLatch);
				if (worker != null) {
					futureList.add(executor.submit(worker));
				} else {
					//we need to count down the latch to be able to unlock collecting of worker results
					countDownLatch.countDown();
					shouldFinish = true;
				}
			}

			retrieveParsedRecords(temperatureRecords, futureList, countDownLatch);
			countDownLatch = new CountDownLatch(numberOfWorkers);
			if (shouldFinish) {
				break;
			}
		}

		executor.shutdown();

		LOGGER.info("loading finished - loaded {} records", temperatureRecords.size());
		return temperatureRecords;
	}

	private void retrieveParsedRecords(List<TemperatureRecord> temperatureRecords, List<Future<TemperatureRecord>> futureList, CountDownLatch countDownLatch) throws InterruptedException, ExecutionException {
//		LOGGER.info("going to wait on barrier - current number of waiting threads: {}/{}", countDownLatch.getCount(), numberOfWorkers);
		countDownLatch.await();
//		LOGGER.info("waiting on a barrier finished - current count: {}", countDownLatch.getCount());
		for (Future<TemperatureRecord> future : futureList) {
			temperatureRecords.add(future.get());
		}
		futureList.clear();
	}

	private Worker createWorker(BufferedReader bufferedReader, CountDownLatch countDownLatch) throws IOException {
		bufferedReader.readLine(); // skip this line
		bufferedReader.readLine(); // skip this line
		String dateString = bufferedReader.readLine();
		if (dateString == null) {
			return null;
		}
		bufferedReader.readLine(); // skip this line
		bufferedReader.readLine(); // skip this line
		String temperatureValueString = bufferedReader.readLine();
		bufferedReader.readLine(); // skip this line
		bufferedReader.readLine(); // skip this line

		return new Worker(dateString, temperatureValueString, countDownLatch);
	}

	private class Worker implements Callable<TemperatureRecord> {

		private CountDownLatch countDownLatch;
		private String dateString;
		private String temperatureValueString;

		private Worker(String dateString, String temperatureValueString, CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
			this.dateString = dateString;
			this.temperatureValueString = temperatureValueString;
		}

		@Override
		public TemperatureRecord call() throws Exception {
			TemperatureRecord temperatureRecord = createTemperatureRecord(dateString, temperatureValueString);
//			LOGGER.info("going to count down a latch - current count: {}({})", countDownLatch.getCount(), numberOfWorkers);
			countDownLatch.countDown();
			return temperatureRecord;
		}

		private TemperatureRecord createTemperatureRecord(String dateString, String temperatureValueString) throws IOException, ParseException {
			Date dateMeasured;

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
			dateMeasured = simpleDateFormat.parse(dateString);

			TemperatureRecord temperatureRecord = new TemperatureRecord();
			temperatureRecord.setDateMeasured(dateMeasured);
			temperatureRecord.setDegrees(Double.valueOf(temperatureValueString.split(":")[1].trim()));
			return temperatureRecord;
		}

	}
}
