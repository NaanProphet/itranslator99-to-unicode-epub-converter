package org.dontexist.kb.visual;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class LoggerTestSpring {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTestSpring.class);

    /**
     * Visual test to ensure loggers are configured properly.
     */
    @Test
    public void test() {
        LOGGER.trace("trace");
        LOGGER.debug("debug");
        LOGGER.info("info");
        LOGGER.warn("warn");
        LOGGER.error("error");
    }

}
