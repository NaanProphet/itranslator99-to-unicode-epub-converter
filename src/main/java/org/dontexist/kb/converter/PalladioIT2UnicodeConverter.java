package org.dontexist.kb.converter;

import java.io.IOException;

import javax.script.ScriptException;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PalladioIT2UnicodeConverter extends Text2UnicodeConverter {

	private static final Logger logger = LoggerFactory.getLogger(PalladioIT2UnicodeConverter.class);

	private BidiMap<Character, Character> it2UnicodeMap = new DualHashBidiMap<Character, Character>();

	/**
	 * Converts PalladioIT input to Unicode output.
	 * @param input
	 * @return
	 * @throws IOException
	 * @throws ScriptException
	 * @throws NoSuchMethodException
	 */
	@Override
	public String convert(final String input) throws IOException, ScriptException, NoSuchMethodException {
		// unescape HTML text before converting
		final String unescapedInput = StringEscapeUtils.unescapeHtml4(input);
		
		String output = unescapedInput; // initialize
		for (Character itCharToConvert : it2UnicodeMap.keySet()) {
			String from = itCharToConvert.toString();
			String to = it2UnicodeMap.get(itCharToConvert).toString();
			output = output.replaceAll(from, to);
		}
		
		// escape back before outputting
		final String escapedOutput = StringEscapeUtils.escapeHtml4(output);
		
		logger.debug("Converted input [{}] to unicode output [{}]", input, escapedOutput);
		return escapedOutput;
	}

	// initialize mapping
	{
		// @formatter:off
		// 			IT code point, 	Unicode code point 	// Unicode character to convert to
		addToMap(	0x00CF, 		0x00D1); 			// Ñ
		addToMap(	0x00EF, 		0x00F1); 			// ñ
		addToMap(	0x00C4, 		0x0100); 			// Ā
		addToMap(	0x00E4, 		0x0101); 			// ā
		addToMap(	0x00C9, 		0x012A); 			// Ī
		addToMap(	0x00E9, 		0x012B); 			// ī
		addToMap(	0x00C7, 		0x015A); 			// Ś
		addToMap(	0x00E7, 		0x015B); 			// ś
		addToMap(	0x00DC, 		0x016A); 			// Ū
		addToMap(	0x00FC, 		0x016B); 			// ū
		addToMap(	0x00D2, 		0x1E0C); 			// Ḍ
		addToMap(	0x00F2, 		0x1E0D); 			// ḍ
		addToMap(	0x00D9, 		0x1E24); 			// Ḥ
		addToMap(	0x00F9, 		0x1E25); 			// ḥ
		addToMap(	0x00DF, 		0x1E36); 			// Ḷ
		addToMap(	0x00C0, 		0x1E40); 			// Ṁ
		addToMap(	0x00E0, 		0x1E41); 			// ṁ
		addToMap(	0x00CC, 		0x1E44); 			// Ṅ
		addToMap(	0x00EC, 		0x1E45); 			// ṅ
		addToMap(	0x00CB, 		0x1E46); 			// Ṇ
		addToMap(	0x00EB, 		0x1E47); 			// ṇ
		addToMap(	0x00C5, 		0x1E5A); 			// Ṛ
		addToMap(	0x00E5, 		0x1E5B); 			// ṛ
		addToMap(	0x00C8, 		0x1E5C); 			// Ṝ
		addToMap(	0x00E8, 		0x1E5D); 			// ṝ
		addToMap(	0x00D1, 		0x1E62); 			// Ṣ
		addToMap(	0x00F1, 		0x1E63); 			// ṣ
		addToMap(	0x00D6, 		0x1E6C); 			// Ṭ
		addToMap(	0x00F6, 		0x1E6D); 			// ṭ
		addToMap(	0x00D7, 		0x030D); 			// Vedik accent '
		addToMap(	0x00DE, 		0x0331); 			// Vedik accent _
		addToMap(	0x00F7, 		0x030E); 			// Vedik accent ''
		// @formatter:on
	}

	private void addToMap(int itCodePoint, int unicodeCodePoint) {
		it2UnicodeMap.put((char) itCodePoint, (char) unicodeCodePoint);
	}

}
