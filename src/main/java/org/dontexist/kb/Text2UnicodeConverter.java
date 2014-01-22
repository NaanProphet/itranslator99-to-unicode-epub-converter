package org.dontexist.kb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.util.Assert;

public abstract class Text2UnicodeConverter {

	public abstract String convert(String input) throws IOException, ScriptException, NoSuchMethodException;

	protected List<Integer> findAllIndexOf(String input, String searchFor) {
		List<Integer> indexes = new ArrayList<Integer>();
		for (int index = input.indexOf(searchFor); index >= 0; index = input.indexOf(searchFor, index + 1)) {
			indexes.add(index);
		}
		return indexes;
	}

	public String convertHtmlBlock(String input) throws IOException, ScriptException, NoSuchMethodException {

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
				String convertString = input.substring(ithEndIndex + 1, indexesOfBegTag.get(i + 1));
				String convertedString = convert(convertString);
				output.append(convertedString);
			}

		}

		return output.toString();
	}

}
