package sk.thenoen.rasptemp.temperature;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Service
public class TemperatureRecordLoadingService {

	private static final Logger log = LoggerFactory.getLogger(TemperatureRecordLoadingService.class);
	public static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss z YYYY";


	@Value("${workers.number}")
	private int numberOfWorkers;

	@Value("${threads.number}")
	public int numberOfThreads;

	@Autowired
	private TemperatureRecordRepository temperatureRecordRepository;

	public void loadRecordsFromFile(String pathToFile) {

		log.info("loading temperature records from file: {}", pathToFile);

		File file = new File(pathToFile);
		if (!file.exists()) {
			log.error("file does not exist");
			return;
		}

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			log.error("Problem loading temperature records from file", e);
			return;
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		List<TemperatureRecord> temperatureRecords;
		try {
			temperatureRecords = loadRecords(bufferedReader);
		} catch (Exception e) {
			log.error("problem with loading records", e);
			return;
		}


		log.info("going to store all loaded records to database");
		temperatureRecordRepository.save(temperatureRecords);
//		for (TemperatureRecord temperatureRecord : temperatureRecords) {
//			temperatureRecordRepository.save(temperatureRecord);
//			log.info("Loaded temperature record: {}", temperatureRecord.getDegrees());
//			int progress = temperatureRecords.indexOf(temperatureRecord) * 100 / temperatureRecords.size();
//			if (progress % 10 == 0) {
//				log.info("saving records to database - {}%", progress);
//			}
//		}
		log.info("all loaded records were stored to database");
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

		log.info("loading finished - loaded {} records", temperatureRecords.size());
		return temperatureRecords;
	}

	private void retrieveParsedRecords(List<TemperatureRecord> temperatureRecords, List<Future<TemperatureRecord>> futureList, CountDownLatch countDownLatch) throws InterruptedException, ExecutionException {
		log.info("going to wait on barrier - current number of waiting threads: {}/{}", countDownLatch.getCount(), numberOfWorkers);
		countDownLatch.await();
		log.info("waiting on a barrier finished - current count: {}", countDownLatch.getCount());
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
			log.info("going to count down a latch - current count: {}({})", countDownLatch.getCount(), numberOfWorkers);
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
