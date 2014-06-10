package org.dontexist.kb.service.epuboperations;

import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public class EpublibEpubReaderServiceFactoryTest extends EpubReaderServiceFactoryTest {

    static {
        System.setProperty("epubReaderType", "epublib");
    }

    @Override
    Class getExpectedServiceClass() {
        return EpublibEpubReaderServiceImpl.class;
    }
}
