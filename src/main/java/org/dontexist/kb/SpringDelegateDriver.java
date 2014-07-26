package org.dontexist.kb;

import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FilenameUtils;
import org.dontexist.kb.service.epuboperations.EpubReaderService;
import org.dontexist.kb.service.epuboperations.EpubReaderServiceFactory;
import org.dontexist.kb.service.converter.UnicodeConverterHelper;
import org.dontexist.kb.service.epuboperations.EpubValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import static org.dontexist.kb.util.FileUtil.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class SpringDelegateDriver implements ActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDelegateDriver.class);
    private final CountDownLatch userSelectionLatch = new CountDownLatch(1);
    @Value("${delete.temp.extracted.folder}")
    private boolean isDeleteTempFiles;
    private EpubSelectGui.PromptOptions optionSelected;
    @Autowired
    private EpubReaderServiceFactory epubReaderServiceFactory;
    @Autowired
    private UnicodeConverterHelper unicodeConverterHelper;
    @Autowired
    private EpubValidationService epubValidationService;

    public void main() throws ZipException, IOException {
        LOGGER.debug("Reached SpringDelegateDriver!");
        EpubSelectGui.PromptOptions optionSelected = askUserToSelectConversionType();
        Collection<File> epubsToConvert = fetchEpubsToConvert(optionSelected);
        convertEpub(epubsToConvert);
    }

    private Collection<File> fetchEpubsToConvert(final EpubSelectGui.PromptOptions optionSelected) {
        if (optionSelected == EpubSelectGui.PromptOptions.ALL_FILES_IN_FOLDER_RECURSIVELY) {
            File folder = askUserToSelectInputFolder();
            return drillDownFolderForExtension(folder, true, "epub");
        } else if (optionSelected == EpubSelectGui.PromptOptions.ALL_FILES_IN_A_SINGLE_FOLDER) {
            File folder = askUserToSelectInputFolder();
            return drillDownFolderForExtension(folder, false, "epub");
        } else if (optionSelected == EpubSelectGui.PromptOptions.SINGLE_FILE) {
            File file = askUserToSelectInputFile();
            Collection<File> returnMe = new ArrayList<File>();
            returnMe.add(file);
            return returnMe;
        } else {
            throw new UnsupportedOperationException("Do not know how to handle case for " + optionSelected);
        }
    }

    private void convertEpub(final Collection<File> epubsToConvert) throws ZipException, IOException {
        for (File ithEpub : epubsToConvert) {
            // prototype service, based on file
            final EpubReaderService epubReaderService = epubReaderServiceFactory.createEpubReaderService(ithEpub);
            final Map<String, String> filesAsStringToConvert = epubReaderService.openEpubFindingTextHtmlFiles();

            for (Map.Entry<String, String> entry : filesAsStringToConvert.entrySet()) {
                final String ithHref = entry.getKey();
                final String ithFileAsOneString = entry.getValue();
                final StringBuilder convertedFileAsString = unicodeConverterHelper.convertFileAsOneStringToUnicode(ithFileAsOneString);

                epubReaderService.writeEpubPage(convertedFileAsString.toString(), ithHref);
            }
            final String outputFilePath = ithEpub.getParent() + "/" + FilenameUtils.getBaseName(ithEpub.getAbsolutePath()) + "-unicode.epub";
            epubReaderService.flushEpub(outputFilePath);
            // validate output ePUB
            // TODO inject report suffix via property
            epubValidationService.validate(new File(outputFilePath), new File(outputFilePath + "report.txt"));
        }
    }

    private File askUserToSelectInputFolder() {
        JFileChooser f = new JFileChooser();
        f.setDialogTitle("Please select a folder");
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showOpenDialog(null);
        // actually gets the folder. "file" refers to File class
        return f.getSelectedFile();
    }

    private File askUserToSelectInputFile() {
        JFileChooser f = new JFileChooser();
        f.setDialogTitle("Please select an ePUB file");
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        f.showOpenDialog(null);
        return f.getSelectedFile();
    }

    private EpubSelectGui.PromptOptions askUserToSelectConversionType() {
        // prints dialog
        EpubSelectGui.displayDialog(this);
        try {
            userSelectionLatch.await();
        } catch (final InterruptedException e) {
            throw new IllegalStateException("Cannot continue! Interrupted while waiting for user input", e);
        }
        return optionSelected;
    }

    @Override
    public void actionPerformed(final ActionEvent eventUserSelected) {
        LOGGER.debug("Received action event! [{}}", eventUserSelected);
        final String actionCommand = eventUserSelected.getActionCommand();
        optionSelected = EpubSelectGui.PromptOptions.valueOf(actionCommand);
        userSelectionLatch.countDown();
    }
}
