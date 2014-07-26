package org.dontexist.kb.service.converter;

/**
 * Converts non-Unicode text into their Unicode equivalents.
 */
public interface Text2UnicodeService {

    String convertHtmlBlock(String input);

    String convert(String input);
}
