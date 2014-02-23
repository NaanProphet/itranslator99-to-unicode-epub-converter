package org.dontexist.kb;

/*
 Create Zip File From Directory using ZipOutputStream Example
 This Java example shows how create zip file from directory
 using Java ZipOutputStream class.

 from: http://www.java-examples.com/create-zip-file-directory-using-zipoutputstream-example
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

public class CreateZipFileDirectory {

	private static String metaFolder = "META-INF";

	public static void main(final String zipFile, final String sourceDirectory) throws IOException {
		// String zipFile = "C:/FileIO/zipdemo.zip";
		// String sourceDirectory = "C:/examples";

		// create byte buffer
		byte[] buffer = new byte[1024];
		/*
		 * To create a zip file, use
		 * 
		 * ZipOutputStream(OutputStream out) constructor of ZipOutputStream
		 * class.
		 */

		// create object of FileOutputStream
		FileOutputStream fout = new FileOutputStream(zipFile);

		// create object of ZipOutputStream from FileOutputStream
		ZipOutputStream zout = new ZipOutputStream(fout);

		// write mimetype first as part of EPUB spec
		writeMimeType(zout);
		writeMeta(zout);

		// create File object from directory name
		File dir = new File(sourceDirectory);

		// check to see if this directory exists
		if (!dir.isDirectory()) {
			System.out.println(sourceDirectory + " is not a directory");
		} else {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					String folderName = StringUtils.remove(files[i].getName(), sourceDirectory);
					addFilesToZip(buffer, zout, files[i].listFiles(), folderName);
				} else {
					System.out.println(files[i].getName());
					addFileToZip(buffer, zout, files[i], "");
				}
			}

		}

		// close the ZipOutputStream
		zout.close();

		System.out.println("Zip file has been created!");

	}

	private static void writeMimeType(ZipOutputStream zip) throws IOException {
		byte[] content = "application/epub+zip".getBytes("UTF-8");
		ZipEntry entry = new ZipEntry("mimetype");
		entry.setMethod(ZipEntry.STORED);
		entry.setSize(20);
		entry.setCompressedSize(20);
		entry.setCrc(0x2CAB616F); // pre-computed
		zip.putNextEntry(entry);
		zip.write(content);
		zip.closeEntry();
		System.out.println("Successfully wrote mimetype");
	}
	
	private static void writeMeta(ZipOutputStream zip) throws UnsupportedEncodingException, IOException {
		ZipEntry entry = new ZipEntry(metaFolder+File.separator+"container.xml");
		zip.putNextEntry(entry);
	    zip.write("<?xml version=\"1.0\"?>\r\n".getBytes("UTF-8"));
	    zip.write("<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\r\n".getBytes("UTF-8"));
	    zip.write("<rootfiles>\r\n".getBytes("UTF-8"));
	    zip.write("<rootfile full-path=\"content.opf\" media-type=\"application/oebps-package+xml\"/>\r\n".getBytes("UTF-8"));
	    zip.write("</rootfiles>\r\n".getBytes("UTF-8"));
	    zip.write("</container>\r\n".getBytes("UTF-8"));
	    zip.closeEntry();
	}

	private static void addFilesToZip(byte[] buffer, ZipOutputStream zout, File[] files, String folderName) throws FileNotFoundException, IOException {
		for (int i = 0; i < files.length; i++) {
			addFileToZip(buffer, zout, files[i], folderName);
		}
	}

	private static void addFileToZip(byte[] buffer, ZipOutputStream zout, File file, String folderName) throws FileNotFoundException, IOException {
		if (StringUtils.contains(file.getName(), "mimetype") || StringUtils.contains(file.getName(), "container.xml")) {
			System.out.println("Skipping " + file.getName());
			return;
		}
		System.out.println("Adding " + file.getName());

		// create object of FileInputStream for source file
		FileInputStream fin = new FileInputStream(file);

		/*
		 * To begin writing ZipEntry in the zip file, use
		 * 
		 * void putNextEntry(ZipEntry entry) method of ZipOutputStream class.
		 * 
		 * This method begins writing a new Zip entry to the zip file and
		 * positions the stream to the start of the entry data.
		 */

		zout.putNextEntry(new ZipEntry(folderName + "/" + file.getName()));

		/*
		 * After creating entry in the zip file, actually write the file.
		 */
		int length;

		while ((length = fin.read(buffer)) > 0) {
			zout.write(buffer, 0, length);
		}

		/*
		 * After writing the file to ZipOutputStream, use
		 * 
		 * void closeEntry() method of ZipOutputStream class to close the
		 * current entry and position the stream to write the next entry.
		 */

		zout.closeEntry();

		// close the InputStream
		fin.close();
	}
}

/*
 * Output of this program would be Adding nonav.log Adding ospreg.exe Adding
 * servers.ini Adding setupisam.log Adding sourceFile1.doc Adding
 * sourceFile2.doc Zip file has been created!
 */

