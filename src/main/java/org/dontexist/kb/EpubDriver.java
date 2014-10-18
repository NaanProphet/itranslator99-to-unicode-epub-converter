package org.dontexist.kb;

import java.io.IOException;

import net.lingala.zip4j.exception.ZipException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The main class that should be invoked from the command line to launch the program.
 */
public class EpubDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpubDriver.class);

    private static ClassPathXmlApplicationContext context;

    private EpubDriver() {
    }

    public static void main(String[] args) throws ZipException, IOException {
        try {
            context = new ClassPathXmlApplicationContext("/app-context.xml");
            LOGGER.debug("Spring context initialized.");

            SpringDelegateDriver springDelegateDriver = (SpringDelegateDriver) context.getBean("springDelegateDriver");
            springDelegateDriver.main();
        } finally {
            if (context != null) {
                context.close();
            }
        }
    }

}
