package org.dontexist.kb.service.epuboperations;

import org.junit.AfterClass;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public class EpublibEpubReaderServiceFactoryTest extends EpubReaderServiceFactoryTest {

    static {
        System.setProperty("epubReaderType", "epublib");
    }

    @AfterClass
    public static void tearDown() {
        System.clearProperty("epubReaderType");
    }

    @Override
    Class getExpectedServiceClass() {
        return EpublibEpubReaderServiceImpl.class;
    }
}
