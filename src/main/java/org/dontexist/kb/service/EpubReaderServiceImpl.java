package org.dontexist.kb.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dontexist.kb.service.converter.PalladioIT2UnicodeConverterServiceImpl;
import org.dontexist.kb.service.converter.Sanskrit99ToUnicodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EpubReaderServiceImpl implements InitializingBean {

    private static final String SPAN_END_TAG = "</span>";

    private static final String SPAN_START_TAG = "<span";

    private static final Logger LOGGER = LoggerFactory.getLogger(EpubReaderServiceImpl.class);

    @Autowired
    private Sanskrit99ToUnicodeService san99Converter;

    @Autowired
    private PalladioIT2UnicodeConverterServiceImpl palladioITConverter;

    @Value("${sanskrit99.span.class}")
    private String sanskrit99SpanClassCsv;

    private Set<String> sanskrit99SpanClasses = new HashSet<String>();

    public Collection<File> drillDownFolderForExtension(File folder, String... extensions) {
        boolean recursive = true;
        @SuppressWarnings("unchecked")
        Collection<File> filesToConvert = FileUtils.listFiles(folder, extensions, recursive);
        LOGGER.debug("Found in folder [{}] files [{}]", folder, filesToConvert);
        return filesToConvert;
    }

    public Map<File, String> unzipEpubFindingTextHtmlFiles(final File ithEpub, final String unzipPath) throws IOException, ZipException {
        // epubs are actually zips with special metadata
        unzipFile(ithEpub, unzipPath);

        final File textFolder = new File(unzipPath + "/text/");
        if (!textFolder.exists()) {
            // TODO what if epubs are saved differently?
            throw new IllegalStateException(String.format("Unexpected epub format! Cannot find folder [%s]", textFolder));
        }
        Collection<File> filesToConvert = drillDownFolderForExtension(textFolder, "html");

        Map<File, String> filesAsStringToConvert = new HashMap<File, String>();

        // --------- READ FILE, PARSING AND REPLACING CONTENT INSIDE <SPAN>
        // TAGS
        // ----------

        for (File ithFile : filesToConvert) {
            String ithFileAsOneString = FileUtils.readFileToString(ithFile);
            filesAsStringToConvert.put(ithFile, ithFileAsOneString);

        }
        return filesAsStringToConvert;
    }

    private File unzipFile(final File file, final String unzipFolderDestination) throws IOException, ZipException {
        File unzipDestinationFolder = new File(unzipFolderDestination);
        if (unzipDestinationFolder.exists()) {
            // deletes even if not empty
            FileUtils.deleteDirectory(unzipDestinationFolder);
        }

        ZipFile zipFile = new ZipFile(file);
        // creates destination automatically
        zipFile.extractAll(unzipFolderDestination);
        return unzipDestinationFolder;
    }

    public StringBuilder convertFileAsOneStringToUnicode(final String entireFileAsOneString) throws IOException {
        // do not escape html characters yet, because we need to split
        // based on tags
        StringBuilder outputStringy = new StringBuilder();
        int fromIndex = 0;
        while (true) {
            int spanStart = entireFileAsOneString.indexOf(SPAN_START_TAG, fromIndex);
            if (spanStart == -1) {
                // flush out rest of file and quit
                String restOfFile = entireFileAsOneString.substring(fromIndex);
                String convertedRestOfFile = palladioITConverter.convertHtmlBlock(restOfFile);
                outputStringy.append(convertedRestOfFile);
                break;
            }
            String partBeforeSpan = entireFileAsOneString.substring(fromIndex, spanStart);
            String convertedPartBeforeSpan = palladioITConverter.convertHtmlBlock(partBeforeSpan);
            outputStringy.append(convertedPartBeforeSpan);
            int spanEnd = entireFileAsOneString.indexOf(SPAN_END_TAG, spanStart);

            int beginIndex = spanStart;
            int endIndex = spanEnd + SPAN_END_TAG.length();
            String spanString = entireFileAsOneString.substring(beginIndex, endIndex);

            // perform Unicode replacement logic
            String convertedSpanString = performUnicodeReplacementOnSpanBlock(spanString);

            // prepare for next iteration
            fromIndex = endIndex;

            // flush work
            outputStringy.append(convertedSpanString);
        }
        // add final newline at end to make files identical
        outputStringy.append("\n");
        return outputStringy;
    }

    private String performUnicodeReplacementOnSpanBlock(final String spanString) {
        String convertedSpanString;

        // allow both santext, santext1, etc. to be replaced
        boolean isSpanStringSanskrit = false; // default
        for (String validSanskritSpanClass : sanskrit99SpanClasses) {
            if (StringUtils.contains(spanString, "class=\"" + validSanskritSpanClass + "\"")) {
                isSpanStringSanskrit = true;
                break;
            }
        }

        if (isSpanStringSanskrit) {
            convertedSpanString = san99Converter.convertHtmlBlock(spanString);
        } else {
            convertedSpanString = palladioITConverter.convertHtmlBlock(spanString);
        }
        return convertedSpanString;
    }

    public void afterPropertiesSet() {
        addSanskritSpanClasses(sanskrit99SpanClassCsv);
    }

    public void addSanskritSpanClasses(final String csvString) {
        String[] spanClasses = StringUtils.split(csvString, ",");
        if (spanClasses == null) {
            return;
        }
        for (String ithSpanClass : spanClasses) {
            sanskrit99SpanClasses.add(ithSpanClass);
        }
    }

}
