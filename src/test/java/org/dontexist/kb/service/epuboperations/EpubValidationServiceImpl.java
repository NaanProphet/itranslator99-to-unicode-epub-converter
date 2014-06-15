package org.dontexist.kb.service.epuboperations;

import com.adobe.epubcheck.api.EpubCheck;

import java.io.*;

/**
 * Uses the Epubcheck library to validate ePUB documents. Compatible with ePUB2 and ePUB3.
 */
public class EpubValidationServiceImpl {

    /**
     * Validates an ePUB file. Generates a report text file using the given destination.
     *
     * @param epubFile              the ePUB file to validate.
     * @param reportFileDestination the destination to create the report file
     * @return true if ePUB was valid, false otherwise
     */
    public boolean validate(File epubFile, File reportFileDestination) throws FileNotFoundException {
        OutputStream outputStream = new FileOutputStream(reportFileDestination);
        PrintWriter writer = new PrintWriter(outputStream);
        writer.write("### Validation Report for " + epubFile + " ###\n");
        EpubCheck epubValidator = new EpubCheck(epubFile, writer);
        boolean isValid = epubValidator.validate();
        writer.flush();
        writer.close();
        return isValid;
    }
}
