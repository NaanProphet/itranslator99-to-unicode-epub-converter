package org.dontexist.kb.integration;

import junitx.framework.FileAssert;
import org.apache.commons.io.FileUtils;
import org.dontexist.kb.SpringDriver;
import org.dontexist.kb.service.converter.UnicodeConverterHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

abstract class AbstractEpubConverterIT {

    @Autowired
    protected SpringDriver springDriver;

    @Autowired
    protected UnicodeConverterHelper unicodeConverterHelper;

    // --------------- HELPER METHODS -----------------

    protected void verify(File inputfile, File actualOutputFile, File expectedOutputFile) throws IOException, Exception {
        actualOutputFile.deleteOnExit();
        StringBuilder convertedFileAsString = unicodeConverterHelper.convertFileAsOneStringToUnicode(FileUtils.readFileToString(inputfile));
        FileUtils.writeStringToFile(actualOutputFile, convertedFileAsString.toString());
        FileAssert.assertEquals(expectedOutputFile, actualOutputFile);
    }

}
