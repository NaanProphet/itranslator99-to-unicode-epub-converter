package org.dontexist.kb.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://stackoverflow.com/questions/8131376/java-code-for-building-epub-is-not
 * -working
 */
public class ToEpub {
	
	private static final String EPUB_MIME_CONTENT = "application/epub+zip";


	private static final String UTF8_CHARSET = "UTF-8";


	private static final Logger LOGGER = LoggerFactory.getLogger(ToEpub.class);


	private static String metaFolder = "META-INF";
	private static String oebpsFolder = "";
	private static String containerFile = "container.xml";
	private static String contentPpfFile = "content.opf";
	private static String tocFile = "toc.ncx";
	private static String baseFileName;
	private static String baseFileNameHTML;
	private String baseFilePath;
	private ZipOutputStream zip;
	private String baseFileZipPath;

	public ToEpub(final String fileName) throws IOException {
		File file = new File(fileName);
		baseFileName = file.getName();
		baseFilePath = file.getPath();
		baseFileNameHTML = baseFileName.substring(0, baseFileName.lastIndexOf('.')) + ".html";
		baseFileZipPath = baseFilePath.substring(0, baseFilePath.lastIndexOf('.')) + ".epub";
		zip = new ZipOutputStream(new FileOutputStream(baseFileZipPath));

	}

	public void start() throws IOException {

		createMimeFile();
		createMeta();
		readConnent();
		createContentList();
		createTocFile();

		zip.close();
		/*
		 * Reading Creating Zip again
		 */
		ZipFile zfile = new ZipFile(new File(baseFileZipPath));
		LOGGER.debug(zfile.size() + " Files");
		Enumeration<? extends ZipEntry> itr = zfile.entries();
		while (itr.hasMoreElements()) {
			ZipEntry entry = itr.nextElement();
			LOGGER.debug(entry.getName() + " Size: " + entry.getCompressedSize() + "/" + entry.getSize() + " CRC: " + entry.getCrc());

		}
	}

	public void createMimeFile() throws IOException {
		byte[] content = EPUB_MIME_CONTENT.getBytes(UTF8_CHARSET);
		ZipEntry entry = new ZipEntry("mimetype");
		entry.setMethod(ZipEntry.STORED);
		entry.setSize(20);
		entry.setCompressedSize(20);
		entry.setCrc(0x2CAB616F); // pre-computed
		zip.putNextEntry(entry);

		zip.write(content);
		zip.closeEntry();

	}

	private void writeMimeType(ZipOutputStream zip) throws IOException {
		byte[] content = EPUB_MIME_CONTENT.getBytes(UTF8_CHARSET);
		ZipEntry entry = new ZipEntry("mimetype");
		entry.setMethod(ZipEntry.STORED);
		entry.setSize(20);
		entry.setCompressedSize(20);
		entry.setCrc(0x2CAB616F); // pre-computed
		zip.putNextEntry(entry);
		zip.write(content);
		zip.closeEntry();
	}

	public void readConnent() throws IOException {
		String thisLine;
		FileReader fr = new FileReader(baseFilePath);
		BufferedReader bReader = new BufferedReader(fr);
		String outFile = oebpsFolder + File.separator + baseFileNameHTML;
		ZipEntry entry = new ZipEntry(baseFileNameHTML);
		zip.putNextEntry(entry);
		zip.write("<?xml version='1.0' encoding='utf-8'?>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n".getBytes(UTF8_CHARSET));
		while ((thisLine = bReader.readLine()) != null) {
			zip.write(("<p>" + thisLine + "</p>\r\n").getBytes(UTF8_CHARSET));
		}
		zip.write("</html>".getBytes(UTF8_CHARSET));
		zip.closeEntry();
	}

