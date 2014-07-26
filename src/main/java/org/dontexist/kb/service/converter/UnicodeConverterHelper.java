package org.dontexist.kb.service.converter;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.*;

@Service
public class UnicodeConverterHelper implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnicodeConverterHelper.class);

    private static final String SPAN_END_TAG = "</span>";

    private static final String SPAN_START_TAG = "<span";

    @Autowired
    private Sanskrit99ToUnicodeService devanagariSanskritConverter;

    @Autowired
    private PalladioIT2UnicodeConverterServiceImpl romanizedSanskritConverter;

    @Autowired
    private SpanContainerService spanContainerService;

    @Value("${sanskrit99.span.class}")
    private String sanskrit99SpanClassCsv;

    private Set<String> sanskrit99SpanClasses = new HashSet<String>();


    public StringBuilder convertFileAsOneStringToUnicode(final String entireFileAsOneString) throws IOException {
        // delegate escaping of html characters to conversion service, because we need to split based on the  tags
        final StringBuilder convertedOutputAsOneString = new StringBuilder();
        int fromIndex = 0;
        while (true) {
            final int spanStart = entireFileAsOneString.indexOf(SPAN_START_TAG, fromIndex);
            final boolean isNoStartingSpanTagFound = spanStart == -1;
            if (isNoStartingSpanTagFound) {
                // flush out rest of file and quit
                final String restOfFile = entireFileAsOneString.substring(fromIndex);
                final String convertedRestOfFile = romanizedSanskritConverter.convertHtmlBlock(restOfFile);
                convertedOutputAsOneString.append(convertedRestOfFile);
                break;
            }
            final String partBeforeSpan = entireFileAsOneString.substring(fromIndex, spanStart);
            // assumption: sanskrit99 text will never be outside of span block, but palladio might
            final String convertedPartBeforeSpan = spanContainerService.getDefaultUnicodeConverter().convertHtmlBlock(partBeforeSpan);
            convertedOutputAsOneString.append(convertedPartBeforeSpan);

            // loop to find entire span block, possibly returning nested span blocks
            final String spanString = readForCompleteSpanBlock(entireFileAsOneString, spanStart);

            // split span string with parser
            final List<ParseContainer> textBlocks = splitUpSpanString(spanString.toString());
            final StringBuilder convertedSpanString = convertSpanTextBlocks(textBlocks);

            // flush work
            convertedOutputAsOneString.append(convertedSpanString);

            // prepare for next iteration
            fromIndex = spanStart + spanString.length();
        }
        // add final newline at end to make files identical
        convertedOutputAsOneString.append("\n");
        return convertedOutputAsOneString;
    }

    private StringBuilder convertSpanTextBlocks(List<ParseContainer> textBlocks) {
        final StringBuilder convertedSpanString = new StringBuilder();
        for (ParseContainer entry : textBlocks) {
            final String textToConvert = entry.getRawText();
            final Text2UnicodeService converter = entry.getConverter();
            final String convertedText = entry.isHtmlTag() ? converter.convertHtmlBlock(textToConvert) : converter.convert(textToConvert);
            convertedSpanString.append(convertedText);
        }
        return convertedSpanString;
    }

    private String readForCompleteSpanBlock(String entireFileAsOneString, int spanStart) {
        final StringBuilder spanString = new StringBuilder();
        int newEndIndex = spanStart;
        int numSpanStarts = 0;
        int numSpanEnds = 0;
        do {
            // continue looking for the end span tag
            final int newSpanStart = newEndIndex;
            final int newSpanEnd = entireFileAsOneString.indexOf(SPAN_END_TAG, newSpanStart);
            final int newBeginIndex = newSpanStart;
            newEndIndex = newSpanEnd + SPAN_END_TAG.length();
            final String portionOfSpanBlockFound = entireFileAsOneString.substring(newBeginIndex, newEndIndex);
            spanString.append(portionOfSpanBlockFound);
            // check count of nested span elements
            numSpanStarts = countMatches(spanString, SPAN_START_TAG);
            numSpanEnds = countMatches(spanString, SPAN_END_TAG);
        } while (numSpanStarts != numSpanEnds);
        return spanString.toString();
    }

    List<ParseContainer> splitUpSpanString(final String spanString) {
        // TODO make service
        try {
            Parser parser = new Parser();
            parser.setInputHTML(spanString);
            String[] tagsToFind = {"span"};
            final MyVisitor visitor = new MyVisitor();
            parser.visitAllNodesWith(visitor);
            return visitor.textBlocks;
        } catch (ParserException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String performUnicodeReplacementOnSpanBlock(final String spanString) {
        String convertedSpanString;

        // allow both santext, santext1, etc. to be replaced
        boolean isSpanStringSanskrit = false; // default
        for (String validSanskritSpanClass : sanskrit99SpanClasses) {
            if (contains(spanString, "class=\"" + validSanskritSpanClass + "\"")) {
                isSpanStringSanskrit = true;
                break;
            }
        }

        if (isSpanStringSanskrit) {
            convertedSpanString = devanagariSanskritConverter.convertHtmlBlock(spanString);
        } else {
            convertedSpanString = romanizedSanskritConverter.convertHtmlBlock(spanString);
        }
        return convertedSpanString;
    }

    @Override
    public void afterPropertiesSet() {
        addSanskritSpanClasses(sanskrit99SpanClassCsv);
    }

    void addSanskritSpanClasses(final String csvString) {
        String[] spanClasses = split(csvString, ",");
        if (spanClasses == null) {
            return;
        }
        for (String ithSpanClass : spanClasses) {
            sanskrit99SpanClasses.add(ithSpanClass);
        }
    }

    /**
     * @return a Set of all &lt;span&gt; "class" attributes that correspond to devanagari sanskrit unicode blocks.
     */
    public Set<String> getSanskrit99SpanClasses() {
        return sanskrit99SpanClasses;
    }

    /**
     * @return converter for non-standard devanagari sanskrit text
     */
    public Text2UnicodeService getDevanagariSanskritConverter() {
        return devanagariSanskritConverter;
    }

    /**
     * @return converter for non-standard romanized sanskrit text
     */
    public Text2UnicodeService getRomanizedSanskritConverter() {
        return romanizedSanskritConverter;
    }

    /**
     * For parsing valid span tags and pairing text with the appropriate Unicode converted. Works on nested tags too.
     * <p> Note: tags assumed to be well formed, starting with &lt;span&gt; and ending with &lt;/span&gt; </p>
     */
    private class MyVisitor extends NodeVisitor {

        Stack<Text2UnicodeService> spanConverters = new Stack<Text2UnicodeService>();
        List<ParseContainer> textBlocks = new ArrayList<ParseContainer>();

        @Override
        public void visitTag(Tag tag) {
            final String text = "<" + tag.getText() + ">";
            // we may get <br /> tags inside nested spans, so check if this tag is actually a span tag
            if (StringUtils.startsWith(text, "<span")) {
                String spanAttribute = spanContainerService.parseSpanAttribute(text);
                Text2UnicodeService converter = spanContainerService.getUnicodeConverter(spanAttribute);
                spanConverters.push(converter);
            }

            ParseContainer entry = new ParseContainer(text, spanContainerService.getDefaultUnicodeConverter(), true);
            textBlocks.add(entry);


        }

        @Override
        public void visitEndTag(Tag tag) {
            final String text = "<" + tag.getText() + ">";
            // only pop off if end tag is for span, because of visitTag logic
            if ("</span>".equals(text)) {
                spanConverters.pop();
            }
            ParseContainer entry = new ParseContainer(text, spanContainerService.getDefaultUnicodeConverter(), true);
            textBlocks.add(entry);
        }

        @Override
        public void visitStringNode(Text string) {
            final String text = string.getText();
            final Text2UnicodeService converter = spanConverters.peek();
            // perform replacements on sanskrit99 text so that e.g. &lt; gets replaced by < before conversion
            String finalText = text;
            if (converter instanceof Sanskrit99ToUnicodeService) {
                finalText = devanagariSanskritConverter.performPreConvertReplacements(text);

            }
            final boolean treatAsHtmlTag = false;
            ParseContainer entry = new ParseContainer(finalText, converter, treatAsHtmlTag);
            textBlocks.add(entry);
        }

    }

    final class MyEntry<String, Text2UnicodeService> implements Map.Entry<String, Text2UnicodeService> {
        private final String key;
        private Text2UnicodeService value;

        public MyEntry(String key, Text2UnicodeService value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Text2UnicodeService getValue() {
            return value;
        }

        @Override
        public Text2UnicodeService setValue(Text2UnicodeService value) {
            Text2UnicodeService old = this.value;
            this.value = value;
            return old;
        }
    }
}
