package org.dontexist.kb.service;

import net.lingala.zip4j.exception.ZipException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

//@Service
public class EpublibEpubReaderServiceImpl extends ParentEpubReaderServiceImpl {
    @Override
    public Map<File, String> openEpubFindingTextHtmlFiles(File ithEpub, String unzipPath) throws IOException, ZipException {
        return null;
    }
}
