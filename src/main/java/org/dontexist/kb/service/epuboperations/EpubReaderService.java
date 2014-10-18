package org.dontexist.kb.service.epuboperations;

import net.lingala.zip4j.exception.ZipException;
import nl.siegmann.epublib.domain.Resource;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    List<nl.siegmann.epublib.domain.Resource> findCssHrefs();

    Resource findStylesheetCss();

    void letUserEditFiles(Collection<Resource> filesToEdit) throws IOException;

    String editFileText(String windowTitle, String originalFileContent) throws IOException;

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
