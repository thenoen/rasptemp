package sk.thenoen.rasptemp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RaspTempApplication.class)
public class RaspTempApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void filesystemAccess() throws IOException {
		File file = new File(".");
		System.out.println(file.getCanonicalPath());
	}

}
