package org.dontexist.kb;

import java.io.IOException;

import net.lingala.zip4j.exception.ZipException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Driver {

	private static final Logger LOGGER = LoggerFactory.getLogger(Driver.class);

	private Driver() {
	}

	public static void main(String[] args) throws ZipException, IOException {
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/app-context.xml");
		LOGGER.debug("Spring context initialized.");

		SpringDriver springDriver = (SpringDriver) applicationContext.getBean("springDriver");
		springDriver.main();

	}

}
