package org.dontexist.kb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;

import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.dontexist.kb.service.EpubReaderServiceImpl;
import org.dontexist.kb.util.CreateZipFileDirectoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class SpringDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDriver.class);

    @Value("${delete.temp.extracted.folder}")
    private boolean isDeleteTempFiles;

    @Autowired
    private EpubReaderServiceImpl epubReaderService;

    public void main() throws ZipException, IOException {
        LOGGER.debug("Reached SpringDriver!");
        File folder = askUserToSelectInputFolder();
        Collection<File> epubsToConvert = epubReaderService.drillDownFolderForExtension(folder, "epub");
        convertEpub(epubsToConvert);
    }

    private void convertEpub(Collection<File> epubsToConvert) throws ZipException, IOException {
        for (File ithEpub : epubsToConvert) {

            Map<File, String> filesAsStringToConvert = epubReaderService.unzipEpubFindingTextHtmlFiles(ithEpub,
                    unzippedEpubFolderPath(ithEpub));

            for (Entry<File, String> entry : filesAsStringToConvert.entrySet()) {
                File ithFile = entry.getKey();
                String ithFileAsOneString = entry.getValue();
                StringBuilder convertedFileAsString = epubReaderService.convertFileAsOneStringToUnicode(ithFileAsOneString);

                // overwrite file, since we created a new folder
                FileUtils.writeStringToFile(ithFile, convertedFileAsString.toString(), "UTF8");
            }

            // ---------- ZIP UP NEW EPUB ----------
            CreateZipFileDirectoryUtils.main(unzippedEpubFolderPath(ithEpub) + ".epub", unzippedEpubFolderPath(ithEpub));

            // ---------- CLEANUP TEMP FOLDER ------------
            if (isDeleteTempFiles) {
                FileUtils.deleteDirectory(new File(unzippedEpubFolderPath(ithEpub)));
            }
        }
    }

    private String unzippedEpubFolderPath(File ithEpub) {
        final String unzipFolderDestination = FilenameUtils.removeExtension(ithEpub.getAbsolutePath()) + "-unicode";
        return unzipFolderDestination;
    }

    private File askUserToSelectInputFolder() {
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showOpenDialog(null);
        // actually gets the folder. "file" refers to File class
        return f.getSelectedFile();
    }

}
