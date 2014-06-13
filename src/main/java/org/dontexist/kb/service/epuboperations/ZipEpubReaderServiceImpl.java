package org.dontexist.kb.service.epuboperations;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.dontexist.kb.util.CreateZipFileDirectoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Performs I/O operations on ePUBs by treating them as zip files. <p>Note: since ePUBs are unzipped and then re-zipped,
 * this service will preserve with both ePUB2 and ePUB3 formats.</p>
 */
@Service
@Scope("prototype")
public class ZipEpubReaderServiceImpl implements EpubReaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipEpubReaderServiceImpl.class);

    private final File epubFile;
    private final String unzipDestination;
    private boolean isDeleteTempFiles;

    public ZipEpubReaderServiceImpl(final File epubFile) {
        this.epubFile = epubFile;
        this.unzipDestination = epubFile.getAbsolutePath() + "-unicode";
    }

    private File unzipFile(final File file) throws IOException {
        File unzipDestinationFolder = new File(unzipDestination);
        if (unzipDestinationFolder.exists()) {
            // deletes even if not empty
            FileUtils.deleteDirectory(unzipDestinationFolder);
        }

        try {
            ZipFile zipFile = new ZipFile(file);
            // creates destination automatically
            zipFile.extractAll(unzipDestination);
        } catch (ZipException e) {
            // TODO will this swallow the exception?
            throw new IllegalStateException("Could not unzip file [" + epubFile + "]!", e);
        }
        return unzipDestinationFolder;
    }

    private Collection<File> drillDownFolderForExtension(final File folder, final boolean isRecursive, final String... extensions) {
        @SuppressWarnings("unchecked")
        Collection<File> filesToConvert = FileUtils.listFiles(folder, extensions, isRecursive);
        LOGGER.debug("Found in folder [{}] files [{}]", folder, filesToConvert);
        return filesToConvert;
    }

    @Override
    public Map<String, String> openEpubFindingTextHtmlFiles() throws IOException {
        // epubs are actually zips with special metadata
        unzipFile(epubFile);

        final File textFolder = new File(unzipDestination + "/text/");
        if (!textFolder.exists()) {
            // TODO the above assumes ePUB v3. if v2, then the folder is OEBPS/Text/
            throw new IllegalStateException(String.format("Unexpected epub format! Cannot find folder [%s]", textFolder));
        }
        Collection<File> filesToConvert = drillDownFolderForExtension(textFolder, true, "html");

        Map<String, String> filesAsStringToConvert = new HashMap<String, String>();

        for (File ithFile : filesToConvert) {
            String ithFileAsOneString = FileUtils.readFileToString(ithFile);
            filesAsStringToConvert.put(ithFile.getAbsolutePath(), ithFileAsOneString);

        }
        return filesAsStringToConvert;
    }

    @Override
    public void writeEpubPage(String fileAsString, String hrefLocation) throws IOException {

        // overwrite file, since we created a new folder
        File ithFile = new File(hrefLocation);
        FileUtils.writeStringToFile(ithFile, fileAsString.toString(), "UTF8");
    }

    @Override
    public void flushEpub(String outputFilePath) throws IOException {
        // ---------- ZIP UP NEW EPUB ----------
        CreateZipFileDirectoryUtils.main(unzippedEpubFolderPath(epubFile) + ".epub", unzippedEpubFolderPath(epubFile));
        // ---------- CLEANUP TEMP FOLDER ------------
        if (isDeleteTempFiles) {
            FileUtils.deleteDirectory(new File(unzippedEpubFolderPath(epubFile)));
        }
    }

    private String unzippedEpubFolderPath(File ithEpub) {
        final String unzipFolderDestination = FilenameUtils.removeExtension(ithEpub.getAbsolutePath()) + "-unicode";
        return unzipFolderDestination;
    }
}
