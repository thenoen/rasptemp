package sk.thenoen.rasptemp.temperature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.Locale;

@Service
public class TemperatureRecordLoadingService {

	private static final Logger log = LoggerFactory.getLogger(TemperatureRecordLoadingService.class);
	public static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss z YYYY";

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

		for (TemperatureRecord temperatureRecord : temperatureRecords) {
			temperatureRecordRepository.save(temperatureRecord);
			log.info("Loaded temperature record: {}", temperatureRecord.getDegrees());
		}
	}

	private List<TemperatureRecord> loadRecords(BufferedReader bufferedReader) throws IOException, ParseException {
		List<TemperatureRecord> temperatureRecords = new ArrayList<>();
		while (true) {
			bufferedReader.readLine(); // skip this line
			bufferedReader.readLine(); // skip this line
			String dateString = bufferedReader.readLine();
			if (dateString == null) {
				break;
			}
			bufferedReader.readLine(); // skip this line
			bufferedReader.readLine(); // skip this line
			String temperatureValueString = bufferedReader.readLine();
			bufferedReader.readLine(); // skip this line
			bufferedReader.readLine(); // skip this line

			TemperatureRecord temperatureRecord = createTemperatureRecord(dateString, temperatureValueString);
			temperatureRecords.add(temperatureRecord);
		}
		return temperatureRecords;
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
