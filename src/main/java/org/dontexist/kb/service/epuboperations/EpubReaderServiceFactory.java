package org.dontexist.kb.service.epuboperations;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Spring factory class for supplying a {@link org.dontexist.kb.service.epuboperations.EpubReaderService}.
 */
@Component
public class EpubReaderServiceFactory {


    private final String epubReaderServiceImplBeanName;
    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    public EpubReaderServiceFactory(final String epubReaderServiceImplBeanName) {
        this.epubReaderServiceImplBeanName = epubReaderServiceImplBeanName;
    }

    public EpubReaderService createEpubReaderService(final File epubFile) {
        return (EpubReaderService) beanFactory.getBean(epubReaderServiceImplBeanName, epubFile);
    }
}
