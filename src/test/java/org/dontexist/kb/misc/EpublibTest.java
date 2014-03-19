package org.dontexist.kb.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import junit.framework.Assert;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
	public void test() throws Exception {
		EpubReader epubReader = new EpubReader();
		Book bookIn = epubReader.readEpub(new FileInputStream(FILENAME_IN));
		List<Resource> x = bookIn.getContents();
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
		System.out.println("hi");
		// FileOutputStream fos = new FileOutputStream(zipFile);
		// ZipOutputStream zos = new ZipOutputStream(fos);
		//
		// System.out.println("Output to Zip : " + zipFile);
		// writeMimeType(zos);
		// ZipEntry container = new ZipEntry("META-INF\\container.xml");
		// zos.putNextEntry(container);
		// FileInputStream inMime2 = new FileInputStream(SOURCE_FOLDER +
		// File.separator + "META-INF\\container.xml");
		// int len2;
		// while((len2 = inMime2.read(buffer)) > 0){
		// zos.write(buffer, 0, len2);
		// }
		// inMime2.close();
		// for(String file : this.fileList){
		// if(!file.toString().equals("mimetype") &&
		// !file.toString().equals("META-INF\\container.xml")){
		// System.out.println("File Added : " + file);
		// ZipEntry ze= new ZipEntry(file);
		// zos.putNextEntry(ze);
		//
		// FileInputStream in =
		// new FileInputStream(SOURCE_FOLDER + File.separator + file);
		//
		// int len;
		// while ((len = in.read(buffer)) > 0) {
		// zos.write(buffer, 0, len);
		// }
		//
		// in.close();
		// }
		// }
		//
		// zos.closeEntry();
		// zos.close();
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
