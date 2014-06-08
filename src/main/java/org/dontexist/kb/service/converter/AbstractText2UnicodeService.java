package org.dontexist.kb.service.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Common class for converting text in various encodings into Unicode.
 */
public abstract class AbstractText2UnicodeService {

    /**
     * Performs the Unicode conversion.
     *
     * @param input the string to be converted
     * @return Unicode converted equivalent
     */
    public abstract String convert(final String input);

    protected List<Integer> findAllIndexOf(final String input, final String searchFor) {
        List<Integer> indexes = new ArrayList<Integer>();
        for (int index = input.indexOf(searchFor); index >= 0; index = input.indexOf(searchFor, index + 1)) {
            indexes.add(index);
        }
        return indexes;
    }

    @SuppressWarnings("unchecked")
    public String convertHtmlBlock(final String input) {
        return convertHtmlBlockWithSpecialReplacements(input, MapUtils.EMPTY_SORTED_MAP);
    }

    String convertHtmlBlockWithSpecialReplacements(final String input, final Map<String, String> replacements) {
        List<Integer> indexesOfBegTag = findAllIndexOf(input, "<");
        List<Integer> indexesOfEndTag = findAllIndexOf(input, ">");
        Assert.isTrue(indexesOfBegTag.size() == indexesOfEndTag.size(),
                "Mismatched number of < and > tags! Was a complete HTML tag passed in?");

        StringBuilder output = new StringBuilder();
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

    /**
     * Performs a series of string replacements. <p>Note: the order of the replacements depends on the inherent ordering
     * of the {@link Map} used.</p>
     *
     * @param preConvertString the text to perform replacements on
     * @param replacements     a {@link java.util.Map} containing searches as the keys and the replacements as the
     *                         values
     * @return the replaced text
     */
    private String performSpecialReplacements(final String preConvertString, final Map<String, String> replacements) {
        String replacedString = preConvertString; // initialization
        for (Entry<String, String> entry : replacements.entrySet()) {
            String from = entry.getKey();
            String to = entry.getValue();
            replacedString = StringUtils.replace(replacedString, from, to);
        }
        return replacedString;
    }

}
