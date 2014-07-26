package org.dontexist.kb.service.converter;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.*;


/**
 * Indicates which section of an HTML &lt;span&gt; text belongs to.
 */
public enum SpanSection {

    SPAN_OPENING_TAG,
    SPAN_CLOSING_TAG,
    TEXT_CONTENT;

    /**
     * TODO javadoc
     *
     * @param literalText
     * @return
     */
    public static SpanSection matchToEnum(final String literalText) {
        int numSpanClosing = countMatches(literalText, "</span>");
        int numSpanOpeningPseudo = countMatches(literalText, "<span");

        if (numSpanClosing == 0 && numSpanOpeningPseudo == 0) {
            return TEXT_CONTENT;
        }

        if (numSpanClosing == 1 && numSpanOpeningPseudo == 0) {
            // verify there is no additional text
            if ("</span>".equals(literalText)) {
                return SPAN_CLOSING_TAG;
            }
        }

        if (numSpanClosing == 0 && numSpanOpeningPseudo == 1) {

            if (
                // if the text is just a <span attrib1="val1"> block, it should end with >
                // not using HTML parsers b/c they are too lenient
                    startsWith(literalText, "<") &&
                    endsWith(literalText, ">") &&
                            // make sure there are no floating brackets inside
                            countMatches(literalText, "<") == 1 &&
                            countMatches(literalText, ">") == 1
                    ) {
                return SPAN_OPENING_TAG;
            }
        }

        final String errMessage = format(
                "Invalid text block! Contains [%s] closing </span> tags " +
                        "and [%s] opening <span> tags, but must contain only 1 of either OR text only. " +
                        "Cannot assign enum to input [%s]", numSpanClosing, numSpanOpeningPseudo, literalText);
        throw new IllegalArgumentException(errMessage);


    }

}
