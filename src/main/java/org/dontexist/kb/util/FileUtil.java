package org.dontexist.kb.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;

/**
 * Basic I/O File operations
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
    }

    public static Collection<File> drillDownFolderForExtension(final File folder, final boolean isRecursive, final String... extensions) {
        @SuppressWarnings("unchecked")
        Collection<File> filesToConvert = FileUtils.listFiles(folder, extensions, isRecursive);
        LOGGER.debug("Found in folder [{}] files [{}]", folder, filesToConvert);
        return filesToConvert;
    }
}
