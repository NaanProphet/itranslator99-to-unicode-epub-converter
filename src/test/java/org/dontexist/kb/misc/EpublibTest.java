package org.dontexist.kb.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import junit.framework.Assert;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Resources;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class EpublibTest {

    private static final String EPUB_TEXT_FOLDER = "text";
    private static final String EPUB_FILE_ENCODING = "UTF-8";
    public static final String EPUB_FOLDER = "src/test/resources/org/dontexist/kb/";
    public static final String FILENAME_IN = EPUB_FOLDER + "test.epub";
    public static final String FILENAME_OUT = EPUB_FOLDER + "output.test.epub";

    private EpubReader epubReader;
    private EpubWriter epubWriter;

    @Before
    public void setUp() {
        // delete output file if exists
        File outputFile = new File(FILENAME_OUT);
        outputFile.delete();

        // comment out deleteOnExit to keep output file for verification
        outputFile.deleteOnExit();

        epubReader = new EpubReader();
        epubWriter = new EpubWriter();
    }

    @Test
    public void testReadWriteNoModification() throws Exception {
        Book bookIn = epubReader.readEpub(new FileInputStream(FILENAME_IN));
        FileOutputStream fosOut = new FileOutputStream(FILENAME_OUT);
        epubWriter.write(bookIn, fosOut);
        Assert.assertTrue("EpubWriter didn't actually write the output file!", new File(FILENAME_OUT).exists());
    }

    @Test
    public void testReadAndWriteWithModification() throws Exception {
        EpubReader epubReader = new EpubReader();
        Book bookIn = epubReader.readEpub(new FileInputStream(FILENAME_IN));
        List<Resource> x = bookIn.getContents();
        Resource css = bookIn.getResources().getById("css");
        String cssFileText = IOUtils.toString(css.getInputStream());
        Resource pageCss = bookIn.getResources().getById("page_css");
        for (Resource ithX : x) {
            String qualifiedFilename = ithX.getHref();
            MediaType mediaType = ithX.getMediaType();
            // see MediatypeService for list of allowable MediaType s
            System.out.println(qualifiedFilename + " of media type " + mediaType);
            if (qualifiedFilename.contains(EPUB_TEXT_FOLDER)) {
                InputStream myInputStream = ithX.getInputStream();
                String myString = IOUtils.toString(myInputStream, EPUB_FILE_ENCODING);
                System.out.println(myString);
                ithX.setData("hi".getBytes());
            }
        }

        // Create EpubWriter
        EpubWriter epubWriter = new EpubWriter();

        // Write the Book as Epub
        FileOutputStream out = new FileOutputStream("src/test/java/org/dontexist/kb/misc/test1_book1.epub");
        epubWriter.write(bookIn, out);

        System.out.println(bookIn);
    }

    private void writeMimeType(ZipOutputStream zip) throws IOException {
        byte[] content = "application/epub+zip".getBytes(EPUB_FILE_ENCODING);
        ZipEntry entry = new ZipEntry("mimetype");
        entry.setMethod(ZipEntry.STORED);
        entry.setSize(20);
        entry.setCompressedSize(20);
        entry.setCrc(0x2CAB616F); // pre-computed
        zip.putNextEntry(entry);
        zip.write(content);
        zip.closeEntry();
    }

}
