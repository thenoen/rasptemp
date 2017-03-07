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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

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

//		LOGGER.info("loading temperature records from file: {}", pathToFile);

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
			long start = System.currentTimeMillis();
			temperatureRecords = loadRecords(bufferedReader);
			long end = System.currentTimeMillis();
			LOGGER.info("duration: {} ms", end - start);
		} catch (Exception e) {
			LOGGER.error("problem with loading records", e);
			return;
		}


//		LOGGER.info("going to store all loaded records to database");
		temperatureRecordRepository.save(temperatureRecords);
//		for (TemperatureRecord temperatureRecord : temperatureRecords) {
//			temperatureRecordRepository.save(temperatureRecord);
//			LOGGER.info("Loaded temperature record: {}", temperatureRecord.getDegrees());
//			int progress = temperatureRecords.indexOf(temperatureRecord) * 100 / temperatureRecords.size();
//			if (progress % 10 == 0) {
//				LOGGER.info("saving records to database - {}%", progress);
//			}
//		}
//		LOGGER.info("all loaded records were stored to database");
	}

	public TemperatureRecord loadFromSensorFile() {
		BigDecimal temperatureValue = sensorReadingService.readTemperature();

		TemperatureRecord temperatureRecord = new TemperatureRecord();
		temperatureRecord.setDateMeasured(new Date(DateTimeUtils.currentTimeMillis()));
		temperatureRecord.setDegrees(temperatureValue.doubleValue());

		return temperatureRecord;
	}

	private List<TemperatureRecord> loadRecords(BufferedReader bufferedReader) throws IOException, ParseException, BrokenBarrierException, InterruptedException, ExecutionException {
		final List<TemperatureRecord> temperatureRecords = new ArrayList<>();

		final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfThreads);

		final ExecutorCompletionService<TemperatureRecord> executorCompletionService = new ExecutorCompletionService(executor);

		final AtomicBoolean shouldFinish = new AtomicBoolean(false);
		final AtomicLong nrOfWorkers = new AtomicLong(0);

		Thread t = new Thread() {

			@Override
			public void run() {
				try {
					while (!shouldFinish.get() || nrOfWorkers.get() > 0) {
						Future<TemperatureRecord> future = executorCompletionService.take();
						temperatureRecords.add(future.get());
						nrOfWorkers.decrementAndGet();
					}
				} catch (InterruptedException | ExecutionException e) {
					LOGGER.error("error during collecting of temperatures", e);
				}
			}
		};
		t.start();

		while (!shouldFinish.get()) {
			Worker worker = createWorker(bufferedReader);
			if (worker != null) {
				executorCompletionService.submit(worker);
				nrOfWorkers.incrementAndGet();
			} else {
				shouldFinish.set(true);
			}
		}

		t.join();
		executor.shutdown();

//		LOGGER.info("loading finished - loaded {} records", temperatureRecords.size());
		return temperatureRecords;
	}

	private Worker createWorker(BufferedReader bufferedReader) throws IOException {
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

		return new Worker(dateString, temperatureValueString);
	}

	private class Worker implements Callable<TemperatureRecord> {

		private String dateString;
		private String temperatureValueString;

		private Worker(String dateString, String temperatureValueString) {
			this.dateString = dateString;
			this.temperatureValueString = temperatureValueString;
		}

		@Override
		public TemperatureRecord call() throws Exception {
			TemperatureRecord temperatureRecord = createTemperatureRecord(dateString, temperatureValueString);
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

	public void setNumberOfWorkers(int numberOfWorkers) {
		this.numberOfWorkers = numberOfWorkers;
	}

	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}
}
