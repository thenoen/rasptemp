package sk.thenoen.rasptemp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ConsoleRunner.class);

    @Autowired
    private TestBean testBean;

    @Override
    public void run(String... strings) throws Exception {
        log.info("ConsoleRunner started with params: " + Arrays.toString(strings));
        testBean.sayHi();
    }
}
