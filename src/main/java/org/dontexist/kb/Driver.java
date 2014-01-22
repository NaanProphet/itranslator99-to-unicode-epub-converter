package org.dontexist.kb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Driver {

	private static final Logger logger = LoggerFactory.getLogger(Driver.class);

	public static void main(String[] args) throws Exception {
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/app-context.xml");
		logger.debug("Spring context initialized.");

		SpringDriver springDriver = (SpringDriver) applicationContext.getBean("springDriver");
		springDriver.main();

	}

}
