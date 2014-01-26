package org.dontexist.kb.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public abstract class Text2UnicodeConverter {

	public abstract String convert(String input);

	protected List<Integer> findAllIndexOf(String input, String searchFor) {
		List<Integer> indexes = new ArrayList<Integer>();
		for (int index = input.indexOf(searchFor); index >= 0; index = input.indexOf(searchFor, index + 1)) {
			indexes.add(index);
		}
		return indexes;
	}

	public String convertXml(String input) {
		// unescape XML text before converting
		final String unescapedInput = StringEscapeUtils.unescapeXml(input);
		String output = convert(unescapedInput);
		// escape back before outputting
		final String escapedOutput = StringEscapeUtils.escapeXml(output);
		return escapedOutput;
	}

	@SuppressWarnings("unchecked")
	public String convertHtmlBlock(String input) {
		return convertHtmlBlockWithSpecialReplacements(input, MapUtils.EMPTY_SORTED_MAP);
	}

	String convertHtmlBlockWithSpecialReplacements(String input, Map<String, String> replacements) {
		List<Integer> indexesOfBegTag = findAllIndexOf(input, "<");
		List<Integer> indexesOfEndTag = findAllIndexOf(input, ">");
		Assert.isTrue(indexesOfBegTag.size() == indexesOfEndTag.size(),
				"Mismatched number of < and > tags! Was a complete HTML tag passed in?");

		StringBuffer output = new StringBuffer();
		for (int i = 0; i < indexesOfBegTag.size(); i++) {
			int ithBegIndex = indexesOfBegTag.get(i); // <
			int ithEndIndex = indexesOfEndTag.get(i); // >

			String preString = input.substring(ithBegIndex, ithEndIndex + 1); // include
																				// the
																				// >
																				// character
			output.append(preString);

			if ((i + 1) != indexesOfBegTag.size()) {
				String preConvertString = input.substring(ithEndIndex + 1, indexesOfBegTag.get(i + 1));

				// perform special replacements on HTML body, if necessary (e.g.
				// in Sanskrit99 the "<" is used and will be written as "&lt;"
				// in the HTML block but it cannot be replaced before because
				// then it would throw off the beginning- and end-tag search
				// logic
				String convertString = performSpecialReplacements(preConvertString, replacements);

				String convertedString = convert(convertString);
				output.append(convertedString);
			}

		}

		return output.toString();
	}

	private String performSpecialReplacements(String preConvertString, Map<String, String> replacements) {
		String replacedString = preConvertString; // initialization
		for (String from : replacements.keySet()) {
			String to = replacements.get(from);
			replacedString = StringUtils.replace(replacedString, from, to);
		}
		return replacedString;
	}

}
