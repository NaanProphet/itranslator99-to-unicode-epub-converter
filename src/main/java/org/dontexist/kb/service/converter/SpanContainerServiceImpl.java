package org.dontexist.kb.service.converter;

import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static java.lang.String.format;

@Component
public class SpanContainerServiceImpl implements SpanContainerService {

    @Autowired
    private UnicodeConverterHelper unicodeConverterHelper;

    /**
     * @param spanOpeningTag assumed a valid, well-formed &lt;span&gt; opening tag ONLY (can have attributes). must have opening and closing angle brackets
     * @return
     * @throws org.htmlparser.util.ParserException
     */
    @Override
    public String parseSpanAttribute(final String spanOpeningTag) {
        TagNameFilter filter = new TagNameFilter("span");
        Parser parser = new Parser();
        NodeList nodeList;
        try {
            parser.setInputHTML(spanOpeningTag);
            nodeList = parser.parse(filter);
        } catch (ParserException e) {
            throw new IllegalArgumentException(format("Could not parse attributes from <span> tag [%s]", spanOpeningTag), e);
        }

        // sanity check
        final int numSpanTagsFound = nodeList.size();
        Assert.isTrue(numSpanTagsFound == 1,
                "Only expecting one <span> tag but found " + numSpanTagsFound +
                        "! Cannot parse attribute from input text: " + spanOpeningTag);
        TagNode node = (TagNode) nodeList.elementAt(0);
        String classVal = node.getAttribute("class");
        return classVal;
    }

    /**
     * Uses the assigned enum value of the literal text to determine which unicode parser to use <p> Note: though this
     * matches the strategy pattern, the code is not in the enum itself to use Spring autowiring. </p>
     *
     * @return devanagari converter if TODO
     */
    @Override
    public Text2UnicodeService getUnicodeConverter(final String spanAttribute) {
        if (unicodeConverterHelper.getSanskrit99SpanClasses().contains(spanAttribute)) {
            return unicodeConverterHelper.getDevanagariSanskritConverter();
        } else {
            return unicodeConverterHelper.getRomanizedSanskritConverter();
        }
    }

    @Override
    public Text2UnicodeService getDefaultUnicodeConverter() {
        return unicodeConverterHelper.getRomanizedSanskritConverter();
    }

}
