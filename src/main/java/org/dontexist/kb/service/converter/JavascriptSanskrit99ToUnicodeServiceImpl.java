package org.dontexist.kb.service.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class JavascriptSanskrit99ToUnicodeServiceImpl extends AbstractText2UnicodeService implements Sanskrit99ToUnicodeService {

    private static final String JAVASCRIPT_METHOD_NAME = "convert_to_unicode";
    private static final String SANSKRIT99_TO_UNICODE_JAVASCRIPT_FILENAME = "/sanskrit99_to_unicode.js";
    private static final Logger LOGGER = LoggerFactory.getLogger(JavascriptSanskrit99ToUnicodeServiceImpl.class);

    private final ClassPathResource classPathResource = new ClassPathResource(SANSKRIT99_TO_UNICODE_JAVASCRIPT_FILENAME);

    @Override
    public String convert(final String input) {

        // initialize
        String output = null;

        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        Invocable inv = (Invocable) engine;

        try {
            File file = classPathResource.getFile(); // IOException
            Reader fileReader = new InputStreamReader(new FileInputStream(file), "UTF8"); // FileNotFoundException
            engine.eval(fileReader); // ScriptException
            output = (String) inv.invokeFunction(JAVASCRIPT_METHOD_NAME, input); // NoSuchMethodException

            // ------- YUCKY YUCKY. TODO UPGRADE TO JAVA 7 AND USE MULTI-CATCH
        } catch (ScriptException e) {
            String errorMsg = String.format("Catastrophic error! Problem loading Javascript file [%s]. Please check the file for errors.",
                    SANSKRIT99_TO_UNICODE_JAVASCRIPT_FILENAME) + "\n" + ExceptionUtils.getStackTrace(e);
            LOGGER.error(errorMsg);
            throw new IllegalStateException(errorMsg, e);
        } catch (NoSuchMethodException e) {
            String errorMsg = String
                    .format("Catastrophic error! Cannot find method [%s] on the Javascript file [%s]. Cannot continue; please check if the file is corrupt or has recently changed.",
                            JAVASCRIPT_METHOD_NAME, SANSKRIT99_TO_UNICODE_JAVASCRIPT_FILENAME)
                    + "\n" + ExceptionUtils.getStackTrace(e);
            LOGGER.error(errorMsg);
            throw new IllegalStateException(errorMsg, e);
        } catch (FileNotFoundException e) {
            String errorMsg = String.format("Catastrophic error! File [%s] is not a file or does not exist.", classPathResource) + "\n"
                    + ExceptionUtils.getStackTrace(e);
            LOGGER.error(errorMsg);
            throw new IllegalStateException(errorMsg, e);
        } catch (IOException e) {
            String errorMsg = String
                    .format("Catastrophic error! Resource [%s] cannot be resolved as absolute file path. Is the resource available on the file system?",
                            classPathResource)
                    + "\n" + ExceptionUtils.getStackTrace(e);
            LOGGER.error(errorMsg);
            throw new IllegalStateException(errorMsg, e);
        }

        LOGGER.debug("Input [{}] --> Output [{}]", input, output);
        return output;
    }

    @Override
    public String convertHtmlBlock(String input) {
        // special logic for Sanskrit99, because "<" is actually used as a
        // character for replacement. cannot straight up convert unescape and
        // then escape text according to HTML, because then other character
        // (like the en-dash) which previously were not escaped will be escaped
        // and cause problems in Sigil.

        Map<String, String> replacements = new LinkedHashMap<String, String>();
        replacements.put("&lt;", "<");

        String output = convertHtmlBlockWithSpecialReplacements(input, replacements);
        return output;
    }

}