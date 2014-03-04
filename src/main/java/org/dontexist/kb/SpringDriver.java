package org.dontexist.kb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.dontexist.kb.converter.PalladioIT2UnicodeConverter;
import org.dontexist.kb.converter.Sanskrit99ToUnicodeConverter;
import org.dontexist.kb.util.CreateZipFileDirectoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

public class SpringDriver implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDriver.class);

	@Value("${sanskrit99.span.class}")
	private String sanskrit99SpanClassCsv;
	private Set<String> sanskrit99SpanClasses = new HashSet<String>();

	private final Sanskrit99ToUnicodeConverter san99Converter = new Sanskrit99ToUnicodeConverter();
	private final PalladioIT2UnicodeConverter palladioITConverter = new PalladioIT2UnicodeConverter();

	public void main() throws ZipException, IOException {
		LOGGER.debug("Reached SpringDriver!");

		// -------- SELECT INPUT FOLDER -----------

		File folder = askUserToSelectInputFolder();

		// --------- GET ALL FILES ---------

		Collection<File> epubsToConvert = drillDownFolderForAllEpubFiles(folder);

		for (File ithEpub : epubsToConvert) {

			// --------- UNZIP EPUB ----------

			String unzipFolderDestination = FilenameUtils.removeExtension(ithEpub.getAbsolutePath()) + "-unicode";
			File unzipDestinationFile = new File(unzipFolderDestination);
			if (unzipDestinationFile.exists()) {
				// deletes even if not empty
				FileUtils.deleteDirectory(unzipDestinationFile);
			}
			;

			// epubs are actually zips
			ZipFile zipFile = new ZipFile(ithEpub);
			// creates destination automatically
			zipFile.extractAll(unzipFolderDestination);

			// FIXME what if epubs are saved differently?
			final File textFolder = new File(unzipFolderDestination + "/text/");
			Collection<File> filesToConvert = drillDownFolderForAllHtmlFiles(textFolder);

			// --------- READ FILE, PARSING AND REPLACING CONTENT INSIDE <SPAN>
			// TAGS
			// ----------
			for (File ithFile : filesToConvert) {

				StringBuilder convertedFileAsString = convertFileToUnicode(ithFile);

				// overwrite file, since we created a new folder
				FileUtils.writeStringToFile(ithFile, convertedFileAsString.toString(), "UTF8");
			}

			// ---------- ZIP UP NEW EPUB ----------
			CreateZipFileDirectoryUtil.main(unzipFolderDestination + ".epub", unzipFolderDestination);
		}

	}

	private StringBuilder convertFileToUnicode(File ithFile) throws IOException {
		String stringy = FileUtils.readFileToString(ithFile, "UTF8");
		// do not escape html characters yet, because we need to split
		// based on tags
		StringBuilder outputStringy = new StringBuilder();
		int fromIndex = 0;
		while (true) {
			int spanStart = stringy.indexOf("<span", fromIndex);
			if (spanStart == -1) {
				// flush out rest of file and quit
				String restOfFile = stringy.substring(fromIndex);
				String convertedRestOfFile = palladioITConverter.convertHtmlBlock(restOfFile);
				outputStringy.append(convertedRestOfFile);
				break;
			}
			String partBeforeSpan = stringy.substring(fromIndex, spanStart);
			String convertedPartBeforeSpan = palladioITConverter.convertHtmlBlock(partBeforeSpan);
			outputStringy.append(convertedPartBeforeSpan);
			int spanEnd = stringy.indexOf("</span>", spanStart);

			int beginIndex = spanStart;
			int endIndex = spanEnd + "</span>".length();
			String spanString = stringy.substring(beginIndex, endIndex);

			// perform Unicode replacement logic
			String convertedSpanString = performUnicodeReplacementOnSpanBlock(spanString);

			// prepare for next iteration
			fromIndex = endIndex;

			// flush work
			outputStringy.append(convertedSpanString);
		}
		// add final newline at end to make files identical
		outputStringy.append("\n");
		return outputStringy;
	}

	private String performUnicodeReplacementOnSpanBlock(String spanString) {
		String convertedSpanString;

		// allow both santext, santext1, etc. to be replaced
		boolean isSpanStringSanskrit = false; // default
		for (String validSanskritSpanClass : sanskrit99SpanClasses) {
			if (StringUtils.contains(spanString, "class=\"" + validSanskritSpanClass + "\"")) {
				isSpanStringSanskrit = true;
				break;
			}
		}

		if (isSpanStringSanskrit) {
			convertedSpanString = san99Converter.convertHtmlBlock(spanString);
		} else {
			convertedSpanString = palladioITConverter.convertHtmlBlock(spanString);
		}
		return convertedSpanString;
	}

	private Collection<File> drillDownFolderForAllEpubFiles(File folder) {
		String[] extensions = { "epub" };
		boolean recursive = true;
		@SuppressWarnings("unchecked")
		Collection<File> filesToConvert = FileUtils.listFiles(folder, extensions, recursive);
		return filesToConvert;
	}

	private Collection<File> drillDownFolderForAllHtmlFiles(File folder) {
		String[] extensions = { "html" };
		boolean recursive = true;
		@SuppressWarnings("unchecked")
		Collection<File> filesToConvert = FileUtils.listFiles(folder, extensions, recursive);
		return filesToConvert;
	}

	private File askUserToSelectInputFolder() {
		JFileChooser f = new JFileChooser();
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.showOpenDialog(null);
		// actually gets the folder. "file" refers to File class
		return f.getSelectedFile();
	}

	public void afterPropertiesSet() {
		String[] spanClasses = StringUtils.split(sanskrit99SpanClassCsv, ",");
		for (String ithSpanClass : spanClasses) {
			sanskrit99SpanClasses.add(ithSpanClass);
		}

	}

}
