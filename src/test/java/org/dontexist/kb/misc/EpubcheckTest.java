package org.dontexist.kb.misc;

import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.api.Report;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class EpubcheckTest {

    private static final String EPUB_FOLDER = "src/test/resources/org/dontexist/kb/";
    private static final String FILENAME_IN = EPUB_FOLDER + "test.epub";

    @Test
    public void epubCheckTest() throws Exception {
        File epubFile = new File(FILENAME_IN);
        Assert.isTrue(epubFile.exists());
        File reportFile = new File(FILENAME_IN + "report.txt");
        reportFile.delete();
        Assert.isTrue(!reportFile.exists());
        OutputStream outputStream = new FileOutputStream(reportFile);
        PrintWriter writer = new PrintWriter(outputStream);
        writer.write("### Validation Report for " + FILENAME_IN + " ###\n");
        EpubCheck epubValidator = new EpubCheck(epubFile, writer);
        epubValidator.validate();
        writer.flush();
        writer.close();
    }
}
