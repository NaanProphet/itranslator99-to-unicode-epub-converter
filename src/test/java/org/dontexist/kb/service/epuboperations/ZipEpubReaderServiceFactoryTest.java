package org.dontexist.kb.service.epuboperations;

import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public class ZipEpubReaderServiceFactoryTest extends EpubReaderServiceFactoryTest {

    static {
        System.setProperty("epubReaderType", "zip");
    }

    @Override
    Class getExpectedServiceClass() {
        return ZipEpubReaderServiceImpl.class;
    }
}
