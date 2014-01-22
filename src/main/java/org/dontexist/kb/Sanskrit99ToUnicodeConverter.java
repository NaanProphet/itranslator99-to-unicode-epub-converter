package org.dontexist.kb;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class Sanskrit99ToUnicodeConverter extends Text2UnicodeConverter {

	private static final String SANSKRIT99_TO_UNICODE_JAVASCRIPT_FILE = "sanskrit99_to_unicode.js";
	private static final Logger logger = LoggerFactory.getLogger(Sanskrit99ToUnicodeConverter.class);

	public String convert(final String input) throws IOException, ScriptException, NoSuchMethodException {
		// unescape HTML text before converting
		final String unescapedInput = StringEscapeUtils.unescapeHtml4(input);

		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		File file = new ClassPathResource(SANSKRIT99_TO_UNICODE_JAVASCRIPT_FILE).getFile();
		engine.eval(new FileReader(file));
		Invocable inv = (Invocable) engine;
		String output = (String) inv.invokeFunction("convert_to_unicode", unescapedInput);

		// escape back before outputting
		final String escapedOutput = StringEscapeUtils.escapeHtml4(output);

		logger.debug("Input [{}] --> Output [{}]", input, escapedOutput);
		return escapedOutput;
	}

}