	public void createContentList() throws IOException {
		ZipEntry entry = new ZipEntry(contentPpfFile);
		zip.putNextEntry(entry);
		zip.write("<?xml version='1.0' encoding='utf-8'?>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<package xmlns=\"http://www.idpf.org/2007/opf\" version=\"2.0\" unique-identifier=\"uuid_id\">".getBytes(UTF8_CHARSET));
		zip.write("<metadata xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:opf=\"http://www.idpf.org/2007/opf\" xmlns:dcterms=\"http://purl.org/dc/terms/\" xmlns:calibre=\"http://calibre.kovidgoyal.net/2009/metadata\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\r\n"
				.getBytes(UTF8_CHARSET));
		zip.write("<dc:language>en</dc:language>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<dc:creator opf:role=\"aut\">Open Mobile Alliance</dc:creator>\r\n".getBytes(UTF8_CHARSET));

		zip.write("<meta name=\"calibre:timestamp\" content=\"2011-11-11T15:02:14.075083+00:00\"/>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<dc:title>OMA Specification</dc:title>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<dc:contributor opf:role=\"bkp\">calibre (0.8.22) [http://calibre-ebook.com]</dc:contributor>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<dc:identifier id=\"uuid_id\" opf:scheme=\"uuid\">22fb9171-78bd-4bd1-89f8-7cae3a497c7c</dc:identifier>\r\n"
				.getBytes(UTF8_CHARSET));
		zip.write("<dc:subject>Template</dc:subject>\r\n".getBytes(UTF8_CHARSET));
		zip.write("</metadata>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<manifest>\r\n".getBytes(UTF8_CHARSET));
		zip.write(("<item href=\"" + baseFileNameHTML + "\" id=\"id1\" media-type=\"application/xhtml+xml\"/>\r\n").getBytes(UTF8_CHARSET));
		zip.write("<item href=\"toc.ncx\" media-type=\"application/x-dtbncx+xml\" id=\"ncx\"/>\r\n".getBytes(UTF8_CHARSET));
		zip.write("</manifest>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<spine toc=\"ncx\">\r\n".getBytes(UTF8_CHARSET));
		zip.write("<itemref idref=\"id1\"/>\r\n".getBytes(UTF8_CHARSET));
		zip.write("</spine>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<guide/>\r\n".getBytes(UTF8_CHARSET));
		zip.write("</package>\r\n".getBytes(UTF8_CHARSET));
		zip.closeEntry();
	}

	public void createTocFile() throws IOException {
		ZipEntry entry = new ZipEntry(tocFile);
		zip.putNextEntry(entry);
		zip.write("<?xml version=\"1.0\"?>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\r\n".getBytes(UTF8_CHARSET));
		zip.write("<rootfiles>".getBytes(UTF8_CHARSET));

		zip.write(("<rootfile full-path=\"" + contentPpfFile + "\" media-type=\"application/oebps-package+xml\"/>\r\n").getBytes(UTF8_CHARSET));

		zip.write("</rootfiles>\r\n".getBytes(UTF8_CHARSET));
		zip.write("</container>".getBytes(UTF8_CHARSET));
		zip.closeEntry();
	}

	public void createMeta() throws IOException {

		ZipEntry entry = new ZipEntry(metaFolder + File.separator + containerFile);

		zip.putNextEntry(entry);
		zip.write("<?xml version=\"1.0\"?>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\r\n".getBytes(UTF8_CHARSET));
		zip.write("<rootfiles>\r\n".getBytes(UTF8_CHARSET));
		zip.write("<rootfile full-path=\"content.opf\" media-type=\"application/oebps-package+xml\"/>\r\n".getBytes(UTF8_CHARSET));
		zip.write("</rootfiles>\r\n".getBytes(UTF8_CHARSET));
		zip.write("</container>\r\n".getBytes(UTF8_CHARSET));
		zip.closeEntry();
	}

	public void createMetaInfo() throws IOException {
		ZipEntry entry = new ZipEntry(metaFolder + File.separator);
		zip.putNextEntry(entry);
		zip.closeEntry();
	}

}