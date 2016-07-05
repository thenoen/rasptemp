package sk.thenoen.rasptemp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestBean {

    private static final Logger log = LoggerFactory.getLogger(TestBean.class);

    public void sayHi() {
        log.info("Hi!");
    }

}
