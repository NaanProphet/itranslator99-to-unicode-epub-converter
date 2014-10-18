package org.dontexist.kb.service.epuboperations;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Resources;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;
import org.apache.commons.io.IOUtils;
import org.dontexist.kb.swing.TextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Performs I/O operations on ePUBs by using the epublib Java library. <p>Note: currently, while epublib can open both
 * ePUB 2 and ePUB 3 files, it will save all as ePUB 2.</p>
 */
@Service
@Scope("prototype")
public class EpublibEpubReaderServiceImpl implements EpubReaderService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpublibEpubReaderServiceImpl.class);

    private static final String EPUB2_TEXT_FOLDER = "OEBPS/Text";
    private static final String EPUB3_TEXT_FOLDER = "text";
    private static final String EPUB_FILE_ENCODING = "UTF-8";

    private final EpubReader epubReader = new EpubReader();
    private final File epubFile;
    private /*final*/ Book bookIn;

    public EpublibEpubReaderServiceImpl(final File epubFile) throws IOException {
        this.epubFile = epubFile;
    }

    @Override
    public Map<String, String> openEpubFindingTextHtmlFiles() throws IOException {
        final Map<String, String> epubPagesMap = new HashMap<String, String>();
        final List<Resource> contents = bookIn.getContents();
        for (final Resource page : contents) {
            final String qualifiedFilename = page.getHref();
            final MediaType mediaType = page.getMediaType();
            // see MediatypeService for list of allowable MediaType s
            LOGGER.debug("qualifiedFilename is [{}] and mediaType is [{}]", qualifiedFilename, mediaType);
            if (qualifiedFilename.contains(EPUB3_TEXT_FOLDER)) {
                final InputStream myInputStream = page.getInputStream();
                final String pageAsString = IOUtils.toString(myInputStream, EPUB_FILE_ENCODING);
                epubPagesMap.put(page.getHref(), pageAsString);
            }
        }
        return epubPagesMap;
    }

    @Override
    public List<Resource> findCssHrefs() {
        // TODO create test
        Resources allFiles = bookIn.getResources();
        List<Resource> cssFiles = new ArrayList<>();
        for (Resource epubPage : allFiles.getAll()) {
            final String href = epubPage.getHref();
            if (href.endsWith(".css")) {
                LOGGER.debug("Adding page to list of css files [{}]", href);
                cssFiles.add(epubPage);
            }
        }
        return cssFiles;
    }

    @Override
    public Resource findStylesheetCss() {
        // TODO create test
        Resources allFiles = bookIn.getResources();
        List<Resource> cssFiles = new ArrayList<>();
        for (Resource epubPage : allFiles.getAll()) {
            final String href = epubPage.getHref();
            if (href.endsWith("stylesheet.css")) {
                return epubPage;
            }
        }
        throw new IllegalStateException("Could not find stylesheet.css file inside ePUB!");
    }

    @Override
    public void letUserEditFiles(final Collection<Resource> filesToEdit) throws IOException {
        // TODO create test

        for (Resource fileToEdit : filesToEdit) {
            final String href = fileToEdit.getHref();
            Resource page = bookIn.getResources().getByHref(href);
            final InputStream myInputStream = page.getInputStream();
            final String pageAsString = IOUtils.toString(myInputStream, EPUB_FILE_ENCODING);
            final String editedText = editFileText(href, pageAsString);
            if (editedText != null) {
                LOGGER.info("User successfully edited file [{}] to [{}]", fileToEdit, editedText);
                page.setData(editedText.getBytes());
            } else {
                LOGGER.warn("User did not edit file [{}]");
            }
        }

    }

    @Override
    public String editFileText(final String windowTitle, final String originalFileContent) throws IOException {
        final CountDownLatch latch = new CountDownLatch(1);

        final TextEditor textEditor = new TextEditor(windowTitle, originalFileContent, latch);
        TextEditor.askUserToEditText(textEditor);
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Interrupted while editing file!", e);
        }
        final String submittedText = textEditor.getSubmittedText();
        LOGGER.info("Submitted text is [{}]", submittedText);
        return submittedText;
    }


    @Override
    public void writeEpubPage(final String fileAsString, final String hrefLocation) throws IOException {
        final Resource pageToUpdate = bookIn.getResources().getByHref(hrefLocation);
        pageToUpdate.setData(fileAsString.getBytes());
    }

    @Override
    public void flushEpub(final String outputFilePath) throws IOException {
        LOGGER.info("Attempting to write converted ePUB to disk ... [{}]", outputFilePath);
        final EpubWriter epubWriter = new EpubWriter();
        boolean append = false;
        final FileOutputStream out = new FileOutputStream(outputFilePath, append);
        epubWriter.write(bookIn, out);
        LOGGER.info("Successfully wrote converted ePUB [{}]", outputFilePath);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        bookIn = epubReader.readEpub(new FileInputStream(epubFile));
    }
}
