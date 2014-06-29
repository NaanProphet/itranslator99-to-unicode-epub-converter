package org.dontexist.kb.service.epuboperations;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Uses the Epubcheck library to validate ePUB documents. Compatible with ePUB2 and ePUB3.
 */
public interface EpubValidationService {

    /**
     * Validates an ePUB file. Generates a report text file using the given destination.
     *
     * @param epubFile              the ePUB file to validate.
     * @param reportFileDestination the destination to create the report file
     * @return true if ePUB was valid, false otherwise
     */
    boolean validate(File epubFile, File reportFileDestination) throws FileNotFoundException;

}
