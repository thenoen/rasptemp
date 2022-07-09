package sk.thenoen.rasptemp;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;

@SpringBootTest(classes = RaspTempApplication.class)
@TestPropertySource(locations="classpath:test.properties")
public class RaspTempApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(RaspTempApplicationTests.class);

	@Test
	public void contextLoads() {
	}

	@Test
	public void filesystemAccess() throws IOException {
		File file = new File(".");
		log.info(file.getCanonicalPath());
	}

}
