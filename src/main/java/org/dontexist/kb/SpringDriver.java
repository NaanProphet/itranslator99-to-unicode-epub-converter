package org.dontexist.kb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.dontexist.kb.service.EpubReaderService;
import org.dontexist.kb.util.CreateZipFileDirectoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class SpringDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDriver.class);

	@Value("${delete.temp.extracted.folder}")
	private boolean isDeleteExtractedEpubFolder;
	
	@Autowired
	private EpubReaderService epubReaderService;

	public void main() throws ZipException, IOException {
		LOGGER.debug("Reached SpringDriver!");

		// -------- SELECT INPUT FOLDER -----------

		File folder = askUserToSelectInputFolder();

		// --------- GET ALL FILES ---------

		Collection<File> epubsToConvert = epubReaderService.drillDownFolderForAllEpubFiles(folder);

		for (File ithEpub : epubsToConvert) {

			// --------- UNZIP EPUB ----------

			String unzipFolderDestination = FilenameUtils.removeExtension(ithEpub.getAbsolutePath()) + "-unicode";
			File unzipDestinationFolder = new File(unzipFolderDestination);
			if (unzipDestinationFolder.exists()) {
				// deletes even if not empty
				FileUtils.deleteDirectory(unzipDestinationFolder);
			}
			;

			// epubs are actually zips
			ZipFile zipFile = new ZipFile(ithEpub);
			// creates destination automatically
			zipFile.extractAll(unzipFolderDestination);

			// FIXME what if epubs are saved differently?
			final File textFolder = new File(unzipFolderDestination + "/text/");
			Collection<File> filesToConvert = epubReaderService.drillDownFolderForAllHtmlFiles(textFolder);

			// --------- READ FILE, PARSING AND REPLACING CONTENT INSIDE <SPAN>
			// TAGS
			// ----------
			
			Map<File, String> filesAsStringToConvert = new HashMap<File,String>();
			
			for (File ithFile : filesToConvert) {
				String ithFileAsOneString = FileUtils.readFileToString(ithFile);
				filesAsStringToConvert.put(ithFile, ithFileAsOneString);
				
			}
			
			for (File ithFile : filesAsStringToConvert.keySet()) {
				String ithFileAsOneString = filesAsStringToConvert.get(ithFile);
				StringBuilder convertedFileAsString = epubReaderService.convertFileAsOneStringToUnicode(ithFileAsOneString);

				// overwrite file, since we created a new folder
				FileUtils.writeStringToFile(ithFile, convertedFileAsString.toString(), "UTF8");
			}

			// ---------- ZIP UP NEW EPUB ----------
			CreateZipFileDirectoryUtils.main(unzipFolderDestination + ".epub", unzipFolderDestination);

			// ---------- CLEANUP TEMP FOLDER ------------
			if (isDeleteExtractedEpubFolder) {
				FileUtils.deleteDirectory(unzipDestinationFolder);
			}
		}

	}

	private File askUserToSelectInputFolder() {
		JFileChooser f = new JFileChooser();
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.showOpenDialog(null);
		// actually gets the folder. "file" refers to File class
		return f.getSelectedFile();
	}

}
