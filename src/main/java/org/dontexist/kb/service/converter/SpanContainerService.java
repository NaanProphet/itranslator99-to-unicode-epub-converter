package org.dontexist.kb.service.converter;

public interface SpanContainerService {

    String parseSpanAttribute(String spanOpeningTag);

    Text2UnicodeService getUnicodeConverter(String spanAttribute);

    Text2UnicodeService getDefaultUnicodeConverter();
}
