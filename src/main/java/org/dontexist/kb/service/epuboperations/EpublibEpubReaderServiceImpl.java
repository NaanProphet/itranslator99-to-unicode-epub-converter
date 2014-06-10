package org.dontexist.kb.service.epuboperations;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope("prototype")
public class EpublibEpubReaderServiceImpl implements EpubReaderService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpublibEpubReaderServiceImpl.class);

    private static final String EPUB_TEXT_FOLDER = "text";
    private static final String EPUB_FILE_ENCODING = "UTF-8";

    private final EpubReader epubReader = new EpubReader();
    private final File epubFile;
    private Book bookIn;

    public EpublibEpubReaderServiceImpl(final File epubFile) throws IOException {
        this.epubFile = epubFile;
    }

    @Override
    public Map<String, String> openEpubFindingTextHtmlFiles() throws IOException {
        Map<String, String> epubPagesMap = new HashMap<String, String>();
        List<Resource> contents = bookIn.getContents();
        for (Resource page : contents) {
            String qualifiedFilename = page.getHref();
            MediaType mediaType = page.getMediaType();
            // see MediatypeService for list of allowable MediaType s
            System.out.println(qualifiedFilename + " of media type " + mediaType);
            if (qualifiedFilename.contains(EPUB_TEXT_FOLDER)) {
                InputStream myInputStream = page.getInputStream();
                String pageAsString = IOUtils.toString(myInputStream, EPUB_FILE_ENCODING);
                epubPagesMap.put(page.getHref(), pageAsString);
            }
        }
        return epubPagesMap;
    }

    @Override
    public void writeEpubPage(final String fileAsString, final String hrefLocation) throws IOException {
        Resource pageToUpdate = bookIn.getResources().getByHref(hrefLocation);
        pageToUpdate.setData(fileAsString.getBytes());
    }

    @Override
    public void flushEpub(final String outputFilePath) throws IOException {
        LOGGER.info("Attempting to write converted ePUB to disk ... [{}]", outputFilePath);
        EpubWriter epubWriter = new EpubWriter();
        boolean append = false;
        FileOutputStream out = new FileOutputStream(outputFilePath, append);
        epubWriter.write(bookIn, out);
        LOGGER.info("Successfully wrote converted ePUB [{}]", outputFilePath);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        bookIn = epubReader.readEpub(new FileInputStream(epubFile));
    }
}
