package org.dontexist.kb.service;

import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface EpubReaderService extends InitializingBean {

//    Map<File, String> openEpubFindingTextHtmlFiles(File ithEpub, String unzipPath) throws IOException, ZipException;

    Collection<File> drillDownFolderForExtension(File folder, boolean isRecursive, String... extensions);

    StringBuilder convertFileAsOneStringToUnicode(String entireFileAsOneString) throws IOException;
}
