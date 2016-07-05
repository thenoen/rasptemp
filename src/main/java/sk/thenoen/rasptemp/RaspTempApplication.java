package sk.thenoen.rasptemp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
//@EnableAutoConfiguration
@SpringBootApplication
public class RaspTempApplication {

	private static final Logger log = LoggerFactory.getLogger(RaspTempApplication.class);

	public static void main(String[] args) {
		log.info("Before main() method");
		SpringApplication.run(RaspTempApplication.class, args);
		log.info("After main() method");
	}
}
