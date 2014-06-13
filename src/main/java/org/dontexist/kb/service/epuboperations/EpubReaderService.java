package org.dontexist.kb.service.epuboperations;

import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Service for performing basic I/O on ePUB files.
 */
public interface EpubReaderService {

    /**
     * Reads the ePUB file currently set to the service.
     * @return map of relative path string keys and file as one string values
     * @throws IOException
     */
    Map<String, String> openEpubFindingTextHtmlFiles() throws IOException;

    /**
     * Writes a single epub file's changes
     * @param fileAsString the new body of the file
     * @param hrefLocation the file being updated
     * @throws IOException
     */
    void writeEpubPage(String fileAsString, String hrefLocation) throws IOException;

    /**
     * Commits the epubFile to disk
     * @param outputFilePath destination, will be created
     * @throws IOException
     */
    void flushEpub(String outputFilePath) throws IOException;

}
