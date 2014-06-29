package org.dontexist.kb.service.epuboperations;

import com.adobe.epubcheck.api.EpubCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class EpubValidationServiceImpl implements EpubValidationService {

    private static final Logger logger = LoggerFactory.getLogger(EpubValidationServiceImpl.class);

    @Override
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
