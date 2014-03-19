package org.dontexist.kb.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PalladioIT2UnicodeConverterServiceImpl extends AbstractText2UnicodeService implements PalladioIT2UnicodeConverterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PalladioIT2UnicodeConverterServiceImpl.class);

	// certain replacements have to occur in order, so used an LINKED hash map
	// (ordered by insert order)
	private Map<Character, Character> it2UnicodeMap = new LinkedHashMap<Character, Character>();

	private Map<String, String> postReplacementCorrections = new HashMap<String, String>();

	public PalladioIT2UnicodeConverterServiceImpl() {
		initializeMappings();
	}

	/**
	 * Converts PalladioIT input to Unicode output.
	 * 
	 * @param input
	 * @return
	 */
	@Override
	public String convert(final String input) {
		String output = input; // initialize

		for (Character itCharToConvert : it2UnicodeMap.keySet()) {
			String from = itCharToConvert.toString();
			String to = it2UnicodeMap.get(itCharToConvert).toString();
			output = output.replaceAll(from, to);
		}

		// perform special replacements to correct non-sanskrit accented words
		// that might have been incorrectly changed
		output = performSpecialPostReplacementCorrections(output);

		LOGGER.debug("Converted input [{}] to unicode output [{}]", input, output);
		return output;
	}

	private String performSpecialPostReplacementCorrections(final String output) {
		String correctedOutput = output; // initialize
		for (String from : postReplacementCorrections.keySet()) {
			String to = postReplacementCorrections.get(from);
			correctedOutput = StringUtils.replace(output, from, to);
		}
		LOGGER.debug("Performed post replacements corrections on [{}] to [{}]", output, correctedOutput);
		return correctedOutput;
	}

	private void initializeMappings() {
		// @sonar:ofs
		// @formatter:off
		// 			IT code point, 	Unicode code point 	// Unicode character to convert to
		
		// ------ START OF ORDER-DEPENDANT REPLACEMENTS --------
		// to prevent unintentional replacements-of-replacements,
		// the following must be performed in order given below
		
		addToMap(	0x00D1, 		0x1E62); 			// Ñ -> Ṣ
		addToMap(	0x00CF, 		0x00D1); 			// Ï -> Ñ
		addToMap(	0x00F1, 		0x1E63); 			// ñ -> ṣ
		addToMap(	0x00EF, 		0x00F1); 			// ï -> ñ
		
		// ------ END OF ORDER-DEPENDANT REPLACEMENTS --------
		
		addToMap(	0x00C4, 		0x0100); 			// Ä -> Ā
		addToMap(	0x00E4, 		0x0101); 			// ä -> ā
		addToMap(	0x00C9, 		0x012A); 			// É -> Ī
		addToMap(	0x00E9, 		0x012B); 			// é -> ī
		addToMap(	0x00C7, 		0x015A); 			// Ç -> Ś
		addToMap(	0x00E7, 		0x015B); 			// ç -> ś
		addToMap(	0x00DC, 		0x016A); 			// Ü -> Ū
		addToMap(	0x00FC, 		0x016B); 			// ü -> ū
		addToMap(	0x00D2, 		0x1E0C); 			// Ò -> Ḍ
		addToMap(	0x00F2, 		0x1E0D); 			// ò -> ḍ
		addToMap(	0x00D9, 		0x1E24); 			// Ù -> Ḥ
		addToMap(	0x00F9, 		0x1E25); 			// ù -> ḥ
		addToMap(	0x00DF, 		0x1E36); 			// ß -> Ḷ
		addToMap(	0x00C0, 		0x1E40); 			// À -> Ṁ
		addToMap(	0x00E0, 		0x1E41); 			// à -> ṁ
		addToMap(	0x00CC, 		0x1E44); 			// Ì -> Ṅ
		addToMap(	0x00EC, 		0x1E45); 			// ì -> ṅ
		addToMap(	0x00CB, 		0x1E46); 			// Ë -> Ṇ
		addToMap(	0x00EB, 		0x1E47); 			// ë -> ṇ
		addToMap(	0x00C5, 		0x1E5A); 			// Å -> Ṛ
		addToMap(	0x00E5, 		0x1E5B); 			// å -> ṛ
		addToMap(	0x00C8, 		0x1E5C); 			// È -> Ṝ
		addToMap(	0x00E8, 		0x1E5D); 			// è -> ṝ
		addToMap(	0x00D6, 		0x1E6C); 			// Ö -> Ṭ
		addToMap(	0x00F6, 		0x1E6D); 			// ö -> ṭ
		addToMap(	0x00D7, 		0x030D); 			// × -> Vedik accent '
		addToMap(	0x00DE, 		0x0331); 			// Þ -> Vedik accent _
		addToMap(	0x00F7, 		0x030E); 			// ÷ -> Vedik accent ''
		// @formatter:on
		// @sonar:on

		// ---- POST REPLACEMENT CORRECTION MAPPINGS
		postReplacementCorrections.put("fiancī", "fiancé");
	}

	private void addToMap(int itCodePoint, int unicodeCodePoint) {
		it2UnicodeMap.put((char) itCodePoint, (char) unicodeCodePoint);
	}

}
