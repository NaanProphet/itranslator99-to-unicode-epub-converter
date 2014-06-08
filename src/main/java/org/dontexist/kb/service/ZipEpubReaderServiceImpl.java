package org.dontexist.kb.service;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dontexist.kb.service.converter.PalladioIT2UnicodeConverterServiceImpl;
import org.dontexist.kb.service.converter.Sanskrit99ToUnicodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ZipEpubReaderServiceImpl extends ParentEpubReaderServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipEpubReaderServiceImpl.class);

    @Override
    public Map<File, String> openEpubFindingTextHtmlFiles(final File ithEpub, final String unzipPath) throws IOException, ZipException {
        // epubs are actually zips with special metadata
        unzipFile(ithEpub, unzipPath);

        final File textFolder = new File(unzipPath + "/text/");
        if (!textFolder.exists()) {
            // TODO what if epubs are saved differently?
            throw new IllegalStateException(String.format("Unexpected epub format! Cannot find folder [%s]", textFolder));
        }
        Collection<File> filesToConvert = drillDownFolderForExtension(textFolder, true, "html");

        Map<File, String> filesAsStringToConvert = new HashMap<File, String>();

        for (File ithFile : filesToConvert) {
            String ithFileAsOneString = FileUtils.readFileToString(ithFile);
            filesAsStringToConvert.put(ithFile, ithFileAsOneString);

        }
        return filesAsStringToConvert;
    }

    private File unzipFile(final File file, final String unzipFolderDestination) throws IOException, ZipException {
        File unzipDestinationFolder = new File(unzipFolderDestination);
        if (unzipDestinationFolder.exists()) {
            // deletes even if not empty
            FileUtils.deleteDirectory(unzipDestinationFolder);
        }

        ZipFile zipFile = new ZipFile(file);
        // creates destination automatically
        zipFile.extractAll(unzipFolderDestination);
        return unzipDestinationFolder;
    }

}
