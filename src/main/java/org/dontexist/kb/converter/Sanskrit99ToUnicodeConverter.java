package org.dontexist.kb.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class Sanskrit99ToUnicodeConverter extends Text2UnicodeConverter {

	private static final String JAVASCRIPT_METHOD_NAME = "convert_to_unicode";
	private static final String SANSKRIT99_TO_UNICODE_JAVASCRIPT_FILENAME = "sanskrit99_to_unicode.js";
	private static final Logger LOGGER = LoggerFactory.getLogger(Sanskrit99ToUnicodeConverter.class);

	private final ClassPathResource classPathResource = new ClassPathResource(SANSKRIT99_TO_UNICODE_JAVASCRIPT_FILENAME);

	/**
	 * Converts Sanskrit99 input to Unicode output.
	 * <p>
	 * 
	 * @param input
	 * @return Unicode output <b>with characters escaped for HTML4<b>!!
	 * @throws IllegalStateException
	 *             if error occurs while attempting to read/call Javascript file
	 *             that is used for conversion
	 */
	@SuppressWarnings("restriction")
	@Override
	public String convert(final String input) {

		// initialize
		String output = null;

		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		Invocable inv = (Invocable) engine;

		try {
			File file = classPathResource.getFile(); // IOException
			final FileReader fileReader = new FileReader(file); // FileNotFoundException
			engine.eval(fileReader); // ScriptException
			output = (String) inv.invokeFunction(JAVASCRIPT_METHOD_NAME, input); // NoSuchMethodException

			// ------- YUCKY YUCKY. TODO UPGRADE TO JAVA 7 AND USE MULTI-CATCH
		} catch (ScriptException e) {
			String errorMsg = String.format("Catastrophic error! Problem loading Javascript file [%s]. Please check the file for errors.",
					SANSKRIT99_TO_UNICODE_JAVASCRIPT_FILENAME) + "\n" + e.getStackTrace().toString();
			LOGGER.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		} catch (NoSuchMethodException e) {
			String errorMsg = String
					.format("Catastrophic error! Cannot find method [%s] on the Javascript file [%s]. Cannot continue; please check if the file is corrupt or has recently changed.",
							JAVASCRIPT_METHOD_NAME, SANSKRIT99_TO_UNICODE_JAVASCRIPT_FILENAME)
					+ "\n" + e.getStackTrace().toString();
			LOGGER.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		} catch (FileNotFoundException e) {
			String errorMsg = String.format("Catastrophic error! File [{}] is not a file or does not exist.", classPathResource) + "\n"
					+ e.getStackTrace().toString();
			LOGGER.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		} catch (IOException e) {
			String errorMsg = String
					.format("Catastrophic error! Resource [{}] cannot be resolved as absolute file path. Is the resource available on the file system?",
							classPathResource)
					+ "\n" + e.getStackTrace().toString();
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