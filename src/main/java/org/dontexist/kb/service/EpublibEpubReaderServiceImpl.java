package org.dontexist.kb.service;

import net.lingala.zip4j.exception.ZipException;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EpublibEpubReaderServiceImpl extends ParentEpubReaderServiceImpl {

    private static final String EPUB_TEXT_FOLDER = "text";
    private static final String EPUB_FILE_ENCODING = "UTF-8";

    // FIXME bad state
    private Book bookIn;

    public Map<String, String> openEpubFindingTextHtmlFiles(final File epubFile) throws IOException {
        Map<String, String> epubPagesMap = new HashMap<String, String>();
        bookIn = new EpubReader().readEpub(new FileInputStream(epubFile));
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

    public void writeEpubPage(final String fileAsString, final String hrefLocation, final File epubFile) throws IOException {
        // FIXME assumes file is the same as state!
        Resource pageToUpdate = bookIn.getResources().getByHref(hrefLocation);
        pageToUpdate.setData(fileAsString.getBytes());
    }

    public void flushEpub(File outputFile) throws IOException {
        // FIXME assumes file is same as state!
        EpubWriter epubWriter = new EpubWriter();
        FileOutputStream out = new FileOutputStream(outputFile);
        epubWriter.write(bookIn, out);
    }
}
