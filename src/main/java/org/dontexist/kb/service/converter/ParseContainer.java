package org.dontexist.kb.service.converter;

/**
 * Container class for parsing bits of HTML tags.
 */
public class ParseContainer {

    private final String rawText;

    private final Text2UnicodeService converter;

    private final boolean isHtmlTag;

    public ParseContainer(String rawText, Text2UnicodeService converter, boolean isHtmlTag) {
        this.rawText = rawText;
        this.converter = converter;
        this.isHtmlTag = isHtmlTag;
    }

    public String getRawText() {
        return rawText;
    }

    public Text2UnicodeService getConverter() {
        return converter;
    }

    public boolean isHtmlTag() {
        return isHtmlTag;
    }
}
