package sk.thenoen.rasptemp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestBean {

    private static final Logger log = LoggerFactory.getLogger(TestBean.class);

	@Value("${testbean.configparam}")
	private String configParam;

    public void sayHi() {
        log.info("Hi!");
		log.info("value of config param: '{}'", configParam);
    }

}
