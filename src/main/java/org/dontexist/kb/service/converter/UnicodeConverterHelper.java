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
        // do not escape html characters yet, because we need to split
        // based on tags
        StringBuilder outputStringy = new StringBuilder();
        int fromIndex = 0;
        while (true) {
            int spanStart = entireFileAsOneString.indexOf(SPAN_START_TAG, fromIndex);
            final boolean isNoStartingSpanTagFound = spanStart == -1;
            if (isNoStartingSpanTagFound) {
                // flush out rest of file and quit
                String restOfFile = entireFileAsOneString.substring(fromIndex);
                String convertedRestOfFile = romanizedSanskritConverter.convertHtmlBlock(restOfFile);
                outputStringy.append(convertedRestOfFile);
                break;
            }
            String partBeforeSpan = entireFileAsOneString.substring(fromIndex, spanStart);
            // assumption: sanskrit99 text will never be outside of span block, but palladio might
            String convertedPartBeforeSpan = romanizedSanskritConverter.convertHtmlBlock(partBeforeSpan);
            outputStringy.append(convertedPartBeforeSpan);


            int spanEnd = entireFileAsOneString.indexOf(SPAN_END_TAG, spanStart);
            int beginIndex = spanStart;
            int endIndex = spanEnd + SPAN_END_TAG.length();
            final StringBuilder spanString = new StringBuilder(entireFileAsOneString.substring(beginIndex, endIndex));

            // check for nested span elements
            int numSpanStarts = countMatches(spanString, SPAN_START_TAG);
            int numSpanEnds = countMatches(spanString, SPAN_END_TAG);
            // TODO refactor this
            int newEndIndex = endIndex;
            while (numSpanStarts != numSpanEnds) {
                // continue looking for the end span tag
                int newSpanStart = endIndex;
                int newSpanEnd = entireFileAsOneString.indexOf(SPAN_END_TAG, newSpanStart);
                int newBeginIndex = newSpanStart;
                newEndIndex = newSpanEnd + SPAN_END_TAG.length();
                String appendMe = entireFileAsOneString.substring(newBeginIndex, newEndIndex);
                spanString.append(appendMe);
                numSpanStarts = countMatches(spanString, SPAN_START_TAG);
                numSpanEnds = countMatches(spanString, SPAN_END_TAG);
            }

            // split span string with parser
            final StringBuilder convertedSpanString = new StringBuilder();
            List<ParseContainer> textBlocks = splitUpSpanString(spanString.toString());
            for (ParseContainer entry : textBlocks) {
                final String textToConvert = entry.getRawText();
                final Text2UnicodeService converter = entry.getConverter();
                final String convertedText = entry.isHtmlTag() ? converter.convertHtmlBlock(textToConvert) : converter.convert(textToConvert);
                convertedSpanString.append(convertedText);
            }

            // prepare for next iteration
            fromIndex = newEndIndex;

            // flush work
            outputStringy.append(convertedSpanString);
        }
        // add final newline at end to make files identical
        outputStringy.append("\n");
        return outputStringy;
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
