package org.dontexist.kb.service.epuboperations;

import org.junit.AfterClass;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public class ZipEpubReaderServiceFactoryTest extends EpubReaderServiceFactoryTest {

    static {
        System.setProperty("epubReaderType", "zip");
    }

    @AfterClass
    public static void tearDown() {
        System.clearProperty("epubReaderType");
    }

    @Override
    Class getExpectedServiceClass() {
        return ZipEpubReaderServiceImpl.class;
    }
}
