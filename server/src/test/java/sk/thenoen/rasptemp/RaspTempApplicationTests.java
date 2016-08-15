package sk.thenoen.rasptemp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RaspTempApplication.class)
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
