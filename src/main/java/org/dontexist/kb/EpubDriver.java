package org.dontexist.kb;

import java.io.IOException;

import net.lingala.zip4j.exception.ZipException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EpubDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpubDriver.class);

    private static ClassPathXmlApplicationContext context;

    private EpubDriver() {
    }

    public static void main(String[] args) throws ZipException, IOException {
        try {
            context = new ClassPathXmlApplicationContext("/app-context.xml");
            LOGGER.debug("Spring context initialized.");

            SpringDriver springDriver = (SpringDriver) context.getBean("springDriver");
            springDriver.main();
        } finally {
            if (context != null) {
                context.close();
            }
        }
    }

}
