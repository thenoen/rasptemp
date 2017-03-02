package sk.thenoen.rasptemp.temperature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SensorReadingService {

	Logger LOG = LoggerFactory.getLogger(SensorReadingService.class);

	@Value("${temperature.sensorOutputPath}")
	private String sensorOutputPath;

	private static final BigDecimal DIVISOR = new BigDecimal("1000");

	private static final Pattern line1Pattern = Pattern.compile(".*?crc=.*?\\s(.*)");
	private static final Pattern line2Pattern = Pattern.compile(".*?t=(.*)");

	public BigDecimal readTemperature() {

		try (FileReader fileReader = new FileReader(sensorOutputPath);
		     BufferedReader bufferedReader = new BufferedReader(fileReader)) {
			String temperatureLine1 = bufferedReader.readLine();
			String temperatureLine2 = bufferedReader.readLine();
			return parseTemperatureValue(temperatureLine1, temperatureLine2);
		} catch (IOException e) {
			LOG.error("Cannot open sensor path for reading: '{}'", sensorOutputPath, e);
		}

		return null;
	}

	private BigDecimal parseTemperatureValue(String line1, String line2) {
		if (line1 != null) {
			Matcher matcher = line1Pattern.matcher(line1);
			if (matcher.matches() && matcher.group(1).equals("YES")) {
				if (line2 != null) {
					matcher = line2Pattern.matcher(line2);
					if (matcher.matches()) {
						return new BigDecimal(matcher.group(1)).divide(DIVISOR);
					}
				}
			}
		}

		LOG.error("Sensor output unexpected:\nline-1: {}\nline-2: {}", line1, line2);
		return null;
	}

	public void setSensorOutputPath(String sensorOutputPath) {
		this.sensorOutputPath = sensorOutputPath;
	}
}